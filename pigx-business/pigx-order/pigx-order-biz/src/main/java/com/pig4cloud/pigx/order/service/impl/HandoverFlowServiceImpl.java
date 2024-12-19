package com.pig4cloud.pigx.order.service.impl;
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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverReasonTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.feign.RemoteRunJobService;
import com.pig4cloud.pigx.order.api.constant.FlowVariableConstants;
import com.pig4cloud.pigx.order.api.constant.OrderCommonConstants;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import com.pig4cloud.pigx.order.api.entity.HandoverFlow;
import com.pig4cloud.pigx.order.api.entity.HandoverNodeRecord;
import com.pig4cloud.pigx.order.api.vo.HandoverFlowVO;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import com.pig4cloud.pigx.order.mapper.HandoverFlowMapper;
import com.pig4cloud.pigx.order.service.HandoverFlowService;
import com.pig4cloud.pigx.order.service.HandoverNodeRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 交接流程
 *
 * @author luolin
 * @date 2020-04-26 10:35:55
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HandoverFlowServiceImpl extends OrderCommonServiceImpl<HandoverFlowMapper, HandoverFlow> implements HandoverFlowService {

	private final HandoverNodeRecordService handoverNodeRecordService;

	private final RemoteRunJobService remoteRunJobService;

	@Override
	// @GlobalTransactional
	public Boolean saveOrUpdate(HandoverFlowVO handoverFlowVO) {
		handoverFlowVO.setCreateUser(SecurityUtils.getUser().getId());

		HandoverFlow handoverFlow = new HandoverFlow();
		BeanUtil.copyProperties(handoverFlowVO, handoverFlow);
		// 设置流程参数
		Map<String, Object> params = MapUtil.newHashMap();
		this.buildFlowVariables(params, handoverFlowVO, handoverFlow);
		// TODO 是否异步更新附表状态到附表中
		this.updateNodeWorkRecord(handoverFlowVO);
		// 发起流程并保存工单
		super.saveOrUpdateOrder(params, handoverFlow);
		super.updateOrderVOInfo(handoverFlow, handoverFlowVO);
		// 启动流程公共接口
		this.startFlow(params, handoverFlowVO, null);
		return true;
	}

	/**
	 * 更新附表状态
	 *
	 * @param handoverFlowVO 交接流程
	 */
	private void updateNodeWorkRecord(HandoverFlowVO handoverFlowVO) {
		String type = handoverFlowVO.getType();
		if (HandoverFlowTypeEnum.NODE.getType().equals(type)) {
			handoverNodeRecordService.updateByOrderId(handoverFlowVO.getId(), HandoverFlowStatusEnum.RUN.getStatus());
		}
	}

	@Override
	public Boolean tempStore(HandoverFlowVO handoverFlowVO) {
		handoverFlowVO.setCreateUser(SecurityUtils.getUser().getId());

		HandoverFlow handoverFlow = new HandoverFlow();
		BeanUtil.copyProperties(handoverFlowVO, handoverFlow);
		return super.tempStore(handoverFlow);
	}

	@Override
	// @GlobalTransactional
	public Boolean updateOrder(HandoverFlowVO handoverFlowVO) {
		handoverFlowVO.setUpdateUser(SecurityUtils.getUser().getId());
		handoverFlowVO.setUpdateTime(LocalDateTime.now());

		HandoverFlow handoverFlow = new HandoverFlow();
		BeanUtil.copyProperties(handoverFlowVO, handoverFlow);
		boolean save = this.updateById(handoverFlow);
		// TODO 自己决定更新时是否更新流程参数
		String runJobId = handoverFlowVO.getRunJobId();
		if (StrUtil.isEmpty(runJobId)) return true;
		Map<String, Object> params = MapUtil.of(OrderCommonConstants.RUN_JOB_ID, runJobId);
		this.buildFlowVariables(params, handoverFlowVO, handoverFlow);
		super.updateFlowVariables(params, handoverFlowVO);
		return save;
	}

	/**
	 * 构建流程参数
	 *
	 * @param params 参数
	 * @param handoverFlow 交接流程
	 */
	private void buildFlowVariables(Map<String, Object> params, HandoverFlowVO handoverFlowVO, HandoverFlow handoverFlow) {
		Long receiveUser = handoverFlowVO.getReceiveUser();
		if (Objects.isNull(receiveUser)) throw new ValidationException("接收人不能为空");
		// 构造流程人员参数
		DistPerson distPersonUser = this.buildDistPerson(receiveUser);
		params.put(FlowCommonConstants.FLOW_USERS, JSONUtil.toJsonStr(distPersonUser));
		// 设置默认值
		params.put(FlowVariableConstants.IS_NEED_RECEIVE, FlowCommonConstants.NO);
		// 设置交接人信息，排除他人修改
		if (Objects.isNull(handoverFlow.getHandoverUser())) {
			handoverFlow.setHandoverUser(SecurityUtils.getUser().getId());
			handoverFlow.setHandoverUserDept(SecurityUtils.getUser().getDeptId());
		}
		// 判断是否存在交接数据，存在则必须有人来接收
		this.handleExistHandoverData(params, handoverFlowVO);
		// 构建流程公共参数
		super.buildFlowVariables(params, handoverFlowVO);
	}

	private DistPerson buildDistPerson(Long receiveUser) {
		// 设置接收人
		DistPerson distPersonUser = new DistPerson();
		distPersonUser.setRoleId(receiveUser);
		distPersonUser.setJobType(JobUserTypeEnum.USER.getType());
		distPersonUser.setUserKey(FlowVariableConstants.RECEIVE_USER);
		return distPersonUser;
	}

	/**
	 * 判断是否存在交接数据，存在则一定需要人来接收
	 *
	 * @param params 参数
	 * @param handoverFlowVO 交接流程
	 */
	private void handleExistHandoverData(Map<String, Object> params, HandoverFlowVO handoverFlowVO) {
		String type = handoverFlowVO.getType();
		boolean exist = false;
		// 查询判断是否存在交接数据，因可能前端会有修改
		if (HandoverFlowTypeEnum.NODE.getType().equals(type)) {
			long count = handoverNodeRecordService.count(Wrappers.<HandoverNodeRecord>lambdaQuery()
					.eq(HandoverNodeRecord::getOrderId, handoverFlowVO.getId()));
			if (count > 0) exist = true;
		}
		String handoverReason = handoverFlowVO.getHandoverReason();
		if (HandoverReasonTypeEnum.CHANGE_POST.getType().equals(handoverReason) || HandoverReasonTypeEnum.LEAVE_OFFICE.getType().equals(handoverReason)) {
			params.put(FlowVariableConstants.IS_NEED_RECEIVE, FlowCommonConstants.YES);
		}
		if (!exist) {
			return;// 无数据则无需接收
		}
		params.put(FlowVariableConstants.IS_NEED_RECEIVE, FlowCommonConstants.YES);
	}

	@Override
	public Boolean removeOrder(Long id) {
		handoverNodeRecordService.remove(Wrappers.<HandoverNodeRecord>lambdaQuery().eq(HandoverNodeRecord::getOrderId, id));
		return this.removeById(id);
	}

	@Override
	public Boolean removeOrder(List<Long> ids) {
		handoverNodeRecordService.remove(Wrappers.<HandoverNodeRecord>lambdaQuery().in(HandoverNodeRecord::getOrderId, ids));
		return this.removeByIds(ids);
	}

	@Override
	// @GlobalTransactional
	public Boolean distributePerson(HandoverFlowVO handoverFlowVO) {
		Long receiveUser = handoverFlowVO.getReceiveUser();
		if (Objects.isNull(receiveUser)) throw new ValidationException("接收人不能为空");
		Long receiveDept = handoverFlowVO.getReceiveDept();
		boolean save = this.lambdaUpdate().set(Objects.nonNull(receiveDept), HandoverFlow::getReceiveDept, receiveDept)
				.set(HandoverFlow::getReceiveUser, receiveUser)
				.eq(HandoverFlow::getFlowInstId, handoverFlowVO.getFlowInstId())
				.update();
		// 更新接收人
		Map<String, Object> params = MapUtil.of(OrderCommonConstants.RUN_JOB_ID, handoverFlowVO.getRunJobId());
		// 构造流程人员参数
		DistPerson distPersonUser = this.buildDistPerson(receiveUser);
		params.put(FlowCommonConstants.FLOW_USERS, JSONUtil.toJsonStr(distPersonUser));
		super.updateFlowVariables(params, handoverFlowVO);
		return save;
	}

	@Override
	public Boolean checkToReject(HandoverFlowVO handoverFlowVO) {
		List<Long> rejectIds = handoverFlowVO.getRejectIds();
		String type = handoverFlowVO.getType();
		if (HandoverFlowTypeEnum.NODE.getType().equals(type)) {
			handoverNodeRecordService.lambdaUpdate().set(HandoverNodeRecord::getRetStatus, FlowCommonConstants.YES)
					.eq(HandoverNodeRecord::getOrderId, handoverFlowVO.getId())
					.in(HandoverNodeRecord::getId, rejectIds)
					.update();
		}
		return Boolean.TRUE;
	}

	@Override
	// @GlobalTransactional
	public Boolean confirmReceive(HandoverFlowVO handoverFlowVO) {
		HandoverFlow handoverFlow = this.getById(handoverFlowVO.getId());
		String type = handoverFlow.getType();
		String handoverReason = handoverFlow.getHandoverReason();
		List<RunJob> runJobs = Collections.emptyList();
		// 交接流程的业务或任务
		if (HandoverFlowTypeEnum.NODE.getType().equals(type)) {
			List<HandoverNodeRecord> nodeRecords = handoverNodeRecordService.list(Wrappers.<HandoverNodeRecord>lambdaQuery()
					.eq(HandoverNodeRecord::getOrderId, handoverFlow.getId()));
			if (CollUtil.isEmpty(nodeRecords)) throw new ValidationException("接收任务不能为空");
			runJobs = nodeRecords.stream().map(m -> {
				RunJob runJob = new RunJob();
				BeanUtil.copyProperties(m, runJob);
				runJob.setId(m.getRunJobId());
				return runJob;
			}).collect(Collectors.toList());
		}
		// 只要是离职都需要更新
		R<Boolean> res = remoteRunJobService.handleJobHandover(runJobs, handoverFlow.getHandoverUser(), handoverReason, type);
		if (!FlowCommonConstants.SUCCESS.equals(res.getCode())) {
			throw new ValidationException("更新流程办理人失败: " + res.getMsg());
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean updateRecordStatus(Map<String, Object> order) {
		String type = MapUtil.getStr(order, OrderEntityInfoConstants.TYPE);
		String status = MapUtil.getStr(order, OrderEntityInfoConstants.STATUS);
		String id = MapUtil.getStr(order, OrderEntityInfoConstants.ID);
		if (HandoverFlowTypeEnum.NODE.getType().equals(type)) {
			handoverNodeRecordService.updateByOrderId(Long.valueOf(id), status);
		}
		return Boolean.TRUE;
	}

}
