package com.pig4cloud.pigx.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.config.CnirpConfig;
import com.pig4cloud.pigx.constants.CnirpApiConstants;
import com.pig4cloud.pigx.constants.CnirpPatentInfoConstants;
import com.pig4cloud.pigx.entity.PatentInfoEntity;
import com.pig4cloud.pigx.exception.BizException;
import com.pig4cloud.pigx.service.CniprService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoliang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CniprServiceImpl implements CniprService {

    private final StringRedisTemplate stringRedisTemplate;
    private final CnirpConfig config;

    @Override
    public Page<PatentInfoEntity> page(int from, int size) {
        String college = "'昆明医科大学' or '昆明医科大学第一附属医院' or '昆明医科大学第二附属医院' or '昆明医科大学第三附属医院'";
        String exp = StrUtil.format("专利权人=({}) or 历史专利权人=({}) or 申请（专利权）人=({})", college, college, college);
        String dbs = "FMZL,FMSQ,SYXX,WGZL";
        //检索类型，默认值：2 （按字检索）其它值含义见附录：https://open.cnipr.com/oauth/doc/appendix#.option
        int option = 2;
        String order = "+appDate";
        String displayCols = CnirpPatentInfoConstants.ALL_FIELDS;
        boolean highLight = false;
        boolean isDbAgg = false;
        return this.page(exp, dbs, option, order, from, size, displayCols, highLight, isDbAgg);
    }

    @Override
    @SneakyThrows
    public Page<PatentInfoEntity> page(String exp, String dbs, int option, String order, int from, int size, String displayCols, boolean highLight, boolean isDbAgg) {
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
        JSONObject patentResult = this.doPost(CnirpApiConstants.GET_PATENT_LIST, param);
        Page<PatentInfoEntity> page = new Page<>();
        page.setTotal(MapUtil.getLong(patentResult, "total"));
        String results = patentResult.getStr("results");
        List<PatentInfoEntity> patentList = JSONUtil.toList(JSONUtil.parseArray(results), PatentInfoEntity.class);
        page.setRecords(patentList);
        return page;
    }


    /**
     * 获取授权信息，并将access_token和open_id缓存到Redis，同时设置refresh_token的有效期为原超时的80%
     *
     * @return 包含access_token和open_id的Map
     * @throws BizException 如果授权失败
     */
    public Map<String, String> getAuth() throws BizException {
        String cacheKey = "cnirp:" + config.getClientId();

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
        param.put("user_account", config.getUserAccount());
        param.put("user_password", config.getUserPassword());
        param.put("client_id", config.getClientId());
        param.put("client_secret", config.getClientSecret());
        param.put("return_refresh_token", "1");
        param.put("grant_type", "password");

        JSONObject json = this.doPost(CnirpApiConstants.LOGIN, param);
        // 获取Access Token 和 openId
        String accessToken = json.getStr("access_token");
        String openId = json.getStr("open_id");
        Long expiresIn = json.getLong("expires_in");

        // 获取80%的expires_in值，作为refresh_token的有效期
        long refreshTokenExpireTime = (long) (expiresIn * 0.8);

        String refreshToken = json.getStr("refresh_token");

        if (StrUtil.isNotBlank(accessToken)
                && StrUtil.isNotBlank(openId)
                && StrUtil.isNotBlank(refreshToken)) {
            // 将获取到的access_token、open_id、refresh_token缓存到Redis
            stringRedisTemplate.opsForValue().set(cacheKey + ":access_token", accessToken, expiresIn, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(cacheKey + ":open_id", openId, expiresIn, TimeUnit.SECONDS);
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
