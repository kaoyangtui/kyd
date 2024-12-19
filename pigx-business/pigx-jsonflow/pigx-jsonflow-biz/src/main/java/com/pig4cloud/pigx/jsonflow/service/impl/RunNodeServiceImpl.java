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
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeCircTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.TabsOptionEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunRejectVO;
import com.pig4cloud.pigx.jsonflow.engine.IAuditorService;
import com.pig4cloud.pigx.jsonflow.engine.JsonFlowEngineService;
import com.pig4cloud.pigx.jsonflow.engine.NodeJobHandler;
import com.pig4cloud.pigx.jsonflow.mapper.RunNodeMapper;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 运行节点管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RunNodeServiceImpl extends ServiceImpl<RunNodeMapper, RunNode> implements RunNodeService {
	@Lazy
	@Autowired
	private JsonFlowEngineService jsonFlowEngineService;
	@Lazy
	@Autowired
	private RunJobService runJobService;
	@Lazy
	@Autowired
	private NodeJobHandler nodeJobHandler;
	@Autowired
	private IAuditorService auditorService;
	@Autowired
	private IdentifierGenerator idGenerator;

	@Override
	public boolean complete(RunJobVO runJobVO) {
		return jsonFlowEngineService.completeRunNode(runJobVO);
	}

	/**
	 * 保存前校验节点
	 * 串行节点、并行节点、结束节点有上一节点
	 * 虚拟节点、开始节点不能有上一节点
	 *
	 * @param runNodeVO 流程节点设置
	 */
	@Override
	public Boolean saveOrUpdate(RunNodeVO runNodeVO, String fromType) {
		// 保存前校验节点
		String type = runNodeVO.getNodeType();
		Integer sort = runNodeVO.getSort();
		List<RunNode> runNodes = this.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, runNodeVO.getFlowInstId()));
		if (NodeTypeEnum.SERIAL.getType().equals(type) && CollUtil.isNotEmpty(runNodes)) {
			// TODO
		} else if (NodeTypeEnum.PARALLEL.getType().equals(type) && CollUtil.isNotEmpty(runNodes)) {
			// TODO
		}
		// 判断虚拟节点必须是直接返回 且不能有条件 排序必须为负数
		else if (NodeTypeEnum.VIRTUAL.getType().equals(type)) {
			boolean rejectType = NodeCircTypeEnum.DIRECT_RETURN.getType().equals(runNodeVO.getRejectType());
			if (!rejectType) throw new ValidationException("虚拟节点【被驳回后再次提交时】必须是直接返回");
			if (sort >= 0) runNodeVO.setSort(CommonNbrPool.MINUS_INT_1);
		}
		// 判断开始节点与结束节点一定是只能有一个 TODO 结束节点支持多个
		else if (NodeTypeEnum.START.getType().equals(type) /*|| NodeTypeEnum.END.getType().equals(type)*/) {
			if (CollUtil.isNotEmpty(runNodes)) {
				boolean present = runNodes.stream().anyMatch(f -> f.getNodeType().equals(type) && !f.getId().equals(runNodeVO.getId()));
				if (present) throw new ValidationException("开始节点与结束节点有且只能有一个");
			}
		}
		// 默认增加审批过程、查看流程图
		List<String> tabs = CollUtil.newArrayList(TabsOptionEnum.COMMENT.getId(), TabsOptionEnum.FLOW_PHOTO.getId());
		// 判断一键快捷设计
		if (CommonNbrPool.STR_0.equals(fromType)) tabs.add(TabsOptionEnum.VIEW_RUN_APPLICATION.getId());
		if (ArrayUtil.isEmpty(runNodeVO.getPcTodoUrl())) {
			runNodeVO.setPcTodoUrl(ArrayUtil.toArray(tabs, String.class));
		}
		if (ArrayUtil.isEmpty(runNodeVO.getPcFinishUrl())) {
			runNodeVO.setPcFinishUrl(ArrayUtil.toArray(tabs, String.class));
		}
		// 保存或修改
		RunNode runNode = new RunNode();
		BeanUtil.copyProperties(runNodeVO, runNode);
		return super.saveOrUpdate(runNode);
	}

	@Override
	public boolean reject(RunJobVO runJobVO) {
		RunRejectVO runRejectVO = runJobVO.getRunRejectVO();
		// 设置当前节点为驳回中
		RunNode reject = this.getById(runRejectVO.getFromRunNodeId());
		this.handleRunNodeStatus(CollUtil.newArrayList(reject), NodeJobStatusEnum.REJECT.getStatus());
		// 设置驳回到节点为被驳回
		RunNode rejected = this.getById(runRejectVO.getToRunNodeId());
		this.handleRunNodeStatus(CollUtil.newArrayList(rejected), NodeJobStatusEnum.REJECTED.getStatus());
		return true;
	}

	@Override
	public List<RunJob> anyJump(RunJobVO runJobVO) {
		RunRejectVO runRejectVO = runJobVO.getRunRejectVO();
		if (runRejectVO.getToFlowNodeId().equals(runJobVO.getFlowNodeId())) throw new ValidationException("不能跳转到自身节点");
		String jobType = runRejectVO.getJobType();
		Long handleRoleId = runRejectVO.getHandleRoleId();
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runRejectVO.getToRunNodeId()));
		if (StrUtil.isNotBlank(jobType)){
			runJobs = this.selectedSkipRunJobs(runJobs);
			String roleIds = this.selectedSysRoles(runJobs, jobType);
			// 判断是否选择了参与者
			if (Objects.nonNull(handleRoleId)) {
				if (StrUtil.isNotBlank(roleIds)) runJobs = this.handleNoneMatch(runJobs, handleRoleId, roleIds, jobType);
				else runJobs.forEach(each -> {
					each.setJobType(jobType);
					each.setRoleId(handleRoleId);
				});
			} else {
				// 判断被跳转任务是否存在参与者
				runJobs = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId())).collect(Collectors.toList());
				if (CollUtil.isEmpty(runJobs)) throw new ValidationException("当前待跳转节点下可候选待跳转参与者为空，请指定待跳转角色即为其分配参与者");
			}
		} else {
			runJobs = this.selectedRunJobs(runJobs);
		}
		return runJobs;
	}

	/**
	 * 可选择运行任务列表
	 *
	 * @param runJobs 运行任务集合
	 */
	private List<RunJob> selectedRunJobs(List<RunJob> runJobs) {
		// 判断跳转到的任务不满足条件已被跳过或不存在参与者
		runJobs = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus()) &&
				Objects.nonNull(f.getRoleId())).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("当前选择跳转节点的任务已被跳过或不存在参与者");
		return runJobs;
	}

	/**
	 * 可选择运行任务列表
	 *
	 * @param runJobs 运行任务集合
	 */
	private List<RunJob> selectedSkipRunJobs(List<RunJob> runJobs) {
		// 判断跳转到的任务不满足条件已被跳过
		runJobs = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("当前选择跳转节点的任务已被跳过");
		return runJobs;
	}

	/**
	 * 可选择办理角色列表
	 *
	 * @param runJobs 运行任务集合
	 */
	private String selectedSysRoles(List<RunJob> runJobs, String jobType) {
		List<Long> roleIds = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId()) && jobType.equals(f.getJobType()))
				.map(RunJob::getRoleId).distinct().collect(Collectors.toList());
		if (CollUtil.isEmpty(roleIds)) return StrUtil.EMPTY;
		Object[] array = new Object[]{};
		if (JobUserTypeEnum.USER.getType().equals(jobType)) {
			List<SysUser> sysUsers = auditorService.listUsersByUserIds(roleIds);
			array = sysUsers.stream().map(SysUser::getName).toArray();
		} else if (JobUserTypeEnum.ROLE.getType().equals(jobType)) {
			List<SysRole> sysRoles = auditorService.listRolesByRoleIds(roleIds);
			array = sysRoles.stream().map(SysRole::getRoleName).toArray();
		} else if (JobUserTypeEnum.POST.getType().equals(jobType)) {
			List<SysPost> sysPosts = auditorService.listPostsByPostIds(roleIds);
			array = sysPosts.stream().map(SysPost::getPostName).toArray();
		} else if (JobUserTypeEnum.DEPT.getType().equals(jobType)) {
			List<SysDept> sysDepts = auditorService.listDeptsByDeptIds(roleIds);
			array = sysDepts.stream().map(SysDept::getName).toArray();
		}
		return ArrayUtil.join(array, StrUtil.COMMA);
	}

	/**
	 * 判断当前选择的办理角色是否存在任务
	 *
	 * @param runJobs      运行任务集合
	 * @param handleRoleId 处理角色
	 * @param roleIds      角色ID集合
	 */
	private List<RunJob> handleNoneMatch(List<RunJob> runJobs, Long handleRoleId, String roleIds, String jobType) {
		runJobs = runJobs.stream().filter(any -> any.getRoleId().equals(handleRoleId) && jobType.equals(any.getJobType())).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("不存在当前选择跳转节点的参与者!可选择有【" + roleIds + "】");
		return runJobs;
	}

	/**
	 * 更新符合条件节点状态
	 *
	 * @param runNodes 运行节点集合
	 * @param status   状态
	 */
	public void handleRunNodeStatus(List<RunNode> runNodes, String status) {
		boolean complete = NodeJobStatusEnum.COMPLETE.getStatus().equals(status);
		List<Long> noTimeIds = runNodes.stream().filter(f -> Objects.isNull(f.getStartTime())).map(RunNode::getId).collect(Collectors.toList());
		List<Long> hasTimeIds = runNodes.stream().filter(f -> Objects.nonNull(f.getStartTime())).map(RunNode::getId).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(noTimeIds))
			this.lambdaUpdate().set(RunNode::getStartTime, LocalDateTime.now())
					.set(complete, RunNode::getEndTime, LocalDateTime.now())
					.set(RunNode::getStatus, status)
					.in(RunNode::getId, noTimeIds)
					.update();
		if (CollUtil.isNotEmpty(hasTimeIds))
			this.lambdaUpdate().set(RunNode::getStatus, status)
					.set(complete, RunNode::getEndTime, LocalDateTime.now())
					.in(RunNode::getId, hasTimeIds)
					.update();
	}

	@Override
	public void handleRunNodeStatusByRunNodes(List<RunNode> runNodes, String status) {
		runNodes = runNodes.stream().filter(f -> NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(runNodes)) return;
		this.handleRunNodeStatus(runNodes, status);
	}

	/**
	 * 完成节点并更新操作
	 * @param id 运行节点ID
	 * @param currRunNodeId 运行节点ID
	 * @param suspension 是否挂起
	 */
	public void completeRunNodeStatus(Long id, Long currRunNodeId, String suspension) {
		RunNode runNode = this.getById(currRunNodeId);
		String[] fromRunNodeIds = runNode.getFromRunNodeIds();
		if (ArrayUtil.isNotEmpty(fromRunNodeIds)) {
			List<Long> runNodeIds = Arrays.stream(fromRunNodeIds).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
			this.lambdaUpdate().set(RunNode::getNextStatus, FlowCommonConstants.YES)
					.in(RunNode::getId, runNodeIds)
					.update();
		}
		this.lambdaUpdate().set(RunNode::getCurrStatus, FlowCommonConstants.YES)
				.eq(RunNode::getId, currRunNodeId)
				.update();
		this.lambdaUpdate().set(RunNode::getSuspension, suspension)
				.set(RunNode::getEndTime, LocalDateTime.now())
				.set(RunNode::getStatus, NodeJobStatusEnum.COMPLETE.getStatus())
				.eq(RunNode::getId, id)
				.update();
	}

	@Override
	public RunNode buildRunNode(RunJobVO runJobVO, RunNodeVO runNodeVO) {
		RunNode runNode = new RunNode();
		Long nodeId = idGenerator.nextId(null).longValue();
		runNode.setId(nodeId);
		runNode.setFlowNodeId(nodeId);
		runNode.setNodeName(runJobVO.getNodeName());
		runNode.setDefFlowId(runNodeVO.getDefFlowId());
		runNode.setFlowInstId(runNodeVO.getFlowInstId());
		runNode.setFlowKey(runNodeVO.getFlowKey());
		runNode.setNodeType(runNodeVO.getNodeType());
		runNode.setJobBtns(runNodeVO.getJobBtns());
		runNode.setPcTodoUrl(runNodeVO.getPcTodoUrl());
		runNode.setPcFinishUrl(runNodeVO.getPcFinishUrl());
		runNode.setNodeApproveMethod(runNodeVO.getNodeApproveMethod());
		runNode.setStartTime(LocalDateTime.now());
		runNode.setStatus(NodeJobStatusEnum.RUN.getStatus());
		runNode.setCreateTime(LocalDateTime.now());
		runNode.setCreateUser(SecurityUtils.getUser().getId());
		return runNode;
	}

	@Override
	public Boolean remind(RunNode runNode) {
		List<RunJob> runJobs = runJobService.doRemind(runNode.getFlowInstId(), runNode.getId());
		// 催办子流程
		if (CollUtil.isEmpty(runJobs)) runJobs = runJobService.doRemind(runNode.getSubFlowInstId(), null);
		RunJobVO runJobVO = new RunJobVO();
		BeanUtil.copyProperties(runNode, runJobVO);
		// 通知办理人
		nodeJobHandler.notify(runJobVO, runJobs);
		return Boolean.TRUE;
	}

	@Override
	public boolean suspension(RunJobVO runJobVO) {
		return this.lambdaUpdate().set(RunNode::getSuspension, runJobVO.getSuspension())
				.eq(RunNode::getId, runJobVO.getRunNodeId())
				.update();
	}

	@Override
	public List<RunNode> listByFlowNodeId(Long flowInstId) {
		return this.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
	}

	@Override
	public List<RunNode> listPreByFlowNodeId(Long flowInstId, Long runNodeId) {
		return jsonFlowEngineService.listPreByFlowNodeId(flowInstId, runNodeId);
	}

	@Override
	public List<RunNode> listAnyJumpByFlowNodeId(Long flowInstId, Long runNodeId) {
		return jsonFlowEngineService.listAnyJumpByFlowNodeId(flowInstId, runNodeId);
	}

	@Override
	public boolean startCalculateRunNodes(List<RunJob> runJobs, boolean updateRunJob) {
		List<Long> runNodeIds = runJobs.stream().map(RunJob::getRunNodeId).distinct().collect(Collectors.toList());
		this.handleRunNodeStatus(this.listByIds(runNodeIds), NodeJobStatusEnum.RUN.getStatus());
		if (updateRunJob) {
			runJobService.handleRunJobsUser(runJobs, NodeJobStatusEnum.RUN.getStatus());
		}
		return true;
	}

	@Override
	public boolean signature(RunJobVO runJobVO) {
		return jsonFlowEngineService.handleNodeSignatureType(runJobVO);
	}

}
