package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.config.YtConfig;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.service.YtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoliang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class YtServiceImpl implements YtService {

    private final YtConfig ytConfig;

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Page<PatentLogEntity> page(String exp, String dbs, int option, String order, int from, int size, String displayCols, boolean highLight, boolean isDbAgg) {
        Map<String, Object> param = new HashMap<>();
        param.put("exp", exp);
        param.put("dbs", dbs);
        param.put("option", option);
        param.put("order", order);
        param.put("from", from);
        param.put("size", size);
        param.put("displayCols", displayCols);
        param.put("highLight", highLight);
        param.put("isDbAgg", isDbAgg);
        JSONObject patentResult = this.doPost(ytConfig.getUrl(), param);
        Page<PatentLogEntity> page = JSONUtil.toBean(patentResult.getJSONObject("data"), Page.class);
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
        page.setRecords(patentLogEntityList);
        return page;
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
                .body(JSONUtil.toJsonStr(map))
                .timeout(20000)
                .execute();
        JSONObject result = JSONUtil.parseObj(response.body());
        Long code = result.getLong("code");
        if (code == 0) {
            return result;
        } else {
            throw new BizException("接口调用失败，地址：{}，响应：{}", url, result);
        }
    }

}
