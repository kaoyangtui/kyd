package com.pig4cloud.pigx.jsonflow.util;

/*
 *      Copyright (c) 2018-2025, luolin All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: luolin (766488893@qq.com)
 */

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.config.FlowCommonProperties;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.support.OrderInfoContextHolder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 流程关联表单Http接口调用工具类封装
 *
 * @author luolin
 * @date 2020/2/5 22:31
 */
@Slf4j
@UtilityClass
public class FlowFormHttpInvokeUtils {

	private final FlowRuleService flowRuleService = SpringContextHolder.getBean(FlowRuleService.class);
	private final RunFlowService runFlowService = SpringContextHolder.getBean(RunFlowService.class);
	private final FlowCommonProperties flowCommonProperties = SpringContextHolder.getBean(FlowCommonProperties.class);

	/**
	 * 构建流程信息
	 */
	public Map<String, Object> buildFlowParams(Long flowInstId, String flowKey) {
		Map<String, Object> params = MapUtil.of(FlowEntityInfoConstants.FLOW_INST_ID, flowInstId);
		params.put(FlowEntityInfoConstants.FLOW_KEY, flowKey);
		return params;
	}

	/**
	 * 根据流程ID获取工单数据
	 *
	 * @param flowInstId 流程实例ID
	 */
	public Object getOrderByFlowInstId(Long flowInstId) {
		RunFlow runFlow = runFlowService.getById(flowInstId);
		return FlowFormHttpInvokeUtils.getOrderByFlowInstId(runFlow);
	}

	/**
	 * 根据流程ID获取工单数据
	 *
	 * @param runFlow 流程实例
	 */
	public Object getOrderByFlowInstId(RunFlow runFlow) {
		String flowKey = runFlow.getFlowKey();
		Long defFlowId = runFlow.getDefFlowId();
		Long flowInstId = runFlow.getId();
		// 处理发起流程时事务问题
		DynaBean orderInfo = OrderInfoContextHolder.getOrderInfo(flowInstId);
		if (Objects.nonNull(orderInfo)) return orderInfo.getBean();
		FlowRuleVO flowRuleVO = getFlowRuleVO(FlowParamRuleEnum.ORDER_PARAMS.getType());
		Map<String, String> headers = CommonHttpUrlInvokeUtils.buildMapHeaders(flowKey, defFlowId, flowInstId, flowRuleVO);
		Map<String, Object> formMap = buildFlowParams(flowInstId, flowKey);
		// 默认请求地址
		String httpUrl = runFlow.getQueryOrder();
		String queryMethod = runFlow.getQueryMethod();
		if (StrUtil.isBlank(httpUrl)) {
			httpUrl = flowCommonProperties.getQueryOrder();
			queryMethod = Method.GET.name();
		}
		if (StrUtil.isBlank(httpUrl)) return null;
		flowRuleVO.setHttpUrl(httpUrl);
		flowRuleVO.setHttpMethod(queryMethod);
		return CommonHttpUrlInvokeUtils.doHttpInvoke(flowRuleVO, headers, formMap, null);
	}

	/**
	 * 根据流程ID更新工单信息
	 *
	 * @param order  工单参数
	 * @param flowInstId 流程实例ID
	 */
	public void updateOrderInfo(Map<String, Object> order, Long flowInstId, Map<String, Object> formMap) {
		RunFlow runFlow = runFlowService.getById(flowInstId);
		FlowFormHttpInvokeUtils.updateOrderInfo(order, runFlow, formMap);
	}

	/**
	 * 根据流程ID更新工单信息
	 *
	 * @param order  工单参数
	 * @param runFlow 流程实例
	 */
	public void updateOrderInfo(Map<String, Object> order, RunFlow runFlow) {
		String flowKey = runFlow.getFlowKey();
		Long flowInstId = runFlow.getId();
		Map<String, Object> formMap = buildFlowParams(flowInstId, flowKey);
		FlowFormHttpInvokeUtils.updateOrderInfo(order, runFlow, formMap);
	}

	public void updateOrderInfo(Map<String, Object> order, RunFlow runFlow, Map<String, Object> formMap) {
		String flowKey = runFlow.getFlowKey();
		Long defFlowId = runFlow.getDefFlowId();
		Long flowInstId = runFlow.getId();
		FlowRuleVO flowRuleVO = getFlowRuleVO(FlowParamRuleEnum.ORDER_PARAMS.getType());
		Map<String, String> headers = CommonHttpUrlInvokeUtils.buildMapHeaders(flowKey, defFlowId, flowInstId, flowRuleVO);
		// 默认请求地址
		String httpUrl = runFlow.getUpdateOrder();
		String updateMethod = runFlow.getUpdateMethod();
		if (StrUtil.isBlank(httpUrl)) {
			httpUrl = flowCommonProperties.getUpdateOrder();
			updateMethod = Method.PUT.name();
		}
		if (StrUtil.isBlank(httpUrl)) return;
		flowRuleVO.setHttpUrl(httpUrl);
		flowRuleVO.setHttpMethod(updateMethod);
		CommonHttpUrlInvokeUtils.doHttpInvoke(flowRuleVO, headers, formMap, order);
	}

	private FlowRuleVO getFlowRuleVO(String type) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		flowRuleVO.setType(type);
		return flowRuleVO;
	}

	/**
	 * 启动子流程时保存子工单接口
	 *
	 * @param order  工单参数
	 * @param formMap 参数
	 * @param runNodeVO 运行节点
	 */
	public Object startSubFlow(Map<String, Object> order, Map<String, Object> formMap, RunNodeVO runNodeVO) {
		return FlowFormHttpInvokeUtils.saveOrUpdateFlowForm(order, formMap, runNodeVO, FlowParamRuleEnum.START_SUB_FLOW.getType(),
				flowCommonProperties.getStartSubFlow(), Method.POST.name());
	}

	/**
	 * 返回父流程时更新父工单接口
	 *
	 * @param order  工单参数
	 * @param formMap 参数
	 * @param runNodeVO 运行节点
	 */
	public Boolean backParFlow(Map<String, Object> order, Map<String, Object> formMap, RunNodeVO runNodeVO) {
		FlowFormHttpInvokeUtils.saveOrUpdateFlowForm(order, formMap, runNodeVO, FlowParamRuleEnum.BACK_PAR_FLOW.getType(),
				flowCommonProperties.getBackParFlow(), Method.PUT.name());
		return Boolean.TRUE;
	}

	/**
	 * 重启子流程时更新子工单接口
	 *
	 * @param order  工单参数
	 * @param formMap 参数
	 * @param runNodeVO 运行节点
	 */
	public Boolean restartSubFlow(Map<String, Object> order, Map<String, Object> formMap, RunNodeVO runNodeVO) {
		FlowFormHttpInvokeUtils.saveOrUpdateFlowForm(order, formMap, runNodeVO, FlowParamRuleEnum.RESTART_SUB_FLOW.getType(),
				flowCommonProperties.getRestartSubFlow(), Method.PUT.name());
		return Boolean.TRUE;
	}

	private Object saveOrUpdateFlowForm(Map<String, Object> order, Map<String, Object> formMap, RunNodeVO runNodeVO, String type, String httpUrl, String httpMethod) {
		Long defFlowId = runNodeVO.getDefFlowId();
		Long flowNodeId = runNodeVO.getFlowNodeId();
		Long flowInstId = runNodeVO.getFlowInstId();
		String flowKey = runNodeVO.getFlowKey();
		FlowRuleVO flowRuleVO = getFlowRuleVO(type);
		flowRuleVO.setFlowNodeId(flowNodeId);
		Map<String, String> headers = CommonHttpUrlInvokeUtils.buildMapHeaders(flowKey, defFlowId, flowInstId, flowRuleVO);
		flowRuleVO.setParamFrom(null);
		List<FlowRule> flowRules = flowRuleService.listFlowRules(flowRuleVO);
		// 默认请求地址
		if (CollUtil.isNotEmpty(flowRules)) {
			httpUrl = flowRules.get(0).getHttpUrl();
			httpMethod = flowRules.get(0).getHttpMethod();
		}
		if (StrUtil.isBlank(httpUrl)) return null;
		flowRuleVO.setHttpUrl(httpUrl);
		flowRuleVO.setHttpMethod(httpMethod);
		return CommonHttpUrlInvokeUtils.doHttpInvoke(flowRuleVO, headers, formMap, order);
	}

	public Object listRunByRunJobIds(List<Long> ids){
		Map<String, Object> mapHeaders = new HashMap<>();
		Map<String, String> headers = CommonHttpUrlInvokeUtils.getHeaders(mapHeaders);
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setHttpUrl(flowCommonProperties.getListRunByRunJobIds());
		flowRuleVO.setHttpMethod(Method.POST.name());
		flowRuleVO.setParamType(CommonNbrPool.STR_0);
		return CommonHttpUrlInvokeUtils.doHttpInvoke(flowRuleVO, headers, null, ids);
	}

}
