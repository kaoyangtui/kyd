package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.config.YtConfig;
import com.pig4cloud.pigx.admin.constants.QuotaPeriodType;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.service.ApiQuotaService;
import com.pig4cloud.pigx.admin.service.NationalPatentDetailService;
import com.pig4cloud.pigx.admin.service.NationalPatentInfoService;
import com.pig4cloud.pigx.admin.service.YtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class YtServiceImpl implements YtService {

    private final YtConfig ytConfig;
    private final ApiQuotaService apiQuotaService;

    private static final String API_CODE_SF1V1 = "YT_SF1V1";

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Page<PatentLogEntity> page(String exp, String dbs, int option, String order, int from, int size, String displayCols, boolean highLight, boolean isDbAgg) {
        Long userId = getCurrentUserId();

        apiQuotaService.checkLimitOrThrow(API_CODE_SF1V1, userId, QuotaPeriodType.TOTAL);

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

        JSONObject patentResult = this.doPost(ytConfig.getSf1V1Url(), param);

        Page<JSONObject> jsonPage = JSONUtil.toBean(patentResult.getJSONObject("data"), Page.class);
        List<JSONObject> rawRecords = jsonPage.getRecords() != null ? jsonPage.getRecords() : Collections.emptyList();

        List<PatentLogEntity> patentLogList = new ArrayList<>(rawRecords.size());
        for (JSONObject obj : rawRecords) {
            PatentLogEntity logEntity = JSONUtil.toBean(obj, PatentLogEntity.class);
            patentLogList.add(logEntity);
        }

        // 按“实际返回条数”扣额（不落库）
        int actualCount = patentLogList.size();
        if (actualCount > 0) {
            apiQuotaService.consumeOrThrow(API_CODE_SF1V1, userId, QuotaPeriodType.TOTAL, actualCount);
        }

        Page<PatentLogEntity> page = new Page<>();
        page.setCurrent(jsonPage.getCurrent());
        page.setSize(jsonPage.getSize());
        page.setTotal(jsonPage.getTotal());
        page.setRecords(patentLogList);
        return page;
    }


    @SneakyThrows
    @Override
    public String imgUrl(String pid, String resourceName) {
        Map<String, Object> param = new HashMap<>();
        param.put("pid", pid);
        param.put("resourceName", resourceName);
        JSONObject patentResult = this.doGet(ytConfig.getPi16Url(), param);
        return patentResult.getStr("data");
    }

    @SneakyThrows
    @Override
    public String absUrl(String pid) {
        Map<String, Object> param = new HashMap<>();
        param.put("pid", pid);
        JSONObject patentResult = this.doGet(ytConfig.getPi12Url(), param);
        return patentResult.getStr("data");
    }

    @SneakyThrows
    @Override
    public String pdfUrl(String pid) {
        Map<String, Object> param = new HashMap<>();
        param.put("pid", pid);
        JSONObject patentResult = this.doGet(ytConfig.getPi11Url(), param);
        return patentResult.getStr("data");
    }

    private JSONObject doPost(String url, Map<String, Object> map) throws BizException {
        HttpResponse response = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(map))
                .timeout(20000)
                .execute();
        JSONObject result = JSONUtil.parseObj(response.body());
        Long code = result.getLong("code");
        if (code != null && code == 0) {
            return result;
        } else {
            throw new BizException("接口调用失败，地址：{}，响应：{}", url, result);
        }
    }

    private JSONObject doGet(String url, Map<String, Object> params) throws BizException {
        try (HttpResponse response = HttpRequest.get(url)
                .form(params)
                .execute()) {
            JSONObject result = JSONUtil.parseObj(response.body());
            Long status = result.getLong("code");
            if (status != null && status == 0) {
                return result;
            } else {
                throw new BizException("接口调用失败，地址：{}，响应：{}", url, result);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private Long getCurrentUserId() {
        return null;
    }
}
