package com.pig4cloud.pigx.jsonflow.support;

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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.engine.AuditorServiceImpl;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.util.FlowFormHttpInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.SimpMsgTempUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 处理工单关联相关信息
 *
 * @author luolin
 * @date 2020/5/22 13:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HandleOrderRelativeInfo {

	private static AuditorServiceImpl auditorService;
	private static RunFlowService runFlowService;

	@Autowired
	public HandleOrderRelativeInfo(AuditorServiceImpl auditorService, RunFlowService runFlowService) {
		HandleOrderRelativeInfo.auditorService = auditorService;
		HandleOrderRelativeInfo.runFlowService = runFlowService;
	}

	/**
	 * 作废流程相关信息
	 *
	 * @param flowKey 业务KEY
	 * @param flowInstId  流程实例ID
	 */
	public static void handleOrderRelativeInfo(String flowKey, Long flowInstId, String status) {
		if (StrUtil.isEmpty(flowKey)) throw new ValidationException("业务KEY不存在");
		if (Objects.isNull(flowInstId)) throw new ValidationException("流程ID不存在");
		RunFlow runFlow = updateOrderStatus(flowInstId, status);
		// 钉钉通知发起人（排除自己作废）
		turnNodeNotifyDdUser(runFlow);
	}

	public static RunFlow updateOrderStatus(Long flowInstId, String status){
		RunFlow runFlow = runFlowService.getById(flowInstId);
		// 更新工单状态
		Map<String, Object> updateInfo = MapUtil.of(FlowEntityInfoConstants.STATUS, status);
		FlowFormHttpInvokeUtils.updateOrderInfo(updateInfo, runFlow);
		return runFlow;
	}

	/**
	 * 钉钉通知发起人（排除自己）
	 *
	 * @param runFlow 流程数据
	 */
	private static void turnNodeNotifyDdUser(RunFlow runFlow) {
		Long initiatorId = runFlow.getInitiatorId();
		if (SecurityUtils.getUser().getId().equals(initiatorId)) return;
		List<Long> userIds = CollUtil.newArrayList(initiatorId, SecurityUtils.getUser().getId());
		List<SysUser> ddUsers = auditorService.listUsersByUserIds(userIds);
		if (CollUtil.isEmpty(ddUsers)) return;
		SimpMsgTempUtils.turnNodeNotifyDdUser(runFlow, ddUsers, initiatorId);
	}

}
