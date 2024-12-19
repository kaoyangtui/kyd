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

import com.alibaba.ttl.TransmittableThreadLocal;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import java.util.Objects;

/**
 * 流程实例信息数据对象
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RunFlowContextHolder {

	private static RunFlowService runFlowService;
	private static final ThreadLocal<RunFlow> THREAD_LOCAL_FLOW_VAR_INFO = new TransmittableThreadLocal<>();

	@Autowired
	public RunFlowContextHolder(RunFlowService runFlowService) {
		RunFlowContextHolder.runFlowService = runFlowService;
	}

	/**
	 * 初始化流程实例信息数据对象
	 */
	public static RunFlow initRunFlow(String flowKey, Long flowInstId, RunFlow runFlow) {
		RunFlow exist = RunFlowContextHolder.getRunFlow();
		if (Objects.nonNull(exist)) {
			Long sameFlowInstId = exist.getId();
			if (sameFlowInstId.equals(flowInstId)) return exist;
		}
		// 初始化获取流程实例对象
		if (Objects.isNull(runFlow)) runFlow = runFlowService.getById(flowInstId);
		if (Objects.isNull(runFlow)) throw new ValidationException("流程KEY【" + flowKey + "】的流程实例信息不存在");
		RunFlowContextHolder.setRunFlow(runFlow);
		return runFlow;
	}

	/**
	 * 判断当前流程实例配置数据是否独立
	 * @param flowInstId 流程实例ID
	 * @return Long
	 */
	public static Long validateIndependent(Long flowInstId) {
		if (Objects.nonNull(flowInstId)) {
			RunFlow runFlow = RunFlowContextHolder.initRunFlowById(flowInstId, null);
			if (!FlowCommonConstants.YES.equals(runFlow.getIsIndependent())) {
				flowInstId = null;
			}
		}
		return flowInstId;
	}

	/**
	 * 初始化流程实例信息数据对象
	 */
	public static RunFlow initRunFlowById(Long flowInstId, RunFlow runFlow) {
		RunFlow exist = RunFlowContextHolder.getRunFlow();
		if (Objects.nonNull(exist)) {
			Long sameFlowInstId = exist.getId();
			if (sameFlowInstId.equals(flowInstId)) return exist;
		}
		// 初始化获取流程实例对象
		if (Objects.isNull(runFlow)) runFlow = runFlowService.getById(flowInstId);
		if (Objects.isNull(runFlow)) throw new ValidationException("流程实例ID【" + flowInstId + "】的流程实例信息不存在");
		RunFlowContextHolder.setRunFlow(runFlow);
		return runFlow;
	}

	/**
	 * TTL 设置流程实例信息数据对象<br/>
	 *
	 * @param runFlow 流程实例信息数据对象
	 */
	public static void setRunFlow(RunFlow runFlow) {
		THREAD_LOCAL_FLOW_VAR_INFO.set(runFlow);
	}

	/**
	 * 获取TTL中的元数据对象
	 *
	 * @return RunFlow
	 */
	public static RunFlow getRunFlow() {
		return THREAD_LOCAL_FLOW_VAR_INFO.get();
	}

	public static void clear() {
		THREAD_LOCAL_FLOW_VAR_INFO.remove();
	}

}
