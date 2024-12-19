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
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowClazzMethodEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobBtnTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.Comment;
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowVariable;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.entity.RunReject;
import com.pig4cloud.pigx.jsonflow.api.entity.WsNotice;
import com.pig4cloud.pigx.jsonflow.api.order.FlowTempStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.order.OrderStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.RunFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.engine.JsonFlowEngineService;
import com.pig4cloud.pigx.jsonflow.engine.NodeJobHandler;
import com.pig4cloud.pigx.jsonflow.mapper.RunFlowMapper;
import com.pig4cloud.pigx.jsonflow.service.CommentService;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
import com.pig4cloud.pigx.jsonflow.service.DistPersonService;
import com.pig4cloud.pigx.jsonflow.service.FlowClazzService;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import com.pig4cloud.pigx.jsonflow.service.RunRejectService;
import com.pig4cloud.pigx.jsonflow.service.WsNoticeService;
import com.pig4cloud.pigx.jsonflow.support.HandleOrderRelativeInfo;
import com.pig4cloud.pigx.jsonflow.support.OrderInfoContextHolder;
import com.pig4cloud.pigx.jsonflow.util.FlowFormHttpInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.SimpMsgTempUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RunFlowServiceImpl extends ServiceImpl<RunFlowMapper, RunFlow> implements RunFlowService {

	@Lazy
	@Autowired
	private JsonFlowEngineService jsonFlowEngineService;
	@Autowired
	private DefFlowService defFlowService;
	@Autowired
	private FlowVariableService flowVariableService;
	@Autowired
	private RunNodeService runNodeService;
	@Autowired
	private CommentService commentService;
	@Lazy
	@Autowired
	private RunJobService runJobService;
	@Autowired
	private DistPersonService distPersonService;
	@Lazy
	@Autowired
	private NodeJobHandler nodeJobHandler;
	@Lazy
	@Autowired
	private RunRejectService runRejectService;
	@Lazy
	@Autowired
	private FlowClazzService flowClazzService;
	@Lazy
	@Autowired
	private FlowRuleService flowRuleService;
	@Autowired
	private WsNoticeService wsNoticeService;
	@Autowired
	private IdentifierGenerator idGenerator;

	@Override
	public IPage getPage(Page page, RunFlow runFlow, String[] queryTime) {
		QueryWrapper<RunFlow> query = Wrappers.query(runFlow);
		QueryWrapperUtils.queryTime(query, RunFlow::getCreateTime, queryTime);
		SFunction<RunFlow, String> getFlowName = RunFlow::getFlowName;
		QueryWrapperUtils.queryLike(query, CollUtil.newArrayList(getFlowName), CollUtil.newArrayList(runFlow.getFlowName()));
		List<RunFlow> records = this.page(page, query).getRecords();
		List<RunFlowVO> runFlowVOS = records.stream().map(m -> {
			RunFlowVO runFlowVO = new RunFlowVO();
			BeanUtil.copyProperties(m, runFlowVO);
			// 计算用时
			if (FlowStatusEnum.RUN.getStatus().equals(runFlowVO.getStatus()))
				runFlowVO.setUseTime(FlowCommonConstants.AUDITING);
			else {
				LocalDateTime startTime = runFlowVO.getStartTime();
				LocalDateTime endTime = runFlowVO.getEndTime();
				if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
					Duration duration = Duration.between(startTime, endTime);
					String between = DateUtil.formatBetween(duration.getSeconds() * 1000, BetweenFormatter.Level.SECOND);
					runFlowVO.setUseTime(between);
				}
			}
			return runFlowVO;
		}).collect(Collectors.toList());
		page.setRecords(runFlowVOS);
		return page;
	}

	@Override
	public Boolean saveOrUpdate(RunFlowVO runFlowVO) {
		RunFlowVO attrs = runFlowVO.getAttrs();
		attrs.setCreateUser(SecurityUtils.getUser().getId());

		RunFlow byId = this.getById(attrs.getFlowInstId());
		// 还原状态
		attrs.setStatus(byId.getStatus());
		// 只删除当前实例配置数据
		attrs.setIsIndependent(FlowCommonConstants.YES);

		RunFlow runFlow = new RunFlow();
		BeanUtil.copyProperties(attrs, runFlow);
		// 还原ID
		Long flowInstId = attrs.getFlowInstId();
		runFlow.setId(flowInstId);

		this.saveOrUpdate(runFlow);
		// 保存其他信息
		return jsonFlowEngineService.saveOrUpdateRunFlow(runFlowVO);
	}

	@Override
	// @GlobalTransactional
	public Boolean startFlow(Map<String, Object> order, Map<String, Object> params) {
		// 保存流程条件
		flowVariableService.recordFlowVars(params);
		// 保存到分配参与者表
		distPersonService.recordDistPersons(params);
		// 发起流程
		RunFlowVO runFlowVO = this.doStartFlow(order);
		// 判断是否配置数据独立
		String isIndependent = runFlowVO.getIsIndependent();
		if (FlowCommonConstants.YES.equals(isIndependent)) {
			jsonFlowEngineService.handleIndependent(runFlowVO);
		}
		// 记录整个流程的节点信息、任务信息
		RunJobVO runJobVO = jsonFlowEngineService.recordRunNodes(runFlowVO);
		runJobVO.setInitiatorName(SecurityUtils.getUser().getName());
		OrderInfoContextHolder.initOrderFormInfo(runFlowVO.getFlowKey(), runFlowVO.getId(), order, false);
		// 流程发起事件
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.INITIATE.getMethod());
		flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		// 发起时自动审批首节点
		runJobVO.setJobBtn(JobBtnTypeEnum.INITIATE.getDesc());
		jsonFlowEngineService.completeRunJob(runJobVO);
		return Boolean.TRUE;
	}

	/**
	 * 发起流程
	 *
	 * @param order 工单数据
	 */
	private RunFlowVO doStartFlow(Map<String, Object> order) {
		Long orderId = MapUtil.getLong(order, FlowEntityInfoConstants.ID);
		String code = MapUtil.getStr(order, FlowEntityInfoConstants.CODE);
		Long defFlowId = MapUtil.getLong(order, FlowEntityInfoConstants.DEF_FLOW_ID);
		Long flowInstId = MapUtil.getLong(order, FlowEntityInfoConstants.FLOW_INST_ID);
		String flowKey = MapUtil.getStr(order, FlowEntityInfoConstants.FLOW_KEY);
		List<DefFlow> defFlows = defFlowService.list(Wrappers.<DefFlow>lambdaQuery().eq(DefFlow::getFlowKey, flowKey)
				.eq(Objects.nonNull(defFlowId), DefFlow::getId, defFlowId)
				.eq(DefFlow::getStatus, FlowTempStatusEnum.PUBLISH.getStatus()).orderByDesc(DefFlow::getVersion)
				.orderByDesc(DefFlow::getCreateTime));
		if (CollUtil.isEmpty(defFlows)) throw new RuntimeException("不存在当前办公申请的已发布流程" + flowKey);
		DefFlow defFlow = defFlows.get(0);
		// 保存流程实例信息
		RunFlow runFlow = new RunFlow();
		BeanUtil.copyProperties(defFlow, runFlow, FlowEntityInfoConstants.ID, FlowEntityInfoConstants.CREATE_USER, FlowEntityInfoConstants.CREATE_TIME);
		runFlow.setId(flowInstId);
		runFlow.setCode(code);
		runFlow.setDefFlowId(defFlow.getId());
		Long createUser = MapUtil.getLong(order, FlowEntityInfoConstants.CREATE_USER);
		runFlow.setInitiatorId(createUser);
		runFlow.setOrderId(orderId);
		runFlow.setStartTime(LocalDateTime.now());
		runFlow.setStatus(FlowStatusEnum.RUN.getStatus());
		runFlow.setCreateUser(createUser);
		this.save(runFlow);
		// 处理返回
		RunFlowVO runFlowVO = new RunFlowVO();
		BeanUtil.copyProperties(runFlow, runFlowVO);
		return runFlowVO;
	}

	// @GlobalTransactional
	@Override
	public boolean complete(RunJobVO runJobVO) {
		Long flowInstId = runJobVO.getFlowInstId();
		RunFlow runFlow = this.getById(flowInstId);
		// 更新工单完成时间、状态
		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> updateInfo = MapUtil.of(FlowEntityInfoConstants.STATUS, OrderStatusEnum.NORMAL.getStatus());
		updateInfo.put(FlowEntityInfoConstants.FINISH_TIME, now.toString());
		FlowFormHttpInvokeUtils.updateOrderInfo(updateInfo, runFlow);
		String status = FlowStatusEnum.FINISH.getStatus();
		this.lambdaUpdate().set(RunFlow::getEndTime, now)
				.set(RunFlow::getStatus, status)
				.eq(RunFlow::getId, flowInstId)
				.update();
		// 重入父流程
		Long parFlowInstId = runFlow.getParFlowInstId();
		if (Objects.nonNull(parFlowInstId)) this.backParFlow(flowInstId, runFlow.getFlowKey(), parFlowInstId, status);
		// 流程完成事件
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.FINISH.getMethod());
		flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		// WS 钉钉通知发起人
		SimpMsgTempUtils.nodeEndNotifyUser(runJobVO, CollUtil.newHashSet(runFlow.getInitiatorId()));
		return true;
	}

	@Override
	public void backParFlow(Long currFlowInstId, String currFlowKey, Long parFlowInstId, String subFlowStatus) {
		// 更新子流程状态
		RunNode runNode = this.updateRunNodeSubFlowStatus(currFlowInstId, subFlowStatus);
		// 删除了节点则不处理
		if (Objects.isNull(runNode)) return;
		RunFlow parRunFlow = this.getById(parFlowInstId);
		if (!FlowStatusEnum.RUN.getStatus().equals(parRunFlow.getStatus()) || !NodeJobStatusEnum.RUN.getStatus().equals(runNode.getStatus())) {
			return;
		}
		// 构造父子流程传参回参，子流程作废除外
		Map<String, Object> subOrder = new HashMap<>();
		RunNodeVO runNodeParam = new RunNodeVO();
		runNodeParam.setDefFlowId(runNode.getDefFlowId());
		runNodeParam.setFlowNodeId(runNode.getFlowNodeId());
		runNodeParam.setFlowInstId(currFlowInstId);
		runNodeParam.setFlowKey(currFlowKey);
		if (!FlowStatusEnum.INVALID.getStatus().equals(subFlowStatus)) {
			subOrder = flowRuleService.buildSubFlowParams(runNodeParam, CommonNbrPool.STR_2);
		}
		Map<String, Object> params = new HashMap<>();
		params.put(FlowEntityInfoConstants.FLOW_INST_ID, parFlowInstId);
		params.put(FlowEntityInfoConstants.FLOW_KEY, parRunFlow.getFlowKey());
		FlowFormHttpInvokeUtils.backParFlow(subOrder, params, runNodeParam);
		RunJobVO runJobVO = new RunJobVO();
		RunJob runJob = runJobService.getById(runNode.getSubRunJobId());
		// 判断是否二次审批
		if (!NodeJobStatusEnum.COMPLETE.getStatus().equals(runJob.getStatus())) {
			return;
		}
		BeanUtil.copyProperties(runJob, runJobVO);
		RunNodeVO runNodeVO = new RunNodeVO();
		BeanUtil.copyProperties(runNode, runNodeVO);
		runJobVO.setRunNodeVO(runNodeVO);
		// 完成整个节点
		runJobVO.setNodeCompleteType(CommonNbrPool.STR_2);
		jsonFlowEngineService.completeRunNode(runJobVO);
	}

	private RunNode updateRunNodeSubFlowStatus(Long subFlowInstId, String subFlowStatus) {
		RunNode runNode = runNodeService.getOne(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getSubFlowInstId, subFlowInstId));
		// 删除了节点则不处理
		if (Objects.nonNull(runNode)) {
			runNodeService.lambdaUpdate().set(RunNode::getSubFlowStatus, subFlowStatus)
					.eq(RunNode::getId, runNode.getId())
					.update();
		}
		return runNode;
	}

	// 重入父流程
	private void handleBackParFlow(RunFlow runFlow, Long id, String subFlowStatus, boolean isRun) {
		RunFlow currRunFlow = this.getById(id);
		Long parFlowInstId = currRunFlow.getParFlowInstId();
		if (Objects.nonNull(parFlowInstId)) {
			if (isRun) {
				this.backParFlow(id, runFlow.getFlowKey(), parFlowInstId, subFlowStatus);
			} else {
				this.updateRunNodeSubFlowStatus(id, subFlowStatus);
			}
		}
	}

	// 父流程非正常结束则结束子流程
	private void handleEndSubFlow(RunFlow runFlow, String status, String orderStatus, boolean isRun, boolean earlyComplete) {
		Long parFlowInstId = runFlow.getId();
		String flowStatus = runFlow.getStatus();
		String reason = runFlow.getInvalidReason();
		boolean invalid = FlowStatusEnum.INVALID.getStatus().equals(flowStatus);
		boolean terminate = FlowStatusEnum.TERMINATE.getStatus().equals(flowStatus);
		// 查询运行节点 TODO 若存在已删除节点子流程，则需手动结束
		List<RunNode> runNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery()
				.eq(isRun, RunNode::getSubFlowStatus, FlowStatusEnum.RUN.getStatus())
				// 父流程恢复流程
				.eq(!isRun && invalid, RunNode::getSubFlowStatus, FlowStatusEnum.INVALID.getStatus())
				.eq(!isRun && terminate, RunNode::getSubFlowStatus, FlowStatusEnum.TERMINATE.getStatus())
				.eq(RunNode::getFlowInstId, parFlowInstId)
				.isNotNull(RunNode::getSubFlowInstId));
		if (CollUtil.isEmpty(runNodes)) return;
		// 更新节点上子流程的状态
		List<Long> runNodeIds = runNodes.stream().map(RunNode::getId).collect(Collectors.toList());
		runNodeService.lambdaUpdate().set(RunNode::getSubFlowStatus, status)
				.in(RunNode::getId, runNodeIds)
				.update();
		List<Long> subFlowInstIds = runNodes.stream().map(RunNode::getSubFlowInstId).collect(Collectors.toList());
		subFlowInstIds.forEach(subFlowInstId -> {
			RunFlow subRunFlow = this.getById(subFlowInstId);
			Long id = subRunFlow.getId();
			String flowKey = subRunFlow.getFlowKey();
			if (earlyComplete) {
				this.doEarlyComplete(subRunFlow, id, flowKey, reason, status, orderStatus);
			} else {
				subRunFlow.setInvalidReason(reason);
				this.handleRelativeInfo(subRunFlow, isRun, status, orderStatus);
			}
		});
	}

	@Override
	public boolean earlyComplete(RunFlow runFlow) {
		Long id = runFlow.getId();
		String flowKey = runFlow.getFlowKey();
		String reason = runFlow.getInvalidReason();
		String status = FlowStatusEnum.FINISH.getStatus();
		String orderStatus = OrderStatusEnum.NORMAL.getStatus();
		// 重入父流程
		this.handleBackParFlow(runFlow, id, status, true);
		// 处理子流程
		this.handleEndSubFlow(runFlow, status, orderStatus, true, true);
		this.doEarlyComplete(runFlow, id, flowKey, reason, status, orderStatus);
		return true;
	}

	private void doEarlyComplete(RunFlow runFlow, Long id, String flowKey, String reason, String status, String orderStatus) {
		// 流程完成事件
		RunJobVO runJobVO = new RunJobVO();
		BeanUtil.copyProperties(runFlow, runJobVO);
		runJobVO.setFlowInstId(id);
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.FINISH.getMethod());
		flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		// 处理流程相关信息并通知发起人员
		HandleOrderRelativeInfo.handleOrderRelativeInfo(flowKey, id, orderStatus);
		runNodeService.lambdaUpdate().set(RunNode::getStatus, NodeJobStatusEnum.COMPLETE.getStatus())
				.in(RunNode::getStatus, NodeJobStatusEnum.getRunRejectRejectedStatuses())
				.eq(RunNode::getFlowInstId, id)
				.update();
		runJobService.lambdaUpdate().set(RunJob::getStatus, NodeJobStatusEnum.COMPLETE.getStatus())
				.in(RunJob::getStatus, NodeJobStatusEnum.getRunRejectRejectedStatuses())
				.eq(RunJob::getFlowInstId, id)
				.update();
		runRejectService.lambdaUpdate().set(RunReject::getStatus, NodeJobStatusEnum.REJ_COMP.getStatus())
				.eq(RunReject::getStatus, NodeJobStatusEnum.REJ_RUN.getStatus())
				.eq(RunReject::getFlowInstId, id)
				.update();
		String msg = "(由[" + SecurityUtils.getUser().getName() + "]提前结束) 原因: " + reason;
		this.lambdaUpdate().set(RunFlow::getEndTime, LocalDateTime.now())
				.set(RunFlow::getStatus, status)
				.set(RunFlow::getInvalidReason, msg)
				.eq(RunFlow::getId, id)
				.update();
	}

	@Override
	public boolean terminateFlow(RunFlow runFlow) {
		Long id = runFlow.getId();
		String status = FlowStatusEnum.TERMINATE.getStatus();
		String orderStatus = OrderStatusEnum.TERMINATE.getStatus();
		boolean isRun = FlowStatusEnum.RUN.getStatus().equals(runFlow.getStatus());
		if (!isRun) {
			status = FlowStatusEnum.RUN.getStatus();
			orderStatus = OrderStatusEnum.RUN.getStatus();
		}
		this.handleRelativeInfo(runFlow, isRun, status, orderStatus);
		// 重入父流程
		this.handleBackParFlow(runFlow, id, status, isRun);
		// 处理子流程
		this.handleEndSubFlow(runFlow, status, orderStatus, isRun, false);
		return true;
	}

	private void handleRelativeInfo(RunFlow runFlow, boolean isRun, String status, String orderStatus) {
		Long id = runFlow.getId();
		String flowKey = runFlow.getFlowKey();
		String reason = runFlow.getInvalidReason();
		if (isRun) {
			LinkedList<String> linkedList;
			if (FlowStatusEnum.INVALID.getStatus().equals(status)) {
				linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.INVALID.getMethod());
			} else {
				linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.TERMINATE.getMethod());
			}
			RunJobVO runJobVO = new RunJobVO();
			BeanUtil.copyProperties(runFlow, runJobVO);
			runJobVO.setFlowInstId(id);
			flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
			// 处理流程相关信息并通知发起人员
			HandleOrderRelativeInfo.handleOrderRelativeInfo(flowKey, id, orderStatus);
		} else {
			// 恢复流程相关信息
			HandleOrderRelativeInfo.updateOrderStatus(id, orderStatus);
		}
		String statusName = isRun ? FlowStatusEnum.INVALID.getStatus().equals(status) ? "作废" : "终止" : "恢复";
		String msg = "(由[" + SecurityUtils.getUser().getName() + "]" + statusName + ") 原因: " + reason;
		this.lambdaUpdate().set(RunFlow::getEndTime, isRun ? LocalDateTime.now() : null)
				.set(RunFlow::getStatus, status)
				.set(RunFlow::getInvalidReason, msg)
				.eq(RunFlow::getId, id)
				.update();
	}

	// @GlobalTransactional
	@Override
	public boolean invalidFlow(RunFlow runFlow) {
		Long id = runFlow.getId();
		String status = FlowStatusEnum.INVALID.getStatus();
		String orderStatus = OrderStatusEnum.INVALID.getStatus();
		boolean isRun = FlowStatusEnum.RUN.getStatus().equals(runFlow.getStatus());
		if (!isRun) {
			status = FlowStatusEnum.RUN.getStatus();
			orderStatus = OrderStatusEnum.RUN.getStatus();
		}
		this.handleRelativeInfo(runFlow, isRun, status, orderStatus);
		// 重入父流程
		this.handleBackParFlow(runFlow, id, status, isRun);
		// 处理子流程
		this.handleEndSubFlow(runFlow, status, orderStatus, isRun, false);
		return true;
	}

	@Override
	public RunFlowVO getNodesById(Long id, String isEdit) {
		return jsonFlowEngineService.getNodesByFlowInstId(id, isEdit);
	}

	/**
	 * 前台判空删除
	 *
	 * @param runJobVO 运行任务
	 */
	@Override
	public boolean delFlowInfo(RunJobVO runJobVO) {
		log.info("进入自动删除流程中");
		Long flowInstId = runJobVO.getFlowInstId();
		// 删除流程、节点、任务、分配参与者、驳回、条件
		this.removeById(flowInstId);
		runNodeService.remove(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
		runJobService.remove(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, flowInstId));
		distPersonService.remove(Wrappers.<DistPerson>lambdaQuery().eq(DistPerson::getFlowInstId, flowInstId));
		runRejectService.remove(Wrappers.<RunReject>lambdaQuery().eq(RunReject::getFlowInstId, flowInstId));
		flowVariableService.remove(Wrappers.<FlowVariable>lambdaQuery().eq(FlowVariable::getFlowInstId, flowInstId));
		commentService.remove(Wrappers.<Comment>lambdaQuery().eq(Comment::getFlowInstId, flowInstId));
		wsNoticeService.remove(Wrappers.<WsNotice>lambdaQuery().eq(WsNotice::getFlowInstId, flowInstId));
		// 修改工单状态为未发起 已在消费死信队列中处理
		log.info("发起工单异常, 工单ID : 流程ID {} : {}", runJobVO.getOrderId(), flowInstId);
		return true;
	}

	@Override
	public List<RunFlow> listFlowInstIds(Long initiatorId, List<String> status) {
		return this.list(Wrappers.<RunFlow>lambdaQuery().eq(Objects.nonNull(initiatorId), RunFlow::getInitiatorId, initiatorId)
				.in(CollUtil.isNotEmpty(status), RunFlow::getStatus, status));
	}

	@Override
	public Boolean recallReset(RunFlow runFlow) {
		Long id = runFlow.getId();
		String status = FlowStatusEnum.RECALL.getStatus();
		String orderStatus = OrderStatusEnum.RECALL.getStatus();
		if (FlowStatusEnum.RECALL.getStatus().equals(runFlow.getStatus())) {
			status = FlowStatusEnum.RUN.getStatus();
			orderStatus = OrderStatusEnum.RUN.getStatus();
		} else {
			runFlow = this.getById(id);
			// 流程撤回事件
			RunJobVO runJobVO = new RunJobVO();
			BeanUtil.copyProperties(runFlow, runJobVO);
			runJobVO.setFlowInstId(id);
			LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.RECALL.getMethod());
			flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		}
		HandleOrderRelativeInfo.updateOrderStatus(id, orderStatus);
		// 更新流程状态
		this.lambdaUpdate().set(RunFlow::getStatus, status)
				.eq(RunFlow::getId, id)
				.update();
		return Boolean.TRUE;
	}

	@Override
	public Boolean remind(RunFlow runFlow) {
		List<RunJob> runJobs = runJobService.doRemind(runFlow.getId(), null);
		// 通知办理人
		RunJobVO runJobVO = new RunJobVO();
		BeanUtil.copyProperties(runFlow, runJobVO);
		nodeJobHandler.notify(runJobVO, runJobs);
		return Boolean.TRUE;
	}

}
