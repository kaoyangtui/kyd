package com.pig4cloud.pigx.jsonflow.service.impl;

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
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunReject;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunRejectVO;
import com.pig4cloud.pigx.jsonflow.engine.IAuditorService;
import com.pig4cloud.pigx.jsonflow.mapper.RunRejectMapper;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunRejectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 驳回记录
 *
 * @author luolin
 * @date 2021-03-05 11:56:29
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class RunRejectServiceImpl extends ServiceImpl<RunRejectMapper, RunReject> implements RunRejectService {

	private final RunJobService runJobService;

	private final IAuditorService auditorService;

	@Override
	public List<RunJob> reject(RunJobVO runJobVO) {
		RunRejectVO runRejectVO = runJobVO.getRunRejectVO();
		Long flowInstId = runJobVO.getFlowInstId();
		// 节点信息
		runRejectVO.setFromRunJobId(runJobVO.getId());
		runRejectVO.setFromRunNodeId(runJobVO.getRunNodeId());
		runRejectVO.setFromFlowNodeId(runJobVO.getFlowNodeId());
		if (runRejectVO.getToFlowNodeId().equals(runRejectVO.getFromFlowNodeId())) throw new ValidationException("不能驳回到自己");
		Long handleUserId = runRejectVO.getHandleUserId();
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runRejectVO.getToRunNodeId()));
		runJobs = this.selectedRunJobs(runJobs, runRejectVO.getNodeType());
		String userIds = this.selectedSysUsers(runJobs);
		// 判断是否选择了审批人
		if (Objects.nonNull(handleUserId)) {
			if (StrUtil.isNotBlank(userIds)) runJobs = this.handleNoneMatch(runJobs, handleUserId, "不存在当前选择驳回节点的审批人!可选择有【" + userIds + "】");
			// TODO 这里目前无用
			else runJobs.forEach(each -> {
				each.setJobType(JobUserTypeEnum.USER.getType());
				each.setRoleId(handleUserId);
			});
		} else {
			// 判断被驳回任务是否存在审批人
			runJobs = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId())).collect(Collectors.toList());
			if (CollUtil.isEmpty(runJobs)) throw new ValidationException("当被驳回节点下任务不存在审批人时，请指定被驳回人即为其分配参与者");
		}
		runRejectVO.setFlowInstId(flowInstId);
		runRejectVO.setDefFlowId(runJobVO.getDefFlowId());
		// 可能一人多任务或节点下多任务
		runJobs.forEach(each -> {
			if (Objects.isNull(handleUserId)) runRejectVO.setHandleUserId(each.getUserId());
			runRejectVO.setToRunJobId(each.getId());
			this.doSaveOrUpdate(runJobVO, runRejectVO);
		});
		return runJobs;
	}

	/**
	 * 可选择运行任务列表
	 *
	 * @param runJobs 运行任务集合
	 */
	private List<RunJob> selectedRunJobs(List<RunJob> runJobs, String nodeType) {
		// 判断驳回到的任务不满足条件已被跳过或未被审批过
		runJobs = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus()) && !FlowCommonConstants.YES.equals(f.getIsSkipRejected())
				&& Objects.nonNull(f.getUserId()) && Objects.nonNull(f.getRoleId())
				&& (Objects.nonNull(f.getStartTime()) || nodeType.equals(NodeTypeEnum.VIRTUAL.getType()))).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("当前选择驳回节点的任务已被跳过或未被审批过");
		return runJobs;
	}

	/**
	 * 可选择审批人列表
	 *
	 * @param runJobs     运行任务集合
	 */
	private String selectedSysUsers(List<RunJob> runJobs) {
		List<Long> userIds = runJobs.stream().map(RunJob::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
		if (CollUtil.isEmpty(userIds)) return StrUtil.EMPTY;
		List<SysUser> sysUsers = auditorService.listUsersByUserIds(userIds);
		Object[] array = sysUsers.stream().map(SysUser::getName).toArray();
		return ArrayUtil.join(array, StrUtil.COMMA);
	}

	/**
	 * 判断当前选择的审批人是否存在任务
	 *
	 * @param runJobs      运行任务集合
	 * @param handleUserId 处理人
	 */
	public List<RunJob> handleNoneMatch(List<RunJob> runJobs, Long handleUserId, String errMsg) {
		runJobs = runJobs.stream().filter(any -> any.getUserId().equals(handleUserId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException(errMsg);
		return runJobs;
	}

	/**
	 * 可能一人多任务或节点下多任务，保存全部记录
	 *
	 * @param runJobVO    运行任务
	 * @param runRejectVO 运行驳回
	 */
	private void doSaveOrUpdate(RunJobVO runJobVO, RunRejectVO runRejectVO) {
		// 判断是否已经存在，可以是单任务到多任务
		RunReject runReject = new RunReject();
		BeanUtil.copyProperties(runRejectVO, runReject);
		RunReject one = this.getOne(Wrappers.<RunReject>lambdaQuery().eq(RunReject::getFromRunJobId, runJobVO.getId())
				.eq(RunReject::getToRunJobId, runRejectVO.getToRunJobId()));
		if (Objects.nonNull(one)) runReject.setId(one.getId());
		// 记录节点-任务信息
		runReject.setStatus(NodeJobStatusEnum.RUN.getStatus());
		runReject.setFlowKey(runJobVO.getFlowKey());
		Long userId = SecurityUtils.getUser().getId();
		runReject.setCreateUser(userId);
		runReject.setUserId(userId);
		this.saveOrUpdate(runReject);
	}

	@Override
	public List<RunReject> handleRejectNodes(List<Long> runNodeIds, List<Long> runJobIds, boolean isQuery) {
		List<RunReject> runRejects = new ArrayList<>();
		if (isQuery) {
			runRejects = this.list(Wrappers.<RunReject>lambdaQuery()
					.in(CollUtil.isNotEmpty(runNodeIds), RunReject::getToRunNodeId, runNodeIds)
					.in(CollUtil.isNotEmpty(runJobIds), RunReject::getToRunJobId, runJobIds)
					.eq(RunReject::getStatus, NodeJobStatusEnum.REJ_RUN.getStatus()));
		}
		// 同时更新多个驳回的节点
		this.lambdaUpdate().set(RunReject::getStatus, NodeJobStatusEnum.REJ_COMP.getStatus())
				.in(CollUtil.isNotEmpty(runNodeIds), RunReject::getToRunNodeId, runNodeIds)
				.in(CollUtil.isNotEmpty(runJobIds), RunReject::getToRunJobId, runJobIds)
				.update();
		return runRejects;
	}

	@Override
	public void doHandleJobHandover(Long handoverUser, Long receiveUser, List<RunJob> runJobs) {
		List<Long> runJobIds = runJobs.stream().map(RunJob::getId).collect(Collectors.toList());
		List<Long> flowInstIds = runJobs.stream().map(RunJob::getFlowInstId).collect(Collectors.toList());
		// 任务交接时，更新被驳回任务
		this.lambdaUpdate().set(RunReject::getHandleUserId, receiveUser)
				.eq(RunReject::getHandleUserId, handoverUser)
				.in(CollUtil.isNotEmpty(flowInstIds), RunReject::getFlowInstId, flowInstIds)
				.in(CollUtil.isNotEmpty(runJobIds), RunReject::getToRunJobId, runJobIds)
				.eq(RunReject::getStatus, NodeJobStatusEnum.REJ_RUN.getStatus())
				.update();
	}

}
