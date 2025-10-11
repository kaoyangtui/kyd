package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class JsonFlowHandle {
    private final RunFlowService runFlowService;

    public <Vo> void doStart(Map<String, Object> params, Vo vo) {
        Map<String, Object> order = new HashMap<>();
        BeanUtil.copyProperties(vo, order);
        Long id = MapUtil.getLong(order, OrderEntityInfoConstants.ID);
        Long flowInstId = MapUtil.getLong(order, OrderEntityInfoConstants.FLOW_INST_ID);
        String flowKey = MapUtil.getStr(order, OrderEntityInfoConstants.FLOW_KEY);
        order.put("orderName", params.get("orderName"));
        Boolean res = runFlowService.startFlow(order, params);
        if (!res) {
            throw new ValidationException("发起流程失败: " + res);
        }
        log.info("工单 id:{},flowInstId:{},flowKey:{}", id, flowInstId, flowKey);
    }

}
