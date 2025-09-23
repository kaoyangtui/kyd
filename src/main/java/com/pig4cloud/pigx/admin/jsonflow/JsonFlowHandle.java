package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.map.MapUtil;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class JsonFlowHandle {
    private final RunFlowService runFlowService;

    public void startFlow(Map<String, Object> map, String orderName) {
        startFlow(map, orderName, null);
    }
    @SneakyThrows
    public void startFlow(Map<String, Object> map, String orderName, Map<String, Object> orderParams) {
        // 必填字段校验
        Object id = requireParam(map, "id");
        Object flowInstId = requireParam(map, "flowInstId");
        Object flowKey = requireParam(map, "flowKey");
        Object code = requireParam(map, "code");

        // 发起流程
        Map<String, Object> order = MapUtil.newHashMap();
        order.put("id", id);
        order.put("code", code);
        order.put("flowInstId", flowInstId);
        order.put("flowKey", flowKey);
        order.put("orderName", orderName);
        if (orderParams != null) {
            order.putAll(orderParams);
        }
        Map<String, Object> params = MapUtil.newHashMap();
        params.put("flowInstId", flowInstId);
        params.put("code", code);
        params.put("flowKey", flowKey);
        if (orderParams != null) {
            params.putAll(orderParams);
        }
        Boolean bl = runFlowService.startFlow(order, params);
        if (!bl) {
            throw new BizException("流程启动失败");
        }
    }

    /**
     * 获取必填参数，不存在直接抛异常
     */
    private Object requireParam(Map<String, Object> map, String key) throws BizException {
        Object value = map.get(key);
        if (Objects.isNull(value)) {
            throw new BizException("缺少必填参数: " + key);
        }
        return value;
    }
}
