package com.pig4cloud.pigx.admin.jsonflow;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.order.OrderStatusEnum;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
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

    @SneakyThrows
    public void startFlow(T order, String orderName) {
        Map<String, Object> params = this.doSaveOrUpdate(order);
        params.put("orderName", orderName);
        // 启动流程公共接口
        this.doStart(params, order);
    }


    public <Vo> void doStart(Map<String, Object> params, Vo vo) {
        Map<String, Object> order = new HashMap<>();
        BeanUtil.copyProperties(vo, order);
        Long id = MapUtil.getLong(order, OrderEntityInfoConstants.ID);
        Long flowInstId = MapUtil.getLong(order, OrderEntityInfoConstants.FLOW_INST_ID);
        String flowKey = MapUtil.getStr(order, OrderEntityInfoConstants.FLOW_KEY);
        Boolean res = runFlowService.startFlow(order, params);
        if (!FlowCommonConstants.SUCCESS.equals(res)) {
            throw new ValidationException("发起流程失败: " + res);
        }
        log.info("工单 id:{},flowInstId:{},flowKey:{}", id, flowInstId, flowKey);
    }

    private Map<String, Object> doSaveOrUpdate(T order) throws BizException {
        // 设置流程参数
        Map<String, Object> params = MapUtil.newHashMap();
        // 发起流程并保存工单
        saveOrUpdateOrder(params, order);
        return params;
    }

    public Boolean saveOrUpdateOrder(Map<String, Object> params, T order) throws BizException {
        // 工单公共处理
        DynaBean bean = DynaBean.create(order);
        this.setOrderInfo(order, bean, false);
        // 构建流程参数
        this.buildFlowInfo(params, bean);
        return true;
    }

    public void setOrderInfo(T order, DynaBean bean, boolean isTemp) {
        // 业务id不存在，默认生成一个
        Long id = bean.get(OrderEntityInfoConstants.ID);
        if (ObjectUtil.isEmpty(id)) {
            bean.set(OrderEntityInfoConstants.ID, IdUtil.getSnowflakeNextIdStr());
        }
        // 判断是否存在code
        if (bean.containsProp(OrderEntityInfoConstants.CODE)) {
            // 业务code不存在，默认与id相同
            String code = bean.get(OrderEntityInfoConstants.CODE);
            if (StrUtil.isBlank(code)) {
                id = bean.get(OrderEntityInfoConstants.ID);
                bean.set(OrderEntityInfoConstants.CODE, id.toString());
            }
        }
        // 判断是否存在flowKey
        if (bean.containsProp(OrderEntityInfoConstants.FLOW_KEY)) {
            // 业务key不存在，默认为自身工单
            String flowKey = bean.get(OrderEntityInfoConstants.FLOW_KEY);
            if (StrUtil.isEmpty(flowKey)) {
                bean.set(OrderEntityInfoConstants.FLOW_KEY, order.getClass().getSimpleName());
            }
        }
        // 判断是否存在status
        if (bean.containsProp(OrderEntityInfoConstants.STATUS)) {
            bean.set(OrderEntityInfoConstants.STATUS, isTemp ? OrderStatusEnum.TEMP.getStatus() : OrderStatusEnum.RUN.getStatus());
        }
    }

    public Boolean buildFlowInfo(Map<String, Object> params, DynaBean bean) throws BizException {
        // 业务flowInstId不存在，默认生成一个
        Long flowInstId = bean.get(OrderEntityInfoConstants.FLOW_INST_ID);
        if (Objects.isNull(flowInstId)) {
            throw new BizException("流程实例ID不存在");
        }
        this.extractedCommonParams(params, bean);
        // 判断是否存在表单ID
        if (bean.containsProp(OrderEntityInfoConstants.FORM_ID)) {
            params.put(OrderEntityInfoConstants.FORM_ID, bean.get(OrderEntityInfoConstants.FORM_ID));
        }
        return true;
    }

    private void extractedCommonParams(Map<String, Object> params, DynaBean bean) {
        params.put(OrderEntityInfoConstants.FLOW_INST_ID, bean.get(OrderEntityInfoConstants.FLOW_INST_ID));
        // 判断是否存在code
        if (bean.containsProp(OrderEntityInfoConstants.CODE)) {
            params.put(OrderEntityInfoConstants.CODE, bean.get(OrderEntityInfoConstants.CODE));
        }
        // 判断是否存在flowKey
        if (bean.containsProp(OrderEntityInfoConstants.FLOW_KEY)) {
            params.put(OrderEntityInfoConstants.FLOW_KEY, bean.get(OrderEntityInfoConstants.FLOW_KEY));
        }
        // 判断是否存在创建人
        if (bean.containsProp(OrderEntityInfoConstants.CREATE_USER)) {
            params.put(OrderEntityInfoConstants.CREATE_USER, bean.get(OrderEntityInfoConstants.CREATE_USER));
        }
    }

}
