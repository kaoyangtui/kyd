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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowVariable;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import java.util.List;

/**
 * 流程条件信息数据对象
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlowVarContextHolder {

	private static FlowVariableService flowVariableService;
	private static final ThreadLocal<List<FlowVariable>> THREAD_LOCAL_FLOW_VAR_INFO = new TransmittableThreadLocal<>();

	@Autowired
	public FlowVarContextHolder(FlowVariableService flowVariableService) {
		FlowVarContextHolder.flowVariableService = flowVariableService;
	}

	/**
	 * 初始化流程条件信息数据对象
	 */
	public static List<FlowVariable> initFlowVar(String flowKey, Long flowInstId, List<FlowVariable> variables) {
		List<FlowVariable> exist = FlowVarContextHolder.getFlowVar();
		if (CollUtil.isNotEmpty(exist)) {
			Long sameFlowInstId = exist.get(0).getFlowInstId();
			if (sameFlowInstId.equals(flowInstId)) return exist;
		}
		// 初始化获取流程条件对象
		if (CollUtil.isEmpty(variables)) variables = flowVariableService.listFlowVariables(flowInstId);
		if (CollUtil.isEmpty(variables)) throw new ValidationException("流程KEY【" + flowKey + "】的流程条件信息不存在");
		FlowVarContextHolder.setFlowVar(variables);
		return variables;
	}

	/**
	 * TTL 设置流程条件信息数据对象<br/>
	 *
	 * @param flowVar 流程条件信息数据对象
	 */
	public static void setFlowVar(List<FlowVariable> flowVar) {
		THREAD_LOCAL_FLOW_VAR_INFO.set(flowVar);
	}

	/**
	 * 获取TTL中的元数据对象
	 *
	 * @return List
	 */
	public static List<FlowVariable> getFlowVar() {
		return THREAD_LOCAL_FLOW_VAR_INFO.get();
	}

	public static void clear() {
		THREAD_LOCAL_FLOW_VAR_INFO.remove();
	}

}
