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
package com.pig4cloud.pigx.jsonflow.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.util.FlowFormHttpInvokeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import java.util.Map;
import java.util.Objects;

/**
 * 表单信息数据对象
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderInfoContextHolder {

	private static final ThreadLocal<DynaBean> THREAD_LOCAL_ORDER_INFO = new TransmittableThreadLocal<>();
	private static final ThreadLocal<DynaBean> THREAD_LOCAL_FORM_DATA_INFO = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置工单信息数据对象<br/>
	 *
	 * @param orderInfo 工单信息数据对象
	 */
	public static void setOrderInfo(DynaBean orderInfo) {
		THREAD_LOCAL_ORDER_INFO.set(orderInfo);
	}

	/**
	 * 获取TTL中的工单信息对象
	 *
	 * @return DynaBean
	 */
	public static DynaBean getOrderInfo(Long flowInstId) {
		DynaBean exist = OrderInfoContextHolder.getOrderInfo();
		if (Objects.nonNull(exist)) {
			Object sameFlowInstId = exist.get(FlowEntityInfoConstants.FLOW_INST_ID);
			if (flowInstId.toString().equals(sameFlowInstId.toString())) return exist;
		}
		return null;
	}

	/**
	 * 获取TTL中的工单信息对象
	 *
	 * @return DynaBean
	 */
	public static DynaBean getOrderInfo() {
		return THREAD_LOCAL_ORDER_INFO.get();
	}

	/**
	 * 初始化表单信息数据对象
	 */
	public static DynaBean initOrderFormInfo(String flowKey, Long flowInstId, Object order, boolean isOrder) {
		DynaBean exist = isOrder ? OrderInfoContextHolder.getOrderInfo() : OrderInfoContextHolder.getFormInfo();
		if (Objects.nonNull(exist)) {
			Object sameFlowInstId = exist.get(FlowEntityInfoConstants.FLOW_INST_ID);
			if (flowInstId.toString().equals(sameFlowInstId.toString())) return exist;
		}
		// 初始化获取表单对象
		if (Objects.isNull(order)) order = FlowFormHttpInvokeUtils.getOrderByFlowInstId(flowInstId);
		if (Objects.isNull(order)) throw new ValidationException("流程KEY【" + flowKey + "】的表单信息不存在");
		Map<String, Object> resMap = BeanUtil.beanToMap(order);
		DynaBean orderInfo = DynaBean.create(resMap);
		OrderInfoContextHolder.setOrderInfo(orderInfo);
		Long formId = MapUtil.getLong(resMap, FlowEntityInfoConstants.FORM_ID);
		if (Objects.nonNull(formId)) {
			String formData = MapUtil.getStr(resMap, FlowEntityInfoConstants.FORM_DATA);
			if (StrUtil.isBlank(formData)) throw new ValidationException("流程KEY【" + flowKey + "】的表单信息不存在");
			resMap = JSONUtil.parseObj(formData);
			resMap.put(FlowEntityInfoConstants.FLOW_INST_ID, flowInstId);
		}
		DynaBean formInfo = DynaBean.create(resMap);
		OrderInfoContextHolder.setFormInfo(formInfo);
		return isOrder ? orderInfo : formInfo;
	}

	/**
	 * TTL 设置表单信息数据对象<br/>
	 *
	 * @param formInfo 表单信息数据对象
	 */
	public static void setFormInfo(DynaBean formInfo) {
		THREAD_LOCAL_FORM_DATA_INFO.set(formInfo);
	}

	/**
	 * 获取TTL中的表单信息对象
	 *
	 * @return DynaBean
	 */
	public static DynaBean getFormInfo() {
		return THREAD_LOCAL_FORM_DATA_INFO.get();
	}

	public static void clear() {
		THREAD_LOCAL_ORDER_INFO.remove();
		THREAD_LOCAL_FORM_DATA_INFO.remove();
	}

}
