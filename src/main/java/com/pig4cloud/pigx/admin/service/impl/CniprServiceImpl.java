package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.config.CnirpConfig;
import com.pig4cloud.pigx.admin.constants.CnirpApiConstants;
import com.pig4cloud.pigx.admin.entity.PatentFetchCheckpointEntity;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.service.CniprService;
import com.pig4cloud.pigx.admin.service.PatentFetchCheckpointService;
import com.pig4cloud.pigx.admin.service.PatentLogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaoliang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CniprServiceImpl implements CniprService {

    private static final String TASK_KEY = "cnipr_all_patents";
    private static final int PAGE_SIZE = 10;
    private final StringRedisTemplate stringRedisTemplate;
    private final CnirpConfig cnirpConfig;
    private final PatentLogService patentLogService;
    private final PatentFetchCheckpointService patentFetchCheckpointService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void fetchAllPatents(String exp,
                                String dbs,
                                int option,
                                String order,
                                String displayCols,
                                boolean highLight,
                                boolean isDbAgg) {
        // 1. 读断点
        PatentFetchCheckpointEntity chk = patentFetchCheckpointService.getByTaskKey(TASK_KEY);
        int offset = (chk != null ? chk.getOffset() : 0);
        long total = (chk != null && chk.getTotal() != null ? chk.getTotal() : Long.MAX_VALUE);

        log.info("→ 从 offset={} 开始拉取，总量={}", offset, total);

        try {
            while (offset < total) {
                // 2. 分页调用 3. 批量入库（可先去重再存）
                Page<PatentLogEntity> page = this.page(
                        exp, dbs, option, order, offset, PAGE_SIZE, displayCols, highLight, isDbAgg);


                // 4. 更新 offset & total
                offset += PAGE_SIZE;
                total = page.getTotal();
                patentFetchCheckpointService.saveOrUpdateOffset(TASK_KEY, offset, total);

                log.info("→ 已拉取 {} 条，下一 offset={}", offset, offset);
            }

            // 5. 全部拉完后清理断点
            patentFetchCheckpointService.deleteByTaskKey(TASK_KEY);
            log.info("✅ 全量拉取完成，已清理断点记录");

        } catch (Exception ex) {
            log.error("❌ 拉取异常，中断 offset={}", offset, ex);
            throw ex;
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Page<PatentLogEntity> page(String exp, String dbs, int option, String order, int from, int size, String displayCols, boolean highLight, boolean isDbAgg) {
        Map<String, String> auth = this.getAuth();
        Map<String, Object> param = new HashMap<>();
        param.put("openid", MapUtil.getStr(auth, "open_id"));
        param.put("access_token", MapUtil.getStr(auth, "access_token"));
        param.put("exp", exp);
        param.put("dbs", dbs);
        param.put("option", option);
        param.put("order", order);
        param.put("from", from);
        param.put("size", size);
        param.put("displayCols", displayCols);
        param.put("highLight", highLight);
        param.put("isDbAgg", isDbAgg);
        JSONObject patentResult = this.doPost(
                StrUtil.replace(CnirpApiConstants.GET_PATENT_LIST, "{client_id}", cnirpConfig.getClientId()),
                param);
        Page<PatentLogEntity> page = new Page<>();
        page.setTotal(MapUtil.getLong(patentResult, "total"));
        String results = patentResult.getStr("results");
        JSONArray resultJsonArray = JSONUtil.parseArray(results);
        List<PatentLogEntity> patentLogEntityList = Lists.newArrayList();
        resultJsonArray.forEach(item -> {
            JSONObject resultJson = JSONUtil.parseObj(item);
            PatentLogEntity patentLogEntity = new PatentLogEntity();
            patentLogEntity.setPid(resultJson.getStr("pid"));
            patentLogEntity.setRequestParam(JSONUtil.toJsonStr(param));
            patentLogEntity.setResponseBody(resultJson.toString());
            patentLogEntity.setAppDate(resultJson.getStr("appDate"));
            patentLogEntity.setStatus(0);
            patentLogEntityList.add(patentLogEntity);
        });
        patentLogService.saveBatch(patentLogEntityList);
        page.setRecords(patentLogEntityList);
        return page;
    }


    /**
     * 获取授权信息，并将access_token和open_id缓存到Redis，同时设置refresh_token的有效期为原超时的80%
     *
     * @return 包含access_token和open_id的Map
     * @throws BizException 如果授权失败
     */
    public Map<String, String> getAuth() throws BizException {
        String cacheKey = "cnipr:" + cnirpConfig.getClientId();

        // 尝试从Redis中获取缓存的access_token和open_id
        String cachedAccessToken = stringRedisTemplate.opsForValue().get(cacheKey + ":access_token");
        String cachedOpenId = stringRedisTemplate.opsForValue().get(cacheKey + ":open_id");

        if (StrUtil.isNotBlank(cachedAccessToken) && StrUtil.isNotBlank(cachedOpenId)) {
            // 如果缓存存在，直接返回缓存中的数据
            Map<String, String> result = new HashMap<>();
            result.put("access_token", cachedAccessToken);
            result.put("open_id", cachedOpenId);
            return result;
        }

        // 如果缓存不存在，执行API请求获取新的授权信息
        Map<String, Object> param = new HashMap<>();
        param.put("user_account", cnirpConfig.getUserAccount());
        param.put("user_password", cnirpConfig.getUserPassword());
        param.put("client_id", cnirpConfig.getClientId());
        param.put("client_secret", cnirpConfig.getClientSecret());
        param.put("return_refresh_token", "1");
        param.put("grant_type", "password");

        JSONObject json = this.doPost(CnirpApiConstants.LOGIN, param);
        // 获取Access Token 和 openId
        String accessToken = json.getStr("access_token");
        String openId = json.getStr("open_id");
        Long expiresIn = json.getLong("expires_in");

        // 获取80%的expires_in值，作为refresh_token的有效期
        long refreshTokenExpireTime = (long) (expiresIn * 0.8) / 1000;

        String refreshToken = json.getStr("refresh_token");

        if (StrUtil.isNotBlank(accessToken)
                && StrUtil.isNotBlank(openId)
                && StrUtil.isNotBlank(refreshToken)) {
            // 将获取到的access_token、open_id、refresh_token缓存到Redis
            stringRedisTemplate.opsForValue().set(cacheKey + ":access_token", accessToken, refreshTokenExpireTime, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(cacheKey + ":open_id", openId, refreshTokenExpireTime, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(cacheKey + ":refresh_token", refreshToken, refreshTokenExpireTime, TimeUnit.SECONDS);

            // 返回缓存的值
            Map<String, String> result = new HashMap<>();
            result.put("access_token", accessToken);
            result.put("open_id", openId);
            result.put("refresh_token", refreshToken);
            return result;
        } else {
            throw new BizException("接口调用失败，地址：{}，响应：{}", CnirpApiConstants.LOGIN, json.toString());
        }
    }


    /**
     * 带参数的post请求（使用 Hutool）
     *
     * @param url 请求地址
     * @param map 请求参数
     * @return 请求返回的响应体
     */
    private JSONObject doPost(String url, Map<String, Object> map) throws BizException {
        // 使用 Hutool HttpRequest 发起 POST 请求，自动构造 form 表单数据
        HttpResponse response = HttpRequest.post(url)
                .form(map)
                .timeout(20000)
                .execute();
        JSONObject result = JSONUtil.parseObj(response.body());
        Long status = result.getLong("status");
        if (status == 0) {
            return result;
        } else {
            throw new BizException("接口调用失败，地址：{}，响应：{}", url, result);
        }
    }
}
