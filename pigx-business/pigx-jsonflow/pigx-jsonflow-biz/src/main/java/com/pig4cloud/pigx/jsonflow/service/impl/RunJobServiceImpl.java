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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobBtnTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.entity.TabsOption;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverReasonTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobHandoverVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.TodoJobVO;
import com.pig4cloud.pigx.jsonflow.engine.IAuditorService;
import com.pig4cloud.pigx.jsonflow.engine.JsonFlowEngineService;
import com.pig4cloud.pigx.jsonflow.engine.NodeJobHandler;
import com.pig4cloud.pigx.jsonflow.mapper.RunJobMapper;
import com.pig4cloud.pigx.jsonflow.service.DistPersonService;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import com.pig4cloud.pigx.jsonflow.service.RunRejectService;
import com.pig4cloud.pigx.jsonflow.service.TabsOptionService;
import com.pig4cloud.pigx.jsonflow.support.ToDoneJobVOBuilder;
import com.pig4cloud.pigx.jsonflow.support.TodoJobVOBuilder;
import com.pig4cloud.pigx.jsonflow.util.FlowFormHttpInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.SimpMsgTempUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 任务记录
 *
 * @author luolin
 * @date 2021-03-03 09:26:28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RunJobServiceImpl extends ServiceImpl<RunJobMapper, RunJob> implements RunJobService {
	@Lazy
	@Autowired
	private JsonFlowEngineService jsonFlowEngineService;
	@Lazy
	@Autowired
	private RunNodeService runNodeService;
	@Lazy
	@Autowired
	private RunRejectService runRejectService;
	@Autowired
	private IAuditorService auditorService;
	@Lazy
	@Autowired
	private RunFlowService runFlowService;
	@Autowired
	private TabsOptionService tabsOptionService;
	@Autowired
	private IdentifierGenerator idGenerator;
	@Lazy
	@Autowired
	private DistPersonService distPersonService;
	@Lazy
	@Autowired
	private NodeJobHandler nodeJobHandler;

	// @GlobalTransactional
	@SneakyThrows
	@Override
	public boolean complete(RunJobVO runJobVO) {
		return jsonFlowEngineService.completeRunJob(runJobVO);
	}

	@Override
	public Boolean saveOrUpdate(RunJobVO runJobVO) {
		// 设置任务定义ID
		RunNode runNode = runNodeService.getById(runJobVO.getRunNodeId());

		// 保存前可能已存在多个
		/*if (Objects.nonNull(runJobVO.getId()) && StrUtil.isNotBlank(runJobVO.getUserKey())) {
			// 同节点里相同参与者KEY与人员类型的任务只能有一个
			List<RunJob> sameCalUserKey = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNode.getId())
					.eq(RunJob::getUserKey, runJobVO.getUserKey()).eq(RunJob::getValType, runJobVO.getValType())
					.ne(RunJob::getId, runJobVO.getId()));
			if (CollUtil.isNotEmpty(sameCalUserKey)) throw new ValidationException("同节点里相同参与者KEY与人员类型的任务只能有一个");
		}*/

		// 开始节点或结束节点有且只能有一个任务
		if (NodeTypeEnum.START.getType().equals(runNode.getNodeType()) || NodeTypeEnum.END.getType().equals(runNode.getNodeType())) {
			List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNode.getId())
					.ne(Objects.nonNull(runJobVO.getId()), RunJob::getId, runJobVO.getId()));
			if (CollUtil.isNotEmpty(runJobs)) throw new ValidationException("开始节点或结束节点有且只能有一个任务");
		}

		// 保存节点参与者
		List<RunJob> currRunJobs = runJobVO.getCurrRunJobs();
		if (CollUtil.isEmpty(currRunJobs)) {
			// 节点默认任务
			RunJob newRunJob = new RunJob();
			BeanUtil.copyProperties(runJobVO, newRunJob);
			this.saveOrUpdate(newRunJob);
			return Boolean.TRUE;
		}
		List<RunJob> signRunJobs = currRunJobs.stream().filter(f -> Objects.nonNull(f.getSignatureId())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(signRunJobs)) this.saveOrUpdateBatch(signRunJobs);
		Collection<RunJob> subtract = CollUtil.subtract(currRunJobs, signRunJobs);
		if (CollUtil.isEmpty(subtract)) return Boolean.TRUE;
		subtract.forEach(each -> {
			RunJob newRunJob = new RunJob();
			BeanUtil.copyProperties(runJobVO, newRunJob);
			newRunJob.setStartTime(each.getStartTime());
			newRunJob.setJobName(each.getJobName());
			newRunJob.setJobType(each.getJobType());
			newRunJob.setRoleId(each.getRoleId());
			newRunJob.setUserId(each.getUserId());
			newRunJob.setStatus(each.getStatus());
			newRunJob.setSort(each.getSort());
			this.saveOrUpdate(newRunJob);
		});
		return Boolean.TRUE;
	}

	@Override
	public boolean endCheckAllSkipComplete(RunNode runNode) {
		List<RunJob> allRunJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, runNode.getFlowInstId()));
		// 判断结束整个流程
		boolean allMatch = allRunJobs.stream().allMatch(all -> NodeJobStatusEnum.NO_START.getStatus().equals(all.getStatus()) ||
				NodeJobStatusEnum.SKIP.getStatus().equals(all.getStatus()) || NodeJobStatusEnum.COMPLETE.getStatus().equals(all.getStatus()));
		if (!allMatch) {
			throw new ValidationException("流程存在其他节点任务未审批，不能结束");
		}
		return true;
	}

	@Override
	public boolean anyJump(RunJobVO runJobVO) {
		return jsonFlowEngineService.anyJump(runJobVO);
	}

	@Override
	public boolean backPreJob(RunJobVO runJobVO) {
		return jsonFlowEngineService.backPreJob(runJobVO);
	}

	@Override
	public boolean backFirstJob(RunJobVO runJobVO) {
		return jsonFlowEngineService.backFirstJob(runJobVO);
	}

	// @GlobalTransactional
	@Override
	public boolean reject(RunJobVO runJobVO) {
		return jsonFlowEngineService.reject(runJobVO);
	}

	@Override
	public boolean signature(RunJobVO runJobVO) {
		return jsonFlowEngineService.handleSignatureType(runJobVO);
	}

	@Override
	public boolean turnRunJob(RunJobVO runJobVO) {
		this.lambdaUpdate().set(RunJob::getRoleId, runJobVO.getRoleId())
				.set(RunJob::getJobType, runJobVO.getJobType())
				.eq(RunJob::getId, runJobVO.getId())
				.update();
		// WS 钉钉通知
		String jobName = runJobVO.getJobName();
		String msg = "(审批)【转办】您有一条新的待办任务: " + jobName + "(由[" + SecurityUtils.getUser().getName() + "]转办)";
		String jobType = runJobVO.getJobType();
		List<SysUser> ddUsers = auditorService.listUsersByRoleId(runJobVO.getRoleId(), jobType);
		SimpMsgTempUtils.turnNodeNotifyUser(jobName, ddUsers, runJobVO.getFlowInstId(), runJobVO.getFlowKey(), msg);
		return true;
	}

	@Override
	public boolean appointUser(RunJobVO runJobVO) {
		// 前端指定下一办理人
		DistPerson nextUserRole = runJobVO.getNextUserRole();
		this.lambdaUpdate().set(RunJob::getRoleId, nextUserRole.getRoleId())
				.set(RunJob::getJobType, nextUserRole.getJobType())
				.eq(RunJob::getId, runJobVO.getId())
				.update();
		// WS 钉钉通知
		RunJob runJob = new RunJob();
		BeanUtil.copyProperties(runJobVO, runJob);
		nodeJobHandler.nodeNotifySingleUser(runJobVO, CollUtil.newArrayList(runJob));
		return true;
	}

	@Override
	public boolean suspension(RunJobVO runJobVO) {
		return this.lambdaUpdate().set(RunJob::getSuspension, runJobVO.getSuspension())
				.set(RunJob::getSuspensionReason, runJobVO.getSuspensionReason())
				.eq(RunJob::getId, runJobVO.getId())
				.in(RunJob::getStatus, NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus())
				.update();
	}

	@Override
	public boolean signForJob(RunJobVO runJobVO) {
		String signForType = runJobVO.getSignForType();
		List<String> statuses = CollUtil.newArrayList(NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
		if (FlowCommonConstants.YES.equals(signForType)) {
			this.lambdaUpdate().set(RunJob::getRoleId, SecurityUtils.getUser().getId())
					.set(RunJob::getJobType, JobUserTypeEnum.USER.getType())
					.eq(RunJob::getId, runJobVO.getId())
					.in(RunJob::getStatus, statuses)
					.update();
		} else {
			this.lambdaUpdate().set(RunJob::getRoleId, null)
					.set(RunJob::getJobType, JobUserTypeEnum.NONE.getType())
					.eq(RunJob::getId, runJobVO.getId())
					.in(RunJob::getStatus, statuses)
					.update();
		}
		return true;
	}

	@Override
	public IPage getTodoPage(Page page, TodoJobVO todoJobVO) {
		List<RunFlow> runFlows = new ArrayList<>();
		List<RunJob> jobList = this.queryJobList(page, todoJobVO, runFlows);
		if (CollUtil.isEmpty(jobList)) return page;

		// 构造流程与任务信息
		List<TodoJobVO> jobVOList = TodoJobVOBuilder.handleAllFlowKey(jobList, runFlows);
		page.setRecords(jobVOList);

		return page;
	}

	@Override
	public IPage getTodoSize(Page page, TodoJobVO todoJobVO) {
		List<RunJob> jobList = this.queryJobList(page, todoJobVO, null);
		if (CollUtil.isEmpty(jobList)) return page;

		List<TodoJobVO> jobVOList = jobList.stream().map(job -> {
			TodoJobVO res = new TodoJobVO();
			BeanUtil.copyProperties(job, res);
			return res;
		}).collect(Collectors.toList());
		// 根据开始时间排序
		page.setRecords(jobVOList);

		return page;
	}

	/**
	 * 获取待办任务
	 *
	 * @param todoJobVO 待办参数
	 * @return List
	 */
	private List<RunJob> queryJobList(Page<RunJob> page, TodoJobVO todoJobVO, List<RunFlow> flows) {
		String[] queryTime = todoJobVO.getQueryTime();
		Long initiatorId = todoJobVO.getInitiatorId();
		String status = todoJobVO.getStatus();
		String signatureType = todoJobVO.getSignatureType();
		String belongType = todoJobVO.getBelongType();

		// 根据申请人查询正在运行中的流程
		List<RunFlow> runFlows = runFlowService.listFlowInstIds(initiatorId, CollUtil.newArrayList(FlowStatusEnum.RUN.getStatus()));
		if (CollUtil.isEmpty(runFlows)) return null;
		if (CollUtil.isNotEmpty(runFlows) && Objects.nonNull(flows)) flows.addAll(runFlows);

		List<Long> flowIds = runFlows.stream().map(RunFlow::getId).collect(Collectors.toList());
		QueryWrapper<RunJob> query = this.copyRunJobQueryWrapper(todoJobVO);
		LambdaQueryWrapper<RunJob> lambdaQueryWrapper = buildRunJobLambdaQueryWrapper(queryTime, flowIds, query, signatureType, belongType, null);
		// 默认查询运行中和被驳回任务
		if (StrUtil.isEmpty(status)) {
			lambdaQueryWrapper.in(RunJob::getStatus, NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
			lambdaQueryWrapper.orderByDesc(RunJob::getStartTime);
		}
		// 分页查询
		List<RunJob> records = this.page(page, query).getRecords();
		// List<RunJob> jobList = this.list(query);

		if (CollUtil.isEmpty(records)) return null;
		return records;
	}

	/**
	 * 对象转换
	 *
	 * @param todoJobVO 待办任务
	 */
	private QueryWrapper<RunJob> copyRunJobQueryWrapper(TodoJobVO todoJobVO) {
		RunJob runJob = new RunJob();
		BeanUtil.copyProperties(todoJobVO, runJob);
		runJob.setSignatureType(null);
		runJob.setBelongType(null);
		return Wrappers.query(runJob);
	}

	/**
	 * 构建查询条件 判断公共任务
	 *
	 * @param queryTime 查询时间
	 * @param flowIds   流程实例ID
	 * @param query     条件
	 */
	private LambdaQueryWrapper<RunJob> buildRunJobLambdaQueryWrapper(String[] queryTime, List<Long> flowIds, QueryWrapper<RunJob> query
			, String signatureType, String belongType, String queryJobType) {
		Long userId = SecurityUtils.getUser().getId();
		List<Long> roleIds = SecurityUtils.getRoleIds();
		Long deptId = SecurityUtils.getUser().getDeptId();
		List<SysPost> sysPosts = auditorService.listPostsByUserId(userId);
		boolean b = StrUtil.isNotBlank(belongType) && !CommonNbrPool.MINUS_STR_1.equals(belongType);
		List<Long> postIds = sysPosts.stream().map(SysPost::getPostId).collect(Collectors.toList());
		return QueryWrapperUtils.queryTime(query, RunJob::getCreateTime, queryTime)
				.nested(!CommonNbrPool.STR_1.equals(queryJobType), e ->
						e.or(e2 -> e2.eq(RunJob::getRoleId, userId).eq(RunJob::getJobType, JobUserTypeEnum.USER.getType()))
						.or(e2 -> e2.in(CollUtil.isNotEmpty(roleIds), RunJob::getRoleId, roleIds).eq(RunJob::getJobType, JobUserTypeEnum.ROLE.getType()))
						.or(e2 -> e2.in(CollUtil.isNotEmpty(postIds), RunJob::getRoleId, postIds).eq(RunJob::getJobType, JobUserTypeEnum.POST.getType()))
						.or(e2 -> e2.eq(RunJob::getRoleId, deptId).eq(RunJob::getJobType, JobUserTypeEnum.DEPT.getType()))
				)
				.isNotNull(StrUtil.isNotBlank(signatureType), RunJob::getSignatureId)
				.isNull(StrUtil.isBlank(belongType) && StrUtil.isBlank(signatureType), RunJob::getSignatureId)
				.isNull(CommonNbrPool.STR_0.equals(belongType) && StrUtil.isBlank(signatureType), RunJob::getSignatureId)
				.eq(b, RunJob::getBelongType, belongType)
				.isNull(CommonNbrPool.STR_1.equals(queryJobType), RunJob::getRoleId)
				.eq(CommonNbrPool.STR_0.equals(queryJobType), RunJob::getUserId, userId)
				.in(CollUtil.isNotEmpty(flowIds), RunJob::getFlowInstId, flowIds);
	}

	@Override
	public TodoJobVO getTodoDetailById(TodoJobVO todoJobVO) {
		Long flowInstId = todoJobVO.getFlowInstId();
		// 查询节点信息
		RunNodeVO runNodeVO = this.getRunNodeById(flowInstId, todoJobVO.getRunNodeId());
		todoJobVO.setRunNodeVO(runNodeVO);
		// 审批按钮
		List<String> jobBtns = null;
		if (ArrayUtil.isNotEmpty(runNodeVO.getJobBtns())) {
			jobBtns = Arrays.stream(runNodeVO.getJobBtns()).filter(StrUtil::isNotBlank).collect(Collectors.toList());
		}
		if (CollUtil.isEmpty(jobBtns)) {
			jobBtns = JobBtnTypeEnum.listAllBtns();
		} else {
			jobBtns = jobBtns.stream().map(m -> JobBtnTypeEnum.getEnumByType(m).getType()).collect(Collectors.toList());
		}
		todoJobVO.setJobBtns(ArrayUtil.toArray(jobBtns, String.class));
		// 页面配置信息
		List<TabsOption> tabsOptions = this.getTabsOptions(runNodeVO, true);
		todoJobVO.setElTabs(tabsOptions);
		// 工单数据
		RunFlow runFlow = runFlowService.getById(flowInstId);
		TodoJobVOBuilder.flowToTodoJobVO(todoJobVO, runFlow);
		Object order = FlowFormHttpInvokeUtils.getOrderByFlowInstId(runFlow);
		todoJobVO.setOrder(BeanUtil.beanToMap(order));
		return todoJobVO;
	}

	private List<TabsOption> getTabsOptions(RunNodeVO runNodeVO, boolean isTodo) {
		List<Long> pcTodoUrl = Arrays.stream(isTodo ? runNodeVO.getPcTodoUrl() : runNodeVO.getPcFinishUrl()).map(Long::valueOf)
				.collect(Collectors.toList());
		return tabsOptionService.listByIds(pcTodoUrl);
	}

	private RunNodeVO getRunNodeById(Long flowInstId, Long runNodeId) {
		RunNode runNode;
		if (Objects.nonNull(runNodeId)) runNode = runNodeService.getById(runNodeId);
		else {
			// 运行流程管理查看工单
			List<RunNode> runNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
			runNode = runNodes.stream().findAny().orElseThrow(() -> new ValidationException("运行节点不存在"));
		}
		RunNodeVO runNodeVO = new RunNodeVO();
		BeanUtil.copyProperties(runNode, runNodeVO);
		return runNodeVO;
	}

	@Override
	public IPage getToDonePage(Page page, ToDoneJobVO toDoneJobVO) {
		String[] queryTime = toDoneJobVO.getQueryTime();
		Long initiatorId = toDoneJobVO.getInitiatorId();
		String status = toDoneJobVO.getStatus();
		String signatureType = toDoneJobVO.getSignatureType();
		String belongType = toDoneJobVO.getBelongType();
		String queryJobType = toDoneJobVO.getQueryJobType();
		// 根据申请人查询流程
		List<RunFlow> runFlows = CollUtil.newArrayList();
		List<Long> flowIds = CollUtil.newArrayList();
		if (Objects.nonNull(initiatorId) || StrUtil.isNotBlank(toDoneJobVO.getFlowStatus())) {
			runFlows = runFlowService.listFlowInstIds(initiatorId, CollUtil.newArrayList(toDoneJobVO.getFlowStatus()));
			if (CollUtil.isEmpty(runFlows)) return page;
			flowIds = runFlows.stream().map(RunFlow::getId).collect(Collectors.toList());
		}
		QueryWrapper<RunJob> query = this.copyRunJobDoneQueryWrapper(toDoneJobVO);
		LambdaQueryWrapper<RunJob> lambdaQueryWrapper = buildRunJobLambdaQueryWrapper(queryTime, flowIds, query, signatureType, belongType, queryJobType);
		// 默认查询已办任务 判断公共任务
		if (StrUtil.isEmpty(status)) {
			List<String> statuses = CollUtil.newArrayList(NodeJobStatusEnum.COMPLETE.getStatus());
			if (FlowCommonConstants.YES.equals(queryJobType)) statuses = CollUtil.newArrayList(NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
			lambdaQueryWrapper.in(RunJob::getStatus, statuses);
			lambdaQueryWrapper.orderByDesc(RunJob::getStartTime);
		}
		// 分页查询
		List<RunJob> records = this.page(page, query).getRecords();
		// List<RunJob> jobList = this.list(query);
		if (CollUtil.isEmpty(records)) return page;
		if (CollUtil.isEmpty(runFlows)) {
			List<Long> flowInstIds = records.stream().map(RunJob::getFlowInstId).distinct().collect(Collectors.toList());
			runFlows = runFlowService.listByIds(flowInstIds);
		}
		// 构造流程与任务信息
		List<Long> runNodeIds = records.stream().map(RunJob::getRunNodeId).distinct().collect(Collectors.toList());
		List<RunNode> runNodes = runNodeService.listByIds(runNodeIds);
		List<ToDoneJobVO> jobVOList = ToDoneJobVOBuilder.handleAllFlowKey(records, runFlows, runNodes);
		page.setRecords(jobVOList);
		return page;
	}

	/**
	 * 对象转换
	 *
	 * @param toDoneJobVO 已办任务
	 */
	private QueryWrapper<RunJob> copyRunJobDoneQueryWrapper(ToDoneJobVO toDoneJobVO) {
		RunJob runJob = new RunJob();
		BeanUtil.copyProperties(toDoneJobVO, runJob);
		runJob.setSignatureType(null);
		runJob.setBelongType(null);
		return Wrappers.query(runJob);
	}

	@Override
	public ToDoneJobVO getToDoneDetailById(ToDoneJobVO toDoneJobVO) {
		Long flowInstId = toDoneJobVO.getFlowInstId();
		// 查询节点信息
		RunNodeVO runNodeVO = this.getRunNodeById(flowInstId, toDoneJobVO.getRunNodeId());
		toDoneJobVO.setRunNodeVO(runNodeVO);
		// 查询默认配置信息
		List<TabsOption> tabsOptions = this.getTabsOptions(runNodeVO, false);
		toDoneJobVO.setElTabs(tabsOptions);
		// 工单数据
		RunFlow runFlow = runFlowService.getById(flowInstId);
		ToDoneJobVOBuilder.flowToToDoneJobVO(toDoneJobVO, runFlow);
		Object order = FlowFormHttpInvokeUtils.getOrderByFlowInstId(runFlow);
		toDoneJobVO.setOrder(BeanUtil.beanToMap(order));
		return toDoneJobVO;
	}

	@Override
	public IPage<RunJobHandoverVO> jobHandoverPage(Page<RunJobHandoverVO> page, String status) {
		if (!HandoverFlowStatusEnum.NO_HANDOVER.getStatus().equals(status)) {
			throw new ValidationException("当前只允许查询未交接的任务");
		}
		// 查询属于自己的
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRoleId, SecurityUtils.getUser().getId())
				.eq(RunJob::getJobType, JobUserTypeEnum.USER.getType())
				// 待办任务
				.ne(RunJob::getStatus, NodeJobStatusEnum.COMPLETE.getStatus())
				.orderByDesc(RunJob::getCreateTime));
		if (CollUtil.isEmpty(runJobs)) return page;
		List<Long> flowInstIds = runJobs.stream().map(RunJob::getFlowInstId).collect(Collectors.toList());
		List<RunFlow> runFlows = runFlowService.list(Wrappers.<RunFlow>lambdaQuery().in(RunFlow::getId, flowInstIds)
				.eq(RunFlow::getStatus, FlowStatusEnum.RUN.getStatus()));
		runJobs = runJobs.stream().filter(f -> runFlows.stream().anyMatch(any -> any.getId().equals(f.getFlowInstId()))).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) return page;
		List<RunJobHandoverVO> records = runJobs.stream().map(m -> {
			RunJobHandoverVO runJobHandoverVO = new RunJobHandoverVO();
			runJobHandoverVO.setRunJobId(m.getId());
			runJobHandoverVO.setFlowInstId(m.getFlowInstId());
			runJobHandoverVO.setNodeJobId(m.getNodeJobId());
			runJobHandoverVO.setRunNodeId(m.getRunNodeId());
			runJobHandoverVO.setStartTime(m.getCreateTime());
			runFlows.stream().filter(f -> f.getId().equals(m.getFlowInstId()))
					.findAny().ifPresent(f -> {
						runJobHandoverVO.setFlowKey(f.getFlowKey());
						runJobHandoverVO.setCode(f.getCode());
						runJobHandoverVO.setInitiatorId(f.getInitiatorId());
					});
			runJobHandoverVO.setCreateUser(m.getCreateUser());
			runJobHandoverVO.setCreateTime(m.getCreateTime());
			return runJobHandoverVO;
		}).collect(Collectors.toList());
		this.handoverFlowStatus(page, records, status);
		return page;
	}

	/**
	 * 处理交接流程状态
	 *
	 * @param records 交接记录
	 * @param status  状态
	 */
	private void handoverFlowStatus(Page<RunJobHandoverVO> page, List<RunJobHandoverVO> records, String status) {
		long nextValue = idGenerator.nextId(null).longValue();
		// 根据任务ids查询交接中的任务
		List<Long> ids = records.stream().map(RunJobHandoverVO::getRunJobId).collect(Collectors.toList());
		List<Map<String, Object>> res = (List<Map<String, Object>>) FlowFormHttpInvokeUtils.listRunByRunJobIds(ids);
		List<RunJobHandoverVO> exists = BeanUtil.copyToList(res, RunJobHandoverVO.class);
		if (HandoverFlowStatusEnum.NO_HANDOVER.getStatus().equals(status)) {
			// 默认查询未交接任务
			records.removeIf(f -> exists.stream().anyMatch(d -> d.getRunJobId().equals(f.getRunJobId())));
			records.forEach(each -> {
				each.setOrderId(nextValue);
				each.setStatus(HandoverFlowStatusEnum.NO_HANDOVER.getStatus());
			});
		}
		// 分页处理
		page.setRecords(QueryWrapperUtils.paging(page, records));
	}

	@Override
	public boolean startRunJobByUserKey(DistPersonVO distPersonVO) {
		return jsonFlowEngineService.startRunJobByUserKey(distPersonVO);
	}

	@Override
	public Boolean handleJobHandover(List<RunJob> runJobs, Long handoverUser, String handoverReason, String type) {
		Long userId = SecurityUtils.getUser().getId();
		// 转岗或离职，则全部交接
		if (HandoverReasonTypeEnum.CHANGE_POST.getType().equals(handoverReason) ||
				HandoverReasonTypeEnum.LEAVE_OFFICE.getType().equals(handoverReason)) {
			this.doHandleJobHandover(handoverUser, userId, Collections.emptyList());
			distPersonService.handleJobHandover(handoverUser, userId, null);
		} else if (HandoverFlowTypeEnum.NODE.getType().equals(type)) {
			this.doHandleJobHandover(handoverUser, userId, runJobs);
			List<Long> flowInstIds = runJobs.stream().map(RunJob::getFlowInstId).collect(Collectors.toList());
			distPersonService.handleJobHandover(handoverUser, userId, flowInstIds);
		}
		return Boolean.TRUE;
	}

	/**
	 * 发起人入库，解决交接后再驳回存在的问题
	 *
	 * @param handoverUser 交接人
	 * @param receiveUser  接收人
	 * @param runJobs      运行任务集合
	 */
	private void doHandleJobHandover(Long handoverUser, Long receiveUser, List<RunJob> runJobs) {
		List<Long> runJobIds = runJobs.stream().map(RunJob::getId).collect(Collectors.toList());
		List<Long> flowInstIds = runJobs.stream().map(RunJob::getFlowInstId).collect(Collectors.toList());
		// 更新正在运行中的任务办理人
		this.lambdaUpdate().set(RunJob::getRoleId, receiveUser)
				.in(CollUtil.isNotEmpty(flowInstIds), RunJob::getFlowInstId, flowInstIds)
				.in(CollUtil.isNotEmpty(runJobIds), RunJob::getId, runJobIds)
				.eq(RunJob::getRoleId, handoverUser)
				// 只更新用户任务
				.eq(RunJob::getJobType, JobUserTypeEnum.USER.getType())
				.update();
		// 更新交接记录表
		runRejectService.doHandleJobHandover(handoverUser, receiveUser, runJobs);
	}

	@Override
	public List<RunJobVO> listByFlowInstId(Long flowInstId) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, flowInstId));
		List<SysUser> finalSysUsers = this.listSysUsersByUserIds(runJobs);
		// 设置用户和角色名称
		List<SysUser> sysUsers = this.listSysUsersByRoleIds(runJobs);
		List<SysRole> sysRoles = this.listSysRolesByRoleIds(runJobs);
		List<SysPost> sysPosts = this.listSysPostsByPostIds(runJobs);
		List<SysDept> sysDepts = this.listSysDeptsByDeptIds(runJobs);
		return runJobs.stream().map(each -> {
			RunJobVO runJobVO = new RunJobVO();
			BeanUtil.copyProperties(each, runJobVO);
			if (Objects.nonNull(runJobVO.getUserId())) {
				Optional<SysUser> any = finalSysUsers.stream().filter(f -> f.getUserId().equals(runJobVO.getUserId())).findAny();
				if (any.isPresent()) runJobVO.setUserName(any.get().getName());
				else runJobVO.setUserName("不存在的用户");
			}
			if (Objects.nonNull(runJobVO.getRoleId())) {
				if (JobUserTypeEnum.USER.getType().equals(runJobVO.getJobType())) {
					Optional<SysUser> any = sysUsers.stream().filter(f -> f.getUserId().equals(runJobVO.getRoleId())).findAny();
					if (any.isPresent()) runJobVO.setRoleName(any.get().getName());
					else runJobVO.setRoleName("不存在的用户");
				} else if (JobUserTypeEnum.ROLE.getType().equals(runJobVO.getJobType())) {
					Optional<SysRole> any = sysRoles.stream().filter(f -> f.getRoleId().equals(runJobVO.getRoleId())).findAny();
					if (any.isPresent()) runJobVO.setRoleName(any.get().getRoleName());
					else runJobVO.setRoleName("不存在的角色");
				} else if (JobUserTypeEnum.POST.getType().equals(runJobVO.getJobType())) {
					Optional<SysPost> any = sysPosts.stream().filter(f -> f.getPostId().equals(runJobVO.getRoleId())).findAny();
					if (any.isPresent()) runJobVO.setRoleName(any.get().getPostName());
					else runJobVO.setRoleName("不存在的岗位");
				} else if (JobUserTypeEnum.DEPT.getType().equals(runJobVO.getJobType())) {
					Optional<SysDept> any = sysDepts.stream().filter(f -> f.getDeptId().equals(runJobVO.getRoleId())).findAny();
					if (any.isPresent()) runJobVO.setRoleName(any.get().getName());
					else runJobVO.setRoleName("不存在的部门");
				}
			}
			return runJobVO;
		}).collect(Collectors.toList());
	}

	@Override
	public List<SysUser> listSysUsersByUserIds(List<RunJob> runJobs) {
		List<Long> finalUserIds = runJobs.stream().map(RunJob::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(finalUserIds) ? auditorService.listUsersByUserIds(finalUserIds) : new ArrayList<>();
	}

	@Override
	public List<SysUser> listSysUsersByRoleIds(List<RunJob> runJobs) {
		List<Long> userIds = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.USER.getType().equals(f.getJobType()))
				.map(RunJob::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(userIds) ? auditorService.listUsersByUserIds(userIds) : new ArrayList<>();
	}

	@Override
	public List<SysRole> listSysRolesByRoleIds(List<RunJob> runJobs) {
		List<Long> roleIds = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.ROLE.getType().equals(f.getJobType()))
				.map(RunJob::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(roleIds) ? auditorService.listRolesByRoleIds(roleIds) : new ArrayList<>();
	}

	@Override
	public List<SysPost> listSysPostsByPostIds(List<RunJob> runJobs) {
		List<Long> postIds = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.POST.getType().equals(f.getJobType()))
				.map(RunJob::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(postIds) ? auditorService.listPostsByPostIds(postIds) : new ArrayList<>();
	}

	@Override
	public List<SysDept> listSysDeptsByDeptIds(List<RunJob> runJobs) {
		List<Long> deptIds = runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.DEPT.getType().equals(f.getJobType()))
				.map(RunJob::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(deptIds) ? auditorService.listDeptsByDeptIds(deptIds) : new ArrayList<>();
	}

	@Override
	public List<SysUser> listUsersByRunNodeId(Long runNodeId) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
		return this.listSysUsersByUserIds(runJobs);
	}

	@Override
	public List<SysUser> listUserByRunNodeId(Long runNodeId) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
		return this.listSysUsersByRoleIds(runJobs);
	}

	@Override
	public List<SysRole> listRoleByRunNodeId(Long runNodeId) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
		return this.listSysRolesByRoleIds(runJobs);
	}

	@Override
	public List<SysPost> listPostByRunNodeId(Long runNodeId) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
		return this.listSysPostsByPostIds(runJobs);
	}

	@Override
	public List<SysDept> listDeptByRunNodeId(Long runNodeId) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
		return this.listSysDeptsByDeptIds(runJobs);
	}

	@Override
	public List<RunJob> listRunJobByRunNodeId(Long runNodeId) {
		return this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
	}

	/**
	 * 完成节点任务
	 * @param runNodeId 运行节点ID
	 * @param suspension 是否挂起
	 */
	public void completeRunJobStatus(Long runNodeId, List<Long> runJobIds, String suspension) {
		if (Objects.nonNull(runNodeId)) {
			List<RunJob> currRunJobs = this.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId));
			runJobIds = this.listRunJobsByStatus(currRunJobs);
		}
		if (StrUtil.isBlank(suspension)) suspension = NodeJobStatusEnum.ACTIVE.getStatus();
		if (CollUtil.isEmpty(runJobIds)) return;
		this.lambdaUpdate().set(RunJob::getSuspension, suspension)
				.set(RunJob::getEndTime, LocalDateTime.now())
				.set(RunJob::getStatus, NodeJobStatusEnum.COMPLETE.getStatus())
				.eq(Objects.nonNull(runNodeId), RunJob::getRunNodeId, runNodeId)
				.in(CollUtil.isNotEmpty(runJobIds), RunJob::getId, runJobIds)
				.update();
	}

	@Override
	public List<Long> listRunJobsByStatus(List<RunJob> currRunJobs) {
		return currRunJobs.stream().filter(f ->
						NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus()))
				.map(RunJob::getId).collect(Collectors.toList());
	}

	/**
	 * 更新符合条件任务状态并设置办理人
	 *
	 * @param runJobs 运行任务集合
	 * @param status  状态
	 */
	@Override
	public void handleRunJobsUser(List<RunJob> runJobs, String status) {
		List<RunJob> noTimeIds = runJobs.stream().filter(f -> Objects.isNull(f.getStartTime()))
				.peek(p -> {
					p.setStartTime(LocalDateTime.now());
					p.setStatus(status);
				}).collect(Collectors.toList());
		List<RunJob> hasTimeIds = runJobs.stream().filter(f -> Objects.nonNull(f.getStartTime())).peek(p -> p.setStatus(status)).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(noTimeIds)) this.updateBatchById(noTimeIds);
		if (CollUtil.isNotEmpty(hasTimeIds)) this.updateBatchById(hasTimeIds);
	}

	@Override
	public void handleRunJobStatus(List<RunJob> runJobs, String status) {
		boolean complete = NodeJobStatusEnum.COMPLETE.getStatus().equals(status);
		List<Long> noTimeIds = runJobs.stream().filter(f -> Objects.isNull(f.getStartTime())).map(RunJob::getId).collect(Collectors.toList());
		List<Long> hasTimeIds = runJobs.stream().filter(f -> Objects.nonNull(f.getStartTime())).map(RunJob::getId).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(noTimeIds))
			this.lambdaUpdate().set(RunJob::getStartTime, LocalDateTime.now())
					.set(complete, RunJob::getEndTime, LocalDateTime.now())
					.set(RunJob::getStatus, status)
					.in(RunJob::getId, noTimeIds)
					.update();
		if (CollUtil.isNotEmpty(hasTimeIds))
			this.lambdaUpdate().set(RunJob::getStatus, status)
					.set(complete, RunJob::getEndTime, LocalDateTime.now())
					.in(RunJob::getId, hasTimeIds)
					.update();
	}

	@Override
	public void handleRunJobStatusByRunNodeIds(List<Long> runNodeIds, String status) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, runNodeIds)
				.in(RunJob::getStatus, NodeJobStatusEnum.getRunRejectRejectedStatuses()));
		if (CollUtil.isEmpty(runJobs)) return;
		this.handleRunJobStatus(runJobs, status);
	}

	@Override
	public void handleRunNodeJobStatus(List<RunJob> runJobs, String status, boolean isUpdateNextStatus, boolean isUpdateCurrStatus) {
		if (CollUtil.isEmpty(runJobs)) return;
		List<Long> runJobIds = runJobs.stream().map(RunJob::getId).collect(Collectors.toList());
		List<Long> runNodeIds = runJobs.stream().map(RunJob::getRunNodeId).collect(Collectors.toList());
		runNodeService.lambdaUpdate().set(RunNode::getStatus, status)
				.set(isUpdateNextStatus, RunNode::getNextStatus, FlowCommonConstants.YES)
				.set(isUpdateCurrStatus, RunNode::getCurrStatus, FlowCommonConstants.YES)
				.in(RunNode::getId, runNodeIds)
				.update();
		this.lambdaUpdate().set(RunJob::getStatus, status)
				.in(RunJob::getId, runJobIds)
				.update();
	}

	@Override
	public RunJob buildRunJob(RunJobVO runJobVO, RunNodeVO runNodeVO, RunNode runNode) {
		RunJob newRunJob = new RunJob();
		DistPerson nextUserRole = runJobVO.getNextUserRole();
		this.resetByDistPerson(nextUserRole, newRunJob);
		newRunJob.setDefFlowId(runNodeVO.getDefFlowId());
		newRunJob.setFlowInstId(runNodeVO.getFlowInstId());
		newRunJob.setFlowKey(runNodeVO.getFlowKey());
		newRunJob.setJobName(runNode.getNodeName());
		newRunJob.setFlowNodeId(runNode.getFlowNodeId());
		newRunJob.setRunNodeId(runNode.getId());
		newRunJob.setNodeJobId(newRunJob.getId());
		newRunJob.setStartTime(LocalDateTime.now());
		newRunJob.setStatus(NodeJobStatusEnum.RUN.getStatus());
		newRunJob.setValType(DistValTypeEnum.ORDINARY.getType());
		newRunJob.setIsConfigJob(FlowCommonConstants.YES);
		newRunJob.setFromType(CommonNbrPool.STR_0);
		newRunJob.setBelongType(CommonNbrPool.STR_0);
		newRunJob.setCreateTime(LocalDateTime.now());
		return newRunJob;
	}

	@Override
	public RunJob resetRunJob(RunJob runJob, String signedType) {
		RunJob newRunJob = new RunJob();
		BeanUtil.copyProperties(runJob, newRunJob, FlowEntityInfoConstants.ID, FlowEntityInfoConstants.START_TIME,
				FlowEntityInfoConstants.END_TIME, FlowEntityInfoConstants.CREATE_TIME);
		newRunJob.setSignatureId(runJob.getId());
		newRunJob.setSignatureType(signedType);
		newRunJob.setStartTime(LocalDateTime.now());
		newRunJob.setCreateTime(LocalDateTime.now());
		newRunJob.setStatus(NodeJobStatusEnum.RUN.getStatus());

		// 防止不同的任务相互影响
		newRunJob.setIsConfigJob(FlowCommonConstants.NO);
		newRunJob.setValType(DistValTypeEnum.ORDINARY.getType());
		newRunJob.setDistFlowNodeId(null);
		newRunJob.setUserKey(null);
		newRunJob.setUserKeyVal(null);
		return newRunJob;
	}

	@Override
	public void resetByDistPerson(DistPerson distPerson, RunJob runJob) {
		// 重置新的ID
		if (Objects.isNull(runJob.getId())) {
			Long id = idGenerator.nextId(null).longValue();
			runJob.setId(id);
			// TODO 目前回显不单独显示
			// runJob.setNodeJobId(id);
			runJob.setIsConfigJob(FlowCommonConstants.NO);
		}

		runJob.setRoleId(distPerson.getRoleId());
		runJob.setJobType(distPerson.getJobType());
		String jobName = distPerson.getJobName();
		if (StrUtil.isNotBlank(jobName)) {
			runJob.setJobName(jobName);
		}
		Integer sort = distPerson.getSort();
		if (Objects.nonNull(sort)) {
			runJob.setSort(sort);
		}
	}

	@Override
	public Boolean retakeJob(RunJob runJob) {
		RunFlow runFlow = runFlowService.getById(runJob.getFlowInstId());
		if (!FlowStatusEnum.RUN.getStatus().equals(runFlow.getStatus())) throw new ValidationException("流程状态已不在运行中，不能做取回操作");
		RunNode runNode = runNodeService.getById(runJob.getRunNodeId());
		String[] toRunNodeIds = runNode.getToRunNodeIds();
		List<Long> runNodeIds = null;
		if (ArrayUtil.isNotEmpty(toRunNodeIds)) {
			runNodeIds = Arrays.stream(toRunNodeIds).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
		}
		if (CollUtil.isNotEmpty(runNodeIds)) {
			List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, runNodeIds));
			boolean anyMatch = runJobs.stream().anyMatch(any -> NodeJobStatusEnum.getRejectRejectedCompleteStatuses().contains(any.getStatus()));
			if (anyMatch) throw new ValidationException("下一任务已经被审批，不能做取回操作");
			List<RunJob> runJobList = runJobs.stream().filter(f -> NodeJobStatusEnum.RUN.getStatus().equals(f.getStatus())).collect(Collectors.toList());
			this.handleRunNodeJobStatus(runJobList, NodeJobStatusEnum.NO_START.getStatus(), false, false);
		}
		this.handleRunNodeJobStatus(CollUtil.newArrayList(runJob), NodeJobStatusEnum.RUN.getStatus(), false, false);
		return Boolean.TRUE;
	}

	@Override
	public Boolean signOff(RunJob runJob) {
		List<RunJob> runJobs = this.list(Wrappers.<RunJob>lambdaQuery().ne(RunJob::getId, runJob.getId())
				.eq(RunJob::getRunNodeId, runJob.getRunNodeId())
				.in(RunJob::getStatus, NodeJobStatusEnum.getRunRejectRejectedCompleteStatuses()));
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("当前节点下只有一个任务不能做减签操作");
		List<RunJob> allRunJobs = this.list(Wrappers.<RunJob>lambdaQuery().ne(RunJob::getId, runJob.getId())
				.eq(RunJob::getFlowInstId, runJob.getFlowInstId())
				.in(RunJob::getStatus, NodeJobStatusEnum.getRunRejectRejectedStatuses()));
		if (CollUtil.isEmpty(allRunJobs)) throw new ValidationException("当前流程只有一个任务不能做减签操作");
		this.lambdaUpdate().set(RunJob::getStatus, NodeJobStatusEnum.SKIP.getStatus())
				.eq(RunJob::getId, runJob.getId())
				.update();
		return Boolean.TRUE;
	}

	@Override
	public List<RunJob> doRemind(Long flowInstId, Long runNodeId) {
		return this.list(Wrappers.<RunJob>lambdaQuery()
				.eq(Objects.nonNull(flowInstId), RunJob::getFlowInstId, flowInstId)
				.eq(Objects.nonNull(runNodeId), RunJob::getRunNodeId, runNodeId)
				.in(RunJob::getStatus, NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus()));
	}

	@Override
	public Boolean remind(RunJobVO runJobVO) {
		List<RunJob> runJobs = new ArrayList<>();
		if (CommonNbrPool.STR_0.equals(runJobVO.getRemindType())) {
			RunJob runJob = this.getById(runJobVO.getId());
			runJobs = CollUtil.newArrayList(runJob);
		} else if (CommonNbrPool.STR_1.equals(runJobVO.getRemindType())) {
			// 催办子流程
			runJobs = this.doRemind(runJobVO.getSubFlowInstId(), null);
		}
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("催办的任务不存在");
		// 通知办理人
		runJobVO.setDefFlowId(runJobs.get(0).getDefFlowId());
		nodeJobHandler.notify(runJobVO, runJobs);
		return Boolean.TRUE;
	}

	@Override
	public Boolean isRead(RunJob runJob) {
		this.lambdaUpdate().set(RunJob::getIsRead, FlowCommonConstants.YES)
				.eq(RunJob::getId, runJob.getId())
				.update();
		return Boolean.TRUE;
	}

}
