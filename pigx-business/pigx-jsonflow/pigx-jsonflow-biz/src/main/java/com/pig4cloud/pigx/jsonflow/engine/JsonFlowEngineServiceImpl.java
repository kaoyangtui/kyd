package com.pig4cloud.pigx.jsonflow.engine;

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
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.ValidationExceptions;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowClazzMethodEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobApproveMethodEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobBtnTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobSignatureTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeApproveMethodEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeCircTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeSignatureTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.SubFlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowClazz;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.FormOption;
import com.pig4cloud.pigx.jsonflow.api.entity.NodeJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.entity.RunReject;
import com.pig4cloud.pigx.jsonflow.api.entity.TabsOption;
import com.pig4cloud.pigx.jsonflow.api.vo.DefFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowClazzVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeRelVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.api.vo.NodeJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunRejectVO;
import com.pig4cloud.pigx.jsonflow.config.FlowCommonProperties;
import com.pig4cloud.pigx.jsonflow.service.CommentService;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
import com.pig4cloud.pigx.jsonflow.service.FlowClazzService;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeRelService;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import com.pig4cloud.pigx.jsonflow.service.FormOptionService;
import com.pig4cloud.pigx.jsonflow.service.NodeJobService;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import com.pig4cloud.pigx.jsonflow.service.RunRejectService;
import com.pig4cloud.pigx.jsonflow.service.TabsOptionService;
import com.pig4cloud.pigx.jsonflow.util.FlowFormHttpInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.SimpMsgTempUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * JsonFlow工作流引擎实现
 *
 * @author luolin
 * @date 2020/2/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JsonFlowEngineServiceImpl implements JsonFlowEngineService {

	private final FlowNodeRelService flowNodeRelService;
	private final NodeJobService nodeJobService;
	private final FlowNodeService flowNodeService;
	private final FlowClazzService flowClazzService;
	private final RunNodeService runNodeService;
	private final RunRejectService runRejectService;
	private final FlowVariableService flowVariableService;
	private final NodeJobHandler nodeJobHandler;
	private final CommentService commentService;
	private final RunFlowService runFlowService;
	private final RunJobService runJobService;
	private final FlowCommonProperties flowCommonProperties;
	private final TabsOptionService tabsOptionService;
	private final DefFlowService defFlowService;
	private final IdentifierGenerator idGenerator;
	private final IAuditorService auditorService;
	private final FormOptionService formOptionService;
	private final FlowRuleService flowRuleService;

	/**
	 * 代码生成流程例子
	 */
	@Override
	public Boolean testCodeGen() {
		DefFlowVO defFlowVO = new DefFlowVO();
		List<RunNodeVO> nodeList = new ArrayList<>();
		RunNodeVO flowNodeVO = new RunNodeVO();
		RunNodeVO nodeAttrs = new RunNodeVO();
		nodeAttrs.setId(10000L);
		nodeAttrs.setNodeName("开始");
		flowNodeVO.setType(NodeTypeEnum.START.getNodeType());
		flowNodeVO.setAttrs(nodeAttrs);
		nodeList.add(flowNodeVO);
		RunNodeVO flowNodeVO2 = new RunNodeVO();
		RunNodeVO nodeAttrs2 = new RunNodeVO();
		nodeAttrs2.setId(20000L);
		nodeAttrs2.setNodeName("串行 (1)");
		flowNodeVO2.setType(NodeTypeEnum.SERIAL.getNodeType());
		flowNodeVO2.setAttrs(nodeAttrs2);
		nodeList.add(flowNodeVO2);
		RunNodeVO flowNodeVO3 = new RunNodeVO();
		RunNodeVO nodeAttrs3 = new RunNodeVO();
		nodeAttrs3.setId(30000L);
		nodeAttrs3.setNodeName("结束");
		flowNodeVO3.setType(NodeTypeEnum.END.getNodeType());
		flowNodeVO3.setAttrs(nodeAttrs3);
		nodeList.add(flowNodeVO3);
		List<FlowNodeRelVO> linkList = new ArrayList<>();
		FlowNodeRelVO flowNodeRelVO = new FlowNodeRelVO();
		FlowNodeRelVO attrsRel = new FlowNodeRelVO();
		attrsRel.setId(100000L);
		attrsRel.setLabel("开始到串行");
		attrsRel.setSourceId(10000L);
		attrsRel.setTargetId(20000L);
		flowNodeRelVO.setAttrs(attrsRel);
		linkList.add(flowNodeRelVO);
		FlowNodeRelVO flowNodeRelVO2 = new FlowNodeRelVO();
		FlowNodeRelVO attrsRel2 = new FlowNodeRelVO();
		attrsRel2.setId(200000L);
		attrsRel2.setLabel("串行到结束");
		attrsRel2.setSourceId(20000L);
		attrsRel2.setTargetId(30000L);
		flowNodeRelVO2.setAttrs(attrsRel2);
		linkList.add(flowNodeRelVO2);
		defFlowVO.setNodeList(nodeList);
		defFlowVO.setLinkList(linkList);
		RunFlowVO attrs = new RunFlowVO();
		attrs.setId(1000000L);
		attrs.setFlowKey("CodeGen");
		attrs.setFlowName("代码生成");
		attrs.setGroupName("代码生成");
		defFlowVO.setAttrs(attrs);
		return this.saveOrUpdateDefFlow(defFlowVO);
	}

	/**
	 * 代码更新流程例子
	 */
	public Boolean testCodeUpdate() {
		DefFlowVO defFlowVO = new DefFlowVO();
		List<RunNodeVO> nodeList = new ArrayList<>();
		RunNodeVO flowNodeVO = new RunNodeVO();
		RunNodeVO nodeAttrs = new RunNodeVO();
		nodeAttrs.setId(10000L);
		nodeAttrs.setNodeName("开始");
		flowNodeVO.setType(NodeTypeEnum.START.getNodeType());
		flowNodeVO.setAttrs(nodeAttrs);
		nodeList.add(flowNodeVO);
		RunNodeVO flowNodeVO3 = new RunNodeVO();
		RunNodeVO nodeAttrs3 = new RunNodeVO();
		nodeAttrs3.setId(30000L);
		nodeAttrs3.setNodeName("结束");
		flowNodeVO3.setType(NodeTypeEnum.END.getNodeType());
		flowNodeVO3.setAttrs(nodeAttrs3);
		List<FlowNodeRelVO> linkList = new ArrayList<>();
		FlowNodeRelVO flowNodeRelVO = new FlowNodeRelVO();
		FlowNodeRelVO attrsRel = new FlowNodeRelVO();
		attrsRel.setId(100000L);
		attrsRel.setLabel("开始到结束");
		attrsRel.setSourceId(10000L);
		attrsRel.setTargetId(30000L);
		flowNodeRelVO.setAttrs(attrsRel);
		defFlowVO.setNodeList(nodeList);
		defFlowVO.setLinkList(linkList);
		RunFlowVO attrs = new RunFlowVO();
		attrs.setId(1000000L);
		attrs.setFlowKey("CodeGen");
		attrs.setFlowName("代码更新");
		attrs.setGroupName("代码更新");
		defFlowVO.setAttrs(attrs);
		return this.saveOrUpdateDefFlow(defFlowVO);
	}

	@Override
	public DefFlowVO getNodesByDefFlowId(Long defFlowId) {
		DefFlowVO defFlowVO = new DefFlowVO();
		defFlowVO.setNodeList(new ArrayList<>());
		defFlowVO.setLinkList(new ArrayList<>());
		// 查询属性配置
		DefFlow defFlow = defFlowService.getById(defFlowId);
		RunFlowVO attrs = new RunFlowVO();
		BeanUtil.copyProperties(defFlow, attrs);
		defFlowVO.setAttrs(attrs);
		// 处理节点任务属性
		List<NodeJob> nodeJobs = nodeJobService.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getDefFlowId, defFlowId));
		List<NodeJob> defNodeJobs = nodeJobs.stream().filter(f -> CommonNbrPool.STR_0.equals(f.getFromType())).collect(Collectors.toList());
		nodeJobs = nodeJobs.stream().filter(f -> !CommonNbrPool.STR_0.equals(f.getFromType())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(nodeJobs)) {
			List<RunNodeVO> nodeJobVOS = nodeJobs.stream().map(m -> {
						RunNodeVO res = new RunNodeVO();
						RunJobVO nodeJobVO = new RunJobVO();
						BeanUtil.copyProperties(m, nodeJobVO);
						res.setType(NodeTypeEnum.JOB.getNodeType());
						res.setDefJob(nodeJobVO);
						// 处理参数规则
						FlowRuleVO flowRuleVO = new FlowRuleVO();
						flowRuleVO.setRelObjId(nodeJobVO.getFlowNodeId());
						flowRuleVO.setValType(nodeJobVO.getValType());
						flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
						if (DistValTypeEnum.HTTP.getType().equals(nodeJobVO.getValType())) {
							flowRuleVO.setHttpUrl(nodeJobVO.getUserKeyVal());
							this.buildFlowRuleParams(attrs, nodeJobVO.getHttpParams(), flowRuleVO);
						} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(nodeJobVO.getValType())) {
							this.buildFixedRoles(attrs, nodeJobVO.getRoleUserId(), flowRuleVO);
						} else {
							this.buildCondGroups(attrs, nodeJobVO.getCondGroups(), flowRuleVO);
						}
						return res;
					}).collect(Collectors.toList());
			defFlowVO.getNodeList().addAll(nodeJobVOS);
		}
		List<TabsOption> tabsOptions = tabsOptionService.list();
		// 处理流程节点属性
		List<FlowNode> flowNodes = flowNodeService.list(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, defFlowId));
		if (CollUtil.isNotEmpty(flowNodes)) {
			List<RunNodeVO> flowNodeVOS = flowNodes.stream().map(m -> {
				RunNodeVO res = new RunNodeVO();
				RunNodeVO flowNodeVO = new RunNodeVO();
				BeanUtil.copyProperties(m, flowNodeVO);
				res.setType(NodeTypeEnum.getEnumByNodeType(flowNodeVO.getNodeType()).getNodeType());
				res.setAttrs(flowNodeVO);
				// 处理默认节点任务
				Optional<NodeJob> existNodeJob = defNodeJobs.stream().filter(f -> f.getFlowNodeId().equals(flowNodeVO.getId())).findAny();
				existNodeJob.ifPresent(nodeJob -> {
					RunJobVO nodeJobVO = new RunJobVO();
					BeanUtil.copyProperties(nodeJob, nodeJobVO);
					res.setDefJob(nodeJobVO);
					// 处理参数规则
					FlowRuleVO flowRuleVO = new FlowRuleVO();
					flowRuleVO.setRelObjId(flowNodeVO.getId());
					flowRuleVO.setValType(nodeJobVO.getValType());
					flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
					if (DistValTypeEnum.HTTP.getType().equals(nodeJobVO.getValType())) {
						flowRuleVO.setHttpUrl(nodeJobVO.getUserKeyVal());
						this.buildFlowRuleParams(attrs, nodeJobVO.getHttpParams(), flowRuleVO);
					} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(nodeJobVO.getValType())) {
						this.buildFixedRoles(attrs, nodeJobVO.getRoleUserId(), flowRuleVO);
					} else {
						this.buildCondGroups(attrs, nodeJobVO.getCondGroups(), flowRuleVO);
					}
				});
				// 处理节点监听事件
				this.buildFlowClazz(attrs, flowNodeVO.getClazzes(), flowNodeVO.getId());
				// 处理表单权限
				this.buildSaveFormPerms(attrs, flowNodeVO, tabsOptions);
				// 处理子流程Http配置及父子流程参数
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(flowNodeVO.getId());
				flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
				flowRuleVO.setType(FlowParamRuleEnum.SUB_FLOW.getType());
				this.buildFlowRuleParams(attrs, flowNodeVO.getSubFlowParams(), flowRuleVO);
				// 处理子流程HttpUrl配置
				this.buildSubFlowHttpUrl(attrs, flowNodeVO, flowRuleVO);
				return res;
			}).collect(Collectors.toList());
			defFlowVO.getNodeList().addAll(flowNodeVOS);
		}
		// 处理节点关系属性
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listAllFlowNodeRels(defFlowId, null);
		if (CollUtil.isNotEmpty(flowNodeRels)) {
			List<FlowNodeRelVO> flowNodeRelVOS = flowNodeRels.stream().map(m -> {
				FlowNodeRelVO res = new FlowNodeRelVO();
				FlowNodeRelVO flowNodeRelVO = new FlowNodeRelVO();
				BeanUtil.copyProperties(m, flowNodeRelVO);
				flowNodeRelVO.setSourceId(flowNodeRelVO.getFromFlowNodeId());
				if (CommonNbrPool.STR_0.equals(flowNodeRelVO.getType())) {
					flowNodeRelVO.setTargetId(flowNodeRelVO.getToFlowNodeId());
				} else {
					flowNodeRelVO.setTargetId(flowNodeRelVO.getToNodeJobId());
				}
				res.setType(NodeTypeEnum.LINK.getNodeType());
				res.setAttrs(flowNodeRelVO);
				// 处理参数规则
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(flowNodeRelVO.getId());
				flowRuleVO.setValType(flowNodeRelVO.getValType());
				flowRuleVO.setType(FlowParamRuleEnum.LINK.getType());
				if (DistValTypeEnum.HTTP.getType().equals(flowNodeRelVO.getValType())) {
					flowRuleVO.setHttpUrl(flowNodeRelVO.getVarKeyVal());
					this.buildFlowRuleParams(attrs, flowNodeRelVO.getHttpParams(), flowRuleVO);
				} else {
					this.buildCondGroups(attrs, flowNodeRelVO.getCondGroups(), flowRuleVO);
				}
				return res;
			}).collect(Collectors.toList());
			defFlowVO.setLinkList(flowNodeRelVOS);
		}
		// 处理全局监听事件
		this.buildFlowClazz(attrs, attrs.getClazzes(), null);
		// 处理关联表单Http接口
		this.buildFlowFormHttp(attrs);
		return defFlowVO;
	}

	@Override
	public RunFlowVO getNodesByFlowInstId(Long flowInstId, String isEdit) {
		RunFlowVO runFlowVO = new RunFlowVO();
		runFlowVO.setNodeList(new ArrayList<>());
		runFlowVO.setLinkList(new ArrayList<>());
		// 查询属性配置
		RunFlow runFlow = runFlowService.getById(flowInstId);
		Long defFlowId = runFlow.getDefFlowId();
		RunFlowVO attrs = new RunFlowVO();
		BeanUtil.copyProperties(runFlow, attrs);
		// 交换ID
		attrs.setFlowInstId(attrs.getId());
		attrs.setId(attrs.getDefFlowId());
		runFlowVO.setAttrs(attrs);
		// 处理节点任务属性
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, flowInstId));
		List<RunJob> defRunJobs = runJobs.stream().filter(f -> CommonNbrPool.STR_0.equals(f.getFromType())).collect(Collectors.toList());
		runJobs = runJobs.stream().filter(f -> !CommonNbrPool.STR_0.equals(f.getFromType())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(runJobs)) {
			List<RunJob> finalRunJobs = runJobs;
			// 构造配置任务
			List<RunNodeVO> runJobVOS = runJobs.stream().filter(f -> FlowCommonConstants.YES.equals(f.getIsConfigJob())).map(m -> {
				RunNodeVO res = new RunNodeVO();
				RunJobVO runJobVO = new RunJobVO();
				BeanUtil.copyProperties(m, runJobVO);
				// 交换ID
				runJobVO.setRunJobId(runJobVO.getId());
				runJobVO.setId(runJobVO.getNodeJobId());
				if (FlowCommonConstants.YES.equals(isEdit)) {
					List<RunJob> currRunJobs = finalRunJobs.stream().filter(f -> f.getNodeJobId().equals(runJobVO.getNodeJobId())).collect(Collectors.toList());
					runJobVO.setCurrRunJobs(currRunJobs);
				}
				res.setType(NodeTypeEnum.JOB.getNodeType());
				res.setDefJob(runJobVO);
				// 处理参数规则
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(runJobVO.getFlowNodeId());
				flowRuleVO.setValType(runJobVO.getValType());
				flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
				if (DistValTypeEnum.HTTP.getType().equals(runJobVO.getValType())) {
					flowRuleVO.setHttpUrl(runJobVO.getUserKeyVal());
					this.buildFlowRuleParams(attrs, runJobVO.getHttpParams(), flowRuleVO);
				} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(runJobVO.getValType())) {
					this.buildFixedRoles(attrs, runJobVO.getRoleUserId(), flowRuleVO);
				} else {
					this.buildCondGroups(attrs, runJobVO.getCondGroups(), flowRuleVO);
				}
				return res;
			}).collect(Collectors.toList());
			runFlowVO.getNodeList().addAll(runJobVOS);
		}
		List<TabsOption> tabsOptions = tabsOptionService.list();
		// 处理流程节点属性
		List<RunNode> runNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
		if (CollUtil.isNotEmpty(runNodes)) {
			List<RunNodeVO> runNodeVOS = runNodes.stream().map(m -> {
				RunNodeVO res = new RunNodeVO();
				RunNodeVO runNodeVO = new RunNodeVO();
				BeanUtil.copyProperties(m, runNodeVO);
				// 交换ID
				runNodeVO.setRunNodeId(runNodeVO.getId());
				runNodeVO.setId(runNodeVO.getFlowNodeId());
				res.setType(NodeTypeEnum.getEnumByNodeType(runNodeVO.getNodeType()).getNodeType());
				res.setAttrs(runNodeVO);
				// 处理默认节点任务（构造配置任务）
				Optional<RunJob> existRunJob = defRunJobs.stream().filter(f -> FlowCommonConstants.YES.equals(f.getIsConfigJob())
						&& f.getFlowNodeId().equals(runNodeVO.getId())).findAny();
				existRunJob.ifPresent(runJob -> {
					RunJobVO runJobVO = new RunJobVO();
					BeanUtil.copyProperties(runJob, runJobVO);
					// 交换ID
					runJobVO.setRunJobId(runJobVO.getId());
					runJobVO.setId(runJobVO.getNodeJobId());
					if (FlowCommonConstants.YES.equals(isEdit)) {
						List<RunJob> currRunJobs = defRunJobs.stream().filter(f -> f.getNodeJobId().equals(runJobVO.getNodeJobId())).collect(Collectors.toList());
						runJobVO.setCurrRunJobs(currRunJobs);
					}
					res.setDefJob(runJobVO);
					// 处理参数规则
					FlowRuleVO flowRuleVO = new FlowRuleVO();
					flowRuleVO.setRelObjId(runNodeVO.getId());
					flowRuleVO.setValType(runJobVO.getValType());
					flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
					if (DistValTypeEnum.HTTP.getType().equals(runJobVO.getValType())) {
						flowRuleVO.setHttpUrl(runJobVO.getUserKeyVal());
						this.buildFlowRuleParams(attrs, runJobVO.getHttpParams(), flowRuleVO);
					} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(runJobVO.getValType())) {
						this.buildFixedRoles(attrs, runJobVO.getRoleUserId(), flowRuleVO);
					} else {
						this.buildCondGroups(attrs, runJobVO.getCondGroups(), flowRuleVO);
					}
				});
				// 处理节点监听事件
				this.buildFlowClazz(attrs, runNodeVO.getClazzes(), runNodeVO.getId());
				// 处理表单权限
				this.buildSaveFormPerms(attrs, runNodeVO, tabsOptions);
				// 处理子流程Http配置及父子流程参数
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(runNodeVO.getId());
				flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
				flowRuleVO.setType(FlowParamRuleEnum.SUB_FLOW.getType());
				this.buildFlowRuleParams(attrs, runNodeVO.getSubFlowParams(), flowRuleVO);
				// 处理子流程HttpUrl配置
				this.buildSubFlowHttpUrl(attrs, runNodeVO, flowRuleVO);
				return res;
			}).collect(Collectors.toList());
			runFlowVO.getNodeList().addAll(runNodeVOS);
		}
		// 处理节点关系属性
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listAllFlowNodeRels(defFlowId, flowInstId);
		if (CollUtil.isNotEmpty(flowNodeRels)) {
			List<FlowNodeRelVO> flowNodeRelVOS = flowNodeRels.stream().map(m -> {
				FlowNodeRelVO res = new FlowNodeRelVO();
				FlowNodeRelVO flowNodeRelVO = new FlowNodeRelVO();
				BeanUtil.copyProperties(m, flowNodeRelVO);
				flowNodeRelVO.setSourceId(flowNodeRelVO.getFromFlowNodeId());
				if (CommonNbrPool.STR_0.equals(flowNodeRelVO.getType())) {
					flowNodeRelVO.setTargetId(flowNodeRelVO.getToFlowNodeId());
				} else {
					flowNodeRelVO.setTargetId(flowNodeRelVO.getToNodeJobId());
				}
				res.setType(NodeTypeEnum.LINK.getNodeType());
				res.setAttrs(flowNodeRelVO);
				// 处理参数规则
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(flowNodeRelVO.getId());
				flowRuleVO.setValType(flowNodeRelVO.getValType());
				flowRuleVO.setType(FlowParamRuleEnum.LINK.getType());
				if (DistValTypeEnum.HTTP.getType().equals(flowNodeRelVO.getValType())) {
					flowRuleVO.setHttpUrl(flowNodeRelVO.getVarKeyVal());
					this.buildFlowRuleParams(attrs, flowNodeRelVO.getHttpParams(), flowRuleVO);
				} else {
					this.buildCondGroups(attrs, flowNodeRelVO.getCondGroups(), flowRuleVO);
				}
				return res;
			}).collect(Collectors.toList());
			runFlowVO.setLinkList(flowNodeRelVOS);
		}
		// 处理全局监听事件
		this.buildFlowClazz(attrs, attrs.getClazzes(), null);
		// 处理关联表单Http接口
		this.buildFlowFormHttp(attrs);
		return runFlowVO;
	}

	private void buildFlowFormHttp(DefFlowVO defFlow) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		if (StrUtil.isNotBlank(defFlow.getQueryOrder()) || StrUtil.isNotBlank(defFlow.getUpdateOrder())) {
			flowRuleVO.setType(FlowParamRuleEnum.ORDER_PARAMS.getType());
			flowRuleVO.setHttpUrl(defFlow.getQueryOrder());
			this.buildFlowRuleParams(defFlow, defFlow.getOrderParams(), flowRuleVO);
		}
	}

	private void buildSubFlowHttpUrl(DefFlowVO defFlow, FlowNodeVO flowNodeVO, FlowRuleVO flowRuleVO) {
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		flowRuleVO.setType(FlowParamRuleEnum.START_SUB_FLOW.getType());
		List<FlowRule> flowRules = this.buildFlowRuleParams(defFlow, null, flowRuleVO);
		if (CollUtil.isNotEmpty(flowRules)) {
			flowNodeVO.setStartSubFlow(flowRules.get(0).getHttpUrl());
			flowNodeVO.setStartSubMethod(flowRules.get(0).getHttpMethod());
		}
		flowRuleVO.setType(FlowParamRuleEnum.RESTART_SUB_FLOW.getType());
		flowRules = this.buildFlowRuleParams(defFlow, null, flowRuleVO);
		if (CollUtil.isNotEmpty(flowRules)) {
			flowNodeVO.setRestartSubFlow(flowRules.get(0).getHttpUrl());
			flowNodeVO.setRestartSubMethod(flowRules.get(0).getHttpMethod());
		}
		flowRuleVO.setType(FlowParamRuleEnum.BACK_PAR_FLOW.getType());
		flowRules = this.buildFlowRuleParams(defFlow, null, flowRuleVO);
		if (CollUtil.isNotEmpty(flowRules)) {
			flowNodeVO.setBackParFlow(flowRules.get(0).getHttpUrl());
			flowNodeVO.setBackParMethod(flowRules.get(0).getHttpMethod());
		}
	}

	private void buildSaveFormPerms(DefFlowVO defFlow, FlowNodeVO flowNodeVO, List<TabsOption> tabsOptions) {
		FormOption perm = new FormOption();
		perm.setType(CommonNbrPool.STR_1);
		perm.setFlowNodeId(flowNodeVO.getId());
		perm.setDefFlowId(defFlow.getId());
		perm.setFlowInstId(defFlow.getFlowInstId());
		List<FormOption> list = formOptionService.listFormOptions(perm);
		if (CollUtil.isEmpty(list)) return;
		FormOption formOption = list.get(0);
		flowNodeVO.setFormTabsId(formOption.getFormTabsId());
		Optional<TabsOption> any = tabsOptions.stream().filter(f -> f.getId().equals(formOption.getFormId())).findAny();
		if (!any.isPresent()) {
			flowNodeVO.setFormId(formOption.getFormId());
		}
		flowNodeVO.setFormFieldPerms(list);
	}

	private void buildFlowClazz(DefFlowVO defFlow, List<FlowClazzVO> clazzes, Long flowNodeId) {
		FlowClazz flowClazz = new FlowClazz();
		flowClazz.setDefFlowId(defFlow.getId());
		flowClazz.setFlowInstId(defFlow.getFlowInstId());
		if (Objects.nonNull(flowNodeId)) {
			flowClazz.setFlowNodeId(flowNodeId);
			flowClazz.setType(CommonNbrPool.STR_0);
		} else {
			flowClazz.setType(CommonNbrPool.STR_1);
		}
		List<FlowClazz> flowClazzes = flowClazzService.listFlowClazzes(flowClazz);
		if (CollUtil.isEmpty(flowClazzes)) return;
		flowClazzes.forEach(each -> {
			FlowClazzVO flowClazzVO = new FlowClazzVO();
			BeanUtil.copyProperties(each, flowClazzVO);
			clazzes.add(flowClazzVO);
			// 处理Http请求参数
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setHttpUrl(flowClazzVO.getHttpUrl());
			if (Objects.nonNull(flowNodeId)) {
				flowRuleVO.setRelObjId(flowNodeId);
			}
			flowRuleVO.setFlowClazzId(flowClazzVO.getId());
			flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
			flowRuleVO.setType(FlowParamRuleEnum.LISTENER.getType());
			this.buildFlowRuleParams(defFlow, flowClazzVO.getHttpParams(), flowRuleVO);
		});
	}

	private void buildCondGroups(DefFlowVO defFlow, List<FlowRuleVO> condGroups, FlowRuleVO flowRuleVO) {
		Long relObjId = flowRuleVO.getRelObjId();
		String valType = flowRuleVO.getValType();
		String type = flowRuleVO.getType();
		if (!DistValTypeEnum.SIMPLE.getType().equals(valType)) return;
		flowRuleVO.setDefFlowId(defFlow.getId());
		flowRuleVO.setFlowInstId(defFlow.getFlowInstId());
		if(FlowParamRuleEnum.LINK.getType().equals(type)) flowRuleVO.setFlowNodeRelId(relObjId);
		else {
			flowRuleVO.setFlowNodeId(relObjId);
		}
		List<FlowRule> condGroup = flowRuleService.listFlowRules(flowRuleVO);
		if (CollUtil.isNotEmpty(condGroup)) {
			List<FlowRuleVO> params = new ArrayList<>();
			Map<Long, List<FlowRule>> groupIdMaps = condGroup.stream().collect(Collectors.groupingBy(FlowRule::getGroupId));
			groupIdMaps.forEach((key, val) -> {
				FlowRuleVO condGroupsRel = new FlowRuleVO();
				BeanUtil.copyProperties(condGroup.get(0), condGroupsRel);
				condGroupsRel.setCondGroup(val);
				params.add(condGroupsRel);
			});
			condGroups.addAll(params);
		}
	}

	private List<FlowRule> buildFlowRuleParams(DefFlowVO defFlow, List<FlowRule> httpParams, FlowRuleVO flowRuleVO) {
		Long relObjId = flowRuleVO.getRelObjId();
		Long flowClazzId = flowRuleVO.getFlowClazzId();
		String valType = flowRuleVO.getValType();
		String type = flowRuleVO.getType();
		boolean isHttp = DistValTypeEnum.HTTP.getType().equals(valType);
		if (!isHttp) return new ArrayList<>();
		flowRuleVO.setDefFlowId(defFlow.getId());
		flowRuleVO.setFlowInstId(defFlow.getFlowInstId());
		if (FlowParamRuleEnum.LINK.getType().equals(type)) flowRuleVO.setFlowNodeRelId(relObjId);
		else if (FlowParamRuleEnum.LISTENER.getType().equals(type)) {
			flowRuleVO.setFlowNodeId(relObjId);
			flowRuleVO.setFlowClazzId(flowClazzId);
		}
		else flowRuleVO.setFlowNodeId(relObjId);
		List<FlowRule> params = flowRuleService.listFlowRules(flowRuleVO);
		if (Objects.nonNull(httpParams)) httpParams.addAll(params);
		return params;
	}

	private void buildExistFixedRoles(List<FlowRule> fixedParams, List<RunJob> defRunJobs, RunJob runJob) {
		List<RunJob> existRunJobs = defRunJobs.stream().filter(f -> !f.getId().equals(runJob.getId())).collect(Collectors.toList());
		if (CollUtil.isEmpty(existRunJobs)) return;
		existRunJobs.forEach(each -> {
			FlowRule flowRule = new FlowRule();
			flowRule.setRoleId(each.getRoleId());
			flowRule.setJobType(each.getJobType());
			fixedParams.add(flowRule);
		});
	}

	private void buildFixedRoles(DefFlowVO defFlow, List<FlowRule> fixedParams, FlowRuleVO flowRuleVO) {
		Long relObjId = flowRuleVO.getRelObjId();
		String valType = flowRuleVO.getValType();
		if (!DistValTypeEnum.SPEL_FIXED.getType().equals(valType)) return;
		flowRuleVO.setDefFlowId(defFlow.getId());
		flowRuleVO.setFlowInstId(defFlow.getFlowInstId());
		flowRuleVO.setFlowNodeId(relObjId);
		List<FlowRule> flowRules = flowRuleService.listFlowRules(flowRuleVO);
		if (CollUtil.isEmpty(flowRules)) return;
		fixedParams.addAll(flowRules);
	}

	public Boolean saveOrUpdateDefFlow(DefFlowVO defFlowVO) {
		DefFlowVO defFlow = defFlowVO.getAttrs();
		Long defFlowId = defFlowVO.getAttrs().getId();
		String fromType = defFlowVO.getAttrs().getFromType();
		// 流程节点关系属性
		List<FlowNodeRelVO> linkVOList = defFlowVO.getLinkList();
		List<FlowNodeRelVO> linkList = linkVOList.stream().map(FlowNodeRelVO::getAttrs).collect(Collectors.toList());
		List<Long> linkIds = linkList.stream().map(FlowNodeRelVO::getId).collect(Collectors.toList());
		if (CollUtil.isEmpty(linkIds)) throw new ValidationException("连线不能为空");
		flowNodeRelService.removeByDefFlowId(defFlowId, null, linkIds);
		// 流程节点或任务属性
		List<RunNodeVO> nodeList = defFlowVO.getNodeList();
		List<RunNodeVO> flowNodeVOList = nodeList.stream().filter(f -> !NodeTypeEnum.JOB.getNodeType().equals(f.getType())).collect(Collectors.toList());
		List<FlowNodeVO> flowNodeVOS = flowNodeVOList.stream().map(FlowNodeVO::getAttrs).collect(Collectors.toList());
		List<RunNodeVO> nodeJobVOList = nodeList.stream().filter(f -> NodeTypeEnum.JOB.getNodeType().equals(f.getType())).collect(Collectors.toList());
		List<RunJobVO> nodeJobVOS = nodeJobVOList.stream().map(FlowNodeVO::getDefJob).collect(Collectors.toList());
		// 删除流程节点
		List<Long> nodeIds = flowNodeVOS.stream().map(FlowNodeVO::getId).collect(Collectors.toList());
		if (CollUtil.isEmpty(nodeIds)) throw new ValidationException("节点不能为空");
		flowNodeService.remove(Wrappers.<FlowNode>lambdaQuery().notIn(FlowNode::getId, nodeIds).eq(FlowNode::getDefFlowId, defFlowId));
		// 删除节点任务
		List<Long> nodeJobIds = nodeJobVOS.stream().map(NodeJobVO::getId).collect(Collectors.toList());
		List<NodeJob> nodeJobs = nodeJobService.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getDefFlowId, defFlowId));
		List<Long> flowNodeJobIds = nodeJobs.stream().filter(f -> {
				AtomicBoolean existNodeJob = new AtomicBoolean(false);
				Optional<FlowNodeVO> existFlowNode = flowNodeVOS.stream().filter(any -> any.getId().equals(f.getFlowNodeId())).findAny();
				existFlowNode.ifPresent(flowNodeVO -> {
					if (this.validateNotExistNodeJobs(linkList, nodeJobVOList, flowNodeVO, null)) {
						existNodeJob.set(true);
					}
				});
				return existNodeJob.get() || nodeJobIds.contains(f.getId());
			}).map(NodeJob::getId).collect(Collectors.toList());
		if (flowNodeJobIds.size() > 0) nodeJobService.remove(Wrappers.<NodeJob>lambdaQuery().notIn(NodeJob::getId, flowNodeJobIds).eq(NodeJob::getDefFlowId, defFlowId));
		// 删除监听事件
		flowClazzService.removeByDefFlowId(defFlowId, null);
		// 删除权限配置
		formOptionService.removeByDefFlowId(defFlowId, null, CommonNbrPool.STR_1);
		// 删除条件人员规则
		flowRuleService.removeByDefFlowId(defFlowId, null);
		List<TabsOption> tabsOptions = tabsOptionService.list();
		// 处理流程节点属性
		flowNodeVOList.forEach(each -> {
			String type = each.getType();
			RunNodeVO flowNodeVO = each.getAttrs();
			String nodeName = StrUtil.trim(flowNodeVO.getNodeName());
			if (StrUtil.isBlank(nodeName)) throw new ValidationException("节点名称不能为空");
			flowNodeVO.setNodeType(NodeTypeEnum.getEnumByNodeType(type).getType());
			this.handleFlowNodeVOAttr(defFlow, flowNodeVO, fromType);
			if (Objects.nonNull(each.getDefJob()) && this.validateNotExistNodeJobs(linkList, nodeJobVOList, flowNodeVO, flowNodeVO)) {
				// 不存在则设置默认节点任务
				RunJobVO nodeJobVO = each.getDefJob();
				nodeJobVO.setFromType(CommonNbrPool.STR_0);
				// 保存节点任务属性
				nodeJobVO.setJobName(flowNodeVO.getNodeName());
				nodeJobVO.setFlowNodeId(flowNodeVO.getId());
				this.saveOrUpdateJob(defFlow, nodeJobVO);
				// 处理参数规则
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(flowNodeVO.getId());
				flowRuleVO.setValType(nodeJobVO.getValType());
				flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
				if (DistValTypeEnum.HTTP.getType().equals(nodeJobVO.getValType())) {
					flowRuleVO.setHttpUrl(nodeJobVO.getUserKeyVal());
					this.handleFlowRuleParams(defFlow, nodeJobVO.getHttpParams(), flowRuleVO);
				} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(nodeJobVO.getValType())) {
					this.handleFixedRoles(defFlow, nodeJobVO.getRoleUserId(), flowRuleVO);
				} else {
					this.handleCondGroups(defFlow, nodeJobVO.getCondGroups(), flowRuleVO);
				}
			}
			// 处理节点监听事件
			this.handleFlowClazz(defFlow, flowNodeVO.getClazzes(), flowNodeVO.getId());
			// 处理表单权限
			this.handleSaveFormPerms(defFlow, flowNodeVO, tabsOptions);
			// 处理子流程Http配置及父子流程参数
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setRelObjId(flowNodeVO.getId());
			flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
			flowRuleVO.setType(FlowParamRuleEnum.SUB_FLOW.getType());
			this.handleFlowRuleParams(defFlow, flowNodeVO.getSubFlowParams(), flowRuleVO);
			// 处理子流程HttpUrl配置
			this.handleSubFlowHttpUrl(defFlow, flowNodeVO, flowRuleVO);
		});
		// 处理节点任务属性
		nodeJobVOList.forEach(each -> {
			RunJobVO nodeJobVO = each.getDefJob();
			String jobName = StrUtil.trim(nodeJobVO.getJobName());
			if (StrUtil.isBlank(jobName)) throw new ValidationException("任务名称不能为空");
			// 判断是否已有节点ID
			if (Objects.isNull(nodeJobVO.getFlowNodeId())) {
				List<FlowNodeRelVO> nodeJobRelVOS = linkList.stream().filter(f -> f.getTargetId().equals(nodeJobVO.getId())).collect(Collectors.toList());
				List<Long> flowNodeIds = nodeJobRelVOS.stream().map(FlowNodeRelVO::getSourceId).collect(Collectors.toList());
				List<FlowNodeVO> existFlowNodes = flowNodeVOS.stream().filter(f -> flowNodeIds.contains(f.getId())).collect(Collectors.toList());
				if (CollUtil.isEmpty(existFlowNodes) || CollUtil.size(existFlowNodes) > 1) throw new ValidationException("任务关联节点为空或存在多个");
				nodeJobVO.setFlowNodeId(existFlowNodes.get(0).getId());
			}
			// 保存节点任务属性
			this.saveOrUpdateJob(defFlow, nodeJobVO);
			// 处理参数规则
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setRelObjId(nodeJobVO.getFlowNodeId());
			flowRuleVO.setValType(nodeJobVO.getValType());
			flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
			if (DistValTypeEnum.HTTP.getType().equals(nodeJobVO.getValType())) {
				flowRuleVO.setHttpUrl(nodeJobVO.getUserKeyVal());
				this.handleFlowRuleParams(defFlow, nodeJobVO.getHttpParams(), flowRuleVO);
			} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(nodeJobVO.getValType())) {
				this.handleFixedRoles(defFlow, nodeJobVO.getRoleUserId(), flowRuleVO);
			} else {
				this.handleCondGroups(defFlow, nodeJobVO.getCondGroups(), flowRuleVO);
			}
		});
		// 处理节点关系属性
		linkVOList.forEach(each -> {
			FlowNodeRelVO flowNodeRelVO = each.getAttrs();
			flowNodeRelVO.setFromFlowNodeId(flowNodeRelVO.getSourceId());
			this.handleFlowNodeRel(defFlow, linkList, flowNodeVOList, nodeJobVOS, flowNodeRelVO);
			// 处理参数规则
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setRelObjId(flowNodeRelVO.getId());
			flowRuleVO.setValType(flowNodeRelVO.getValType());
			flowRuleVO.setType(FlowParamRuleEnum.LINK.getType());
			if (DistValTypeEnum.HTTP.getType().equals(flowNodeRelVO.getValType())) {
				flowRuleVO.setHttpUrl(flowNodeRelVO.getVarKeyVal());
				this.handleFlowRuleParams(defFlow, flowNodeRelVO.getHttpParams(), flowRuleVO);
			} else {
				this.handleCondGroups(defFlow, flowNodeRelVO.getCondGroups(), flowRuleVO);
			}
		});
		// 处理全局监听事件
		this.handleFlowClazz(defFlow, defFlow.getClazzes(), null);
		// 处理关联表单Http接口
		this.handleFlowFormHttp(defFlow);
		return Boolean.TRUE;
	}

	public Boolean saveOrUpdateRunFlow(RunFlowVO runFlowVO) {
		RunFlowVO runFlow = runFlowVO.getAttrs();
		Long defFlowId = runFlowVO.getAttrs().getId();
		Long flowInstId = runFlowVO.getAttrs().getFlowInstId();
		String fromType = runFlowVO.getAttrs().getFromType();
		// 防止第一次编辑多次点击
		runFlowVO.getLinkList().forEach(each -> {
			Long id = idGenerator.nextId(null).longValue();
			each.getAttrs().setId(id);
		});
		// 流程节点关系属性
		List<FlowNodeRelVO> linkVOList = runFlowVO.getLinkList();
		List<FlowNodeRelVO> linkList = linkVOList.stream().map(FlowNodeRelVO::getAttrs).collect(Collectors.toList());
		// List<Long> linkIds = linkList.stream().map(FlowNodeRelVO::getId).collect(Collectors.toList());
		// if (CollUtil.isEmpty(linkIds)) throw new ValidationException("连线不能为空");
		flowNodeRelService.removeByDefFlowId(defFlowId, flowInstId, null);
		// 流程节点或任务属性
		List<RunNodeVO> nodeList = runFlowVO.getNodeList();
		List<RunNodeVO> runNodeVOList = nodeList.stream().filter(f -> !NodeTypeEnum.JOB.getNodeType().equals(f.getType())).collect(Collectors.toList());
		List<RunNodeVO> runNodeVOS = runNodeVOList.stream().map(RunNodeVO::getAttrs).collect(Collectors.toList());
		List<RunNodeVO> runJobVOList = nodeList.stream().filter(f -> NodeTypeEnum.JOB.getNodeType().equals(f.getType())).collect(Collectors.toList());
		List<RunJobVO> runJobVOS = runJobVOList.stream().map(RunNodeVO::getDefJob).collect(Collectors.toList());
		// 删除流程节点
		List<Long> nodeIds = runNodeVOS.stream().map(RunNodeVO::getId).collect(Collectors.toList());
		if (CollUtil.isEmpty(nodeIds)) throw new ValidationException("节点不能为空");
		runNodeService.remove(Wrappers.<RunNode>lambdaQuery().notIn(RunNode::getId, nodeIds).eq(RunNode::getFlowInstId, flowInstId));
		// 删除节点任务
		List<Long> runJobIds = runJobVOS.stream().map(RunJobVO::getId).collect(Collectors.toList());
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, flowInstId));
		List<Long> flowRunJobIds = runJobs.stream().filter(f -> {
			AtomicBoolean existNodeJob = new AtomicBoolean(false);
			Optional<RunNodeVO> existFlowNode = runNodeVOS.stream().filter(any -> any.getId().equals(f.getFlowNodeId())).findAny();
			existFlowNode.ifPresent(flowNodeVO -> {
				if (this.validateNotExistNodeJobs(linkList, runJobVOList, flowNodeVO, null)) {
					existNodeJob.set(true);
				}
			});
			return existNodeJob.get() || runJobIds.contains(f.getId());
		}).map(RunJob::getId).collect(Collectors.toList());
		if (flowRunJobIds.size() > 0) runJobService.remove(Wrappers.<RunJob>lambdaQuery().notIn(RunJob::getId, flowRunJobIds).eq(RunJob::getFlowInstId, flowInstId));
		// 删除监听事件
		flowClazzService.removeByDefFlowId(defFlowId, flowInstId);
		// 删除权限配置
		formOptionService.removeByDefFlowId(defFlowId, flowInstId, CommonNbrPool.STR_1);
		// 删除条件人员规则
		flowRuleService.removeByDefFlowId(defFlowId, flowInstId);
		List<TabsOption> tabsOptions = tabsOptionService.list();
		// 处理流程节点属性
		runNodeVOList.forEach(each -> {
			String type = each.getType();
			RunNodeVO runNodeVO = each.getAttrs();
			String nodeName = StrUtil.trim(runNodeVO.getNodeName());
			if (StrUtil.isBlank(nodeName)) throw new ValidationException("节点名称不能为空");
			runNodeVO.setNodeType(NodeTypeEnum.getEnumByNodeType(type).getType());
			this.handleFlowNodeVOAttr(runFlow, runNodeVO, fromType);
			if (Objects.nonNull(each.getDefJob()) && this.validateNotExistNodeJobs(linkList, runJobVOList, runNodeVO, runNodeVO)) {
				// 不存在则设置默认节点任务
				RunJobVO runJobVO = each.getDefJob();
				runJobVO.setFromType(CommonNbrPool.STR_0);
				// 保存节点任务属性
				runJobVO.setJobName(runNodeVO.getNodeName());
				runJobVO.setFlowNodeId(runNodeVO.getId());
				// 运行节点ID
				runJobVO.setRunNodeId(runNodeVO.getRunNodeId());
				this.saveOrUpdateJob(runFlow, runJobVO);
				// 处理参数规则
				FlowRuleVO flowRuleVO = new FlowRuleVO();
				flowRuleVO.setRelObjId(runNodeVO.getId());
				flowRuleVO.setValType(runJobVO.getValType());
				flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
				if (DistValTypeEnum.HTTP.getType().equals(runJobVO.getValType())) {
					flowRuleVO.setHttpUrl(runJobVO.getUserKeyVal());
					this.handleFlowRuleParams(runFlow, runJobVO.getHttpParams(), flowRuleVO);
				} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(runJobVO.getValType())) {
					this.handleFixedRoles(runFlow, runJobVO.getRoleUserId(), flowRuleVO);
				} else {
					this.handleCondGroups(runFlow, runJobVO.getCondGroups(), flowRuleVO);
				}
			}
			// 处理节点监听事件
			this.handleFlowClazz(runFlow, runNodeVO.getClazzes(), runNodeVO.getId());
			// 处理表单权限
			this.handleSaveFormPerms(runFlow, runNodeVO, tabsOptions);
			// 处理子流程Http配置及父子流程参数
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setRelObjId(runNodeVO.getId());
			flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
			flowRuleVO.setType(FlowParamRuleEnum.SUB_FLOW.getType());
			this.handleFlowRuleParams(runFlow, runNodeVO.getSubFlowParams(), flowRuleVO);
			// 处理子流程HttpUrl配置
			this.handleSubFlowHttpUrl(runFlow, runNodeVO, flowRuleVO);
		});
		// 处理节点任务属性
		runJobVOList.forEach(each -> {
			RunJobVO runJobVO = each.getDefJob();
			String jobName = StrUtil.trim(runJobVO.getJobName());
			if (StrUtil.isBlank(jobName)) throw new ValidationException("任务名称不能为空");
			// 判断是否已有节点ID
			if (Objects.isNull(runJobVO.getFlowNodeId())) {
				List<FlowNodeRelVO> runJobRelVOS = linkList.stream().filter(f -> f.getTargetId().equals(runJobVO.getId())).collect(Collectors.toList());
				List<Long> runNodeIds = runJobRelVOS.stream().map(FlowNodeRelVO::getSourceId).collect(Collectors.toList());
				List<RunNodeVO> existRunNodes = runNodeVOS.stream().filter(f -> runNodeIds.contains(f.getId())).collect(Collectors.toList());
				if (CollUtil.isEmpty(existRunNodes) || CollUtil.size(existRunNodes) > 1) throw new ValidationException("任务关联节点为空或存在多个");
				runJobVO.setFlowNodeId(existRunNodes.get(0).getId());
			}
			// 运行节点ID
			List<RunNodeVO> existRunNodes = runNodeVOList.stream().filter(f -> f.getAttrs().getFlowNodeId().equals(runJobVO.getFlowNodeId())).collect(Collectors.toList());
			if (CollUtil.isEmpty(existRunNodes) || CollUtil.size(existRunNodes) > 1) throw new ValidationException("任务关联节点为空或存在多个");
			runJobVO.setRunNodeId(existRunNodes.get(0).getAttrs().getRunNodeId());
			// 保存节点任务属性
			this.saveOrUpdateJob(runFlow, runJobVO);
			// 处理参数规则
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setRelObjId(runJobVO.getFlowNodeId());
			flowRuleVO.setValType(runJobVO.getValType());
			flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
			if (DistValTypeEnum.HTTP.getType().equals(runJobVO.getValType())) {
				flowRuleVO.setHttpUrl(runJobVO.getUserKeyVal());
				this.handleFlowRuleParams(runFlow, runJobVO.getHttpParams(), flowRuleVO);
			} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(runJobVO.getValType())) {
				this.handleFixedRoles(runFlow, runJobVO.getRoleUserId(), flowRuleVO);
			} else {
				this.handleCondGroups(runFlow, runJobVO.getCondGroups(), flowRuleVO);
			}
		});
		// 处理节点关系属性
		linkVOList.forEach(each -> {
			FlowNodeRelVO flowNodeRelVO = each.getAttrs();
			flowNodeRelVO.setFromFlowNodeId(flowNodeRelVO.getSourceId());
			this.handleFlowNodeRel(runFlow, linkList, runNodeVOList, runJobVOS, flowNodeRelVO);
			// 处理参数规则
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setRelObjId(flowNodeRelVO.getId());
			flowRuleVO.setValType(flowNodeRelVO.getValType());
			flowRuleVO.setType(FlowParamRuleEnum.LINK.getType());
			if (DistValTypeEnum.HTTP.getType().equals(flowNodeRelVO.getValType())) {
				flowRuleVO.setHttpUrl(flowNodeRelVO.getVarKeyVal());
				this.handleFlowRuleParams(runFlow, flowNodeRelVO.getHttpParams(), flowRuleVO);
			} else {
				this.handleCondGroups(runFlow, flowNodeRelVO.getCondGroups(), flowRuleVO);
			}
		});
		// 处理全局监听事件
		this.handleFlowClazz(runFlow, runFlow.getClazzes(), null);
		// 处理关联表单Http接口
		this.handleFlowFormHttp(runFlow);
		return Boolean.TRUE;
	}

	@Override
	public void handleIndependent(RunFlowVO runFlowVO) {
		// 流程参数规则
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(runFlowVO.getDefFlowId());
		List<FlowRule> newFlowRules = new ArrayList<>();
		List<FlowRule> flowRules = flowRuleService.listFlowRules(flowRuleVO);
		// 节点关系记录
		List<FlowNodeRel> newFlowNodeRels = new ArrayList<>();
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listAllFlowNodeRels(runFlowVO.getDefFlowId(), null);
		flowNodeRels.forEach(each -> {
			FlowNodeRel flowNodeRel = new FlowNodeRel();
			BeanUtil.copyProperties(each, flowNodeRel);
			Long id = idGenerator.nextId(null).longValue();
			flowRules.forEach(each2 -> {
				if (!FlowParamRuleEnum.LINK.getType().equals(each2.getType())) return;
				if (flowNodeRel.getId().equals(each2.getFlowNodeRelId())) each2.setFlowNodeRelId(id);
			});
			flowNodeRel.setId(id);
			flowNodeRel.setFlowInstId(runFlowVO.getId());
			newFlowNodeRels.add(flowNodeRel);
		});
		flowNodeRelService.saveBatch(newFlowNodeRels);
		// 流程监听事件
		List<FlowClazz> newFlowClazzes = new ArrayList<>();
		List<FlowClazz> flowClazzes = flowClazzService.listFlowClazzes(runFlowVO.getDefFlowId(), null);
		if (CollUtil.isNotEmpty(flowClazzes)) {
			flowClazzes.forEach(each -> {
				FlowClazz flowClazz = new FlowClazz();
				BeanUtil.copyProperties(each, flowClazz);
				Long id = idGenerator.nextId(null).longValue();
				flowRules.forEach(each2 -> {
					if (!FlowParamRuleEnum.LISTENER.getType().equals(each2.getType())) return;
					if (flowClazz.getId().equals(each2.getFlowClazzId())) each2.setFlowClazzId(id);
				});
				flowClazz.setId(id);
				flowClazz.setFlowInstId(runFlowVO.getId());
				newFlowClazzes.add(flowClazz);
			});
			flowClazzService.saveBatch(newFlowClazzes);
		}
		// 更新节点关系ID、监听事件ID
		if (CollUtil.isNotEmpty(flowRules)) {
			flowRules.forEach(each -> {
				FlowRule flowRule = new FlowRule();
				BeanUtil.copyProperties(each, flowRule);
				flowRule.setId(null);
				flowRule.setFlowInstId(runFlowVO.getId());
				newFlowRules.add(flowRule);
			});
			flowRuleService.saveBatch(newFlowRules);
		}
		// 流程表单权限设计
		FormOption perm = new FormOption();
		perm.setDefFlowId(runFlowVO.getDefFlowId());
		List<FormOption> newFormOptions = new ArrayList<>();
		List<FormOption> formOptions = formOptionService.listFormOptions(perm);
		if (CollUtil.isNotEmpty(formOptions)) {
			formOptions.forEach(each -> {
				FormOption formOption = new FormOption();
				BeanUtil.copyProperties(each, formOption);
				formOption.setId(null);
				formOption.setFlowInstId(runFlowVO.getId());
				newFormOptions.add(formOption);
			});
			formOptionService.saveBatch(newFormOptions);
		}
	}

	private void handleFlowFormHttp(DefFlowVO defFlow) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		if (StrUtil.isNotBlank(defFlow.getQueryOrder()) || StrUtil.isNotBlank(defFlow.getUpdateOrder())) {
			flowRuleVO.setType(FlowParamRuleEnum.ORDER_PARAMS.getType());
			flowRuleVO.setHttpUrl(defFlow.getQueryOrder());
			this.handleFlowRuleParams(defFlow, defFlow.getOrderParams(), flowRuleVO);
		}
	}

	private void handleSubFlowHttpUrl(DefFlowVO defFlow, FlowNodeVO flowNodeVO, FlowRuleVO flowRuleVO) {
		FlowRule flowRule = new FlowRule();
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		if (StrUtil.isNotBlank(flowNodeVO.getStartSubFlow())) {
			flowRuleVO.setType(FlowParamRuleEnum.START_SUB_FLOW.getType());
			flowRuleVO.setHttpUrl(flowNodeVO.getStartSubFlow());
			// 保存值
			flowRule.setHttpUrl(flowNodeVO.getStartSubFlow());
			flowRule.setHttpMethod(flowNodeVO.getStartSubMethod());
			this.handleFlowRuleParams(defFlow, CollUtil.newArrayList(flowRule), flowRuleVO);
		}
		if (StrUtil.isNotBlank(flowNodeVO.getRestartSubFlow())) {
			flowRuleVO.setType(FlowParamRuleEnum.RESTART_SUB_FLOW.getType());
			flowRuleVO.setHttpUrl(flowNodeVO.getRestartSubFlow());
			// 保存值
			flowRule.setHttpUrl(flowNodeVO.getRestartSubFlow());
			flowRule.setHttpMethod(flowNodeVO.getRestartSubMethod());
			flowRule.setId(null);
			this.handleFlowRuleParams(defFlow, CollUtil.newArrayList(flowRule), flowRuleVO);
		}
		if (StrUtil.isNotBlank(flowNodeVO.getBackParFlow())) {
			flowRuleVO.setType(FlowParamRuleEnum.BACK_PAR_FLOW.getType());
			flowRuleVO.setHttpUrl(flowNodeVO.getBackParFlow());
			// 保存值
			flowRule.setHttpUrl(flowNodeVO.getBackParFlow());
			flowRule.setHttpMethod(flowNodeVO.getBackParMethod());
			flowRule.setId(null);
			this.handleFlowRuleParams(defFlow, CollUtil.newArrayList(flowRule), flowRuleVO);
		}
	}

	private void handleFlowRuleParams(DefFlowVO defFlow, List<FlowRule> flowRuleParams, FlowRuleVO flowRuleVO) {
		String httpUrl = flowRuleVO.getHttpUrl();
		Long relObjId = flowRuleVO.getRelObjId();
		Long flowClazzId = flowRuleVO.getFlowClazzId();
		String valType = flowRuleVO.getValType();
		String type = flowRuleVO.getType();
		boolean isHttp = DistValTypeEnum.HTTP.getType().equals(valType);
		if (!isHttp) return;
		if (!FlowParamRuleEnum.SUB_FLOW.getType().equals(type)) {
			if (StrUtil.isBlank(httpUrl)) return;
		}
		if (CollUtil.isEmpty(flowRuleParams)) return;
		List<FlowRule> flowRules = new ArrayList<>();
		flowRuleParams.forEach(flowRule -> {
			flowRule.setId(null);
			flowRule.setDefFlowId(defFlow.getId());
			flowRule.setFlowInstId(defFlow.getFlowInstId());
			flowRule.setFlowKey(defFlow.getFlowKey());
			flowRule.setValType(valType);
			if (CommonNbrPool.STR_0.equals(flowRule.getParamValType())) {
				if (StrUtil.isBlank(flowRule.getTargetProp())) flowRule.setTargetProp(flowRule.getVarKeyVal());
			}
			flowRule.setType(type);
			if (FlowParamRuleEnum.LINK.getType().equals(type)) flowRule.setFlowNodeRelId(relObjId);
			else if (FlowParamRuleEnum.LISTENER.getType().equals(type)) {
				flowRule.setFlowNodeId(relObjId);
				flowRule.setFlowClazzId(flowClazzId);
			}
			else flowRule.setFlowNodeId(relObjId);
			flowRules.add(flowRule);
		});
		flowRuleService.saveBatch(flowRules);
	}

	private void handleCondGroups(DefFlowVO defFlow, List<FlowRuleVO> condGroups, FlowRuleVO flowRuleVO) {
		Long relObjId = flowRuleVO.getRelObjId();
		String valType = flowRuleVO.getValType();
		String type = flowRuleVO.getType();
		if (!DistValTypeEnum.SIMPLE.getType().equals(valType)) return;
		if (CollUtil.isEmpty(condGroups)) return;
		List<FlowRule> flowRules = new ArrayList<>();
		condGroups.forEach(condGroupsRel -> {
			condGroupsRel.setGroupId(idGenerator.nextId(null).longValue());
			List<FlowRule> condGroup = condGroupsRel.getCondGroup();
			condGroup.forEach(condition -> {
				condition.setId(null);
				condition.setDefFlowId(defFlow.getId());
				condition.setFlowInstId(defFlow.getFlowInstId());
				condition.setFlowKey(defFlow.getFlowKey());
				condition.setValType(condGroupsRel.getValType());
				condition.setGroupId(condGroupsRel.getGroupId());
				condition.setGroupsType(condGroupsRel.getGroupsType());
				condition.setGroupType(condGroupsRel.getGroupType());
				FlowRule flowRule = new FlowRule();
				BeanUtil.copyProperties(condition, flowRule);
				flowRule.setType(type);
				if(FlowParamRuleEnum.LINK.getType().equals(type)) flowRule.setFlowNodeRelId(relObjId);
				else {
					flowRule.setFlowNodeId(relObjId);
					flowRule.setRoleId(condGroupsRel.getRoleId());
					flowRule.setJobType(condGroupsRel.getJobType());
				}
				flowRules.add(flowRule);
			});
		});
		flowRuleService.saveBatch(flowRules);
	}

	private void handleFixedRoles(DefFlowVO defFlow, List<FlowRule> FixedParams, FlowRuleVO flowRuleVO) {
		Long relObjId = flowRuleVO.getRelObjId();
		String valType = flowRuleVO.getValType();
		String type = flowRuleVO.getType();
		if (!DistValTypeEnum.SPEL_FIXED.getType().equals(valType)) return;
		if (CollUtil.isEmpty(FixedParams)) return;
		List<FlowRule> flowRules = new ArrayList<>();
		FixedParams.forEach(flowRule -> {
			flowRule.setId(null);
			flowRule.setDefFlowId(defFlow.getId());
			flowRule.setFlowInstId(defFlow.getFlowInstId());
			flowRule.setFlowKey(defFlow.getFlowKey());
			flowRule.setValType(valType);
			flowRule.setType(type);
			flowRule.setFlowNodeId(relObjId);
			flowRules.add(flowRule);
		});
		flowRuleService.saveBatch(flowRules);
	}

	private void handleFlowNodeRel(DefFlowVO defFlow, List<FlowNodeRelVO> linkList, List<RunNodeVO> runNodeVOList, List<RunJobVO> runJobVOS, FlowNodeRelVO flowNodeRelVO) {
		flowNodeRelVO.setDefFlowId(defFlow.getId());
		flowNodeRelVO.setFlowInstId(defFlow.getFlowInstId());
		flowNodeRelVO.setFlowKey(defFlow.getFlowKey());
		Optional<RunNodeVO> runNodeVO = runNodeVOList.stream().filter(f -> f.getAttrs().getId().equals(flowNodeRelVO.getSourceId())).findAny();
		if (!runNodeVO.isPresent()) throw new ValidationException("源节点不能为空");
		flowNodeRelVO.setFromNodeType(NodeTypeEnum.getEnumByNodeType(runNodeVO.get().getType()).getType());
		// 判断是否为关联的任务
		Optional<RunJobVO> existRunJob = runJobVOS.stream().filter(f -> f.getId().equals(flowNodeRelVO.getTargetId())).findAny();
		if (!existRunJob.isPresent()) {
			Optional<RunNodeVO> targetFlowNodeVO = runNodeVOList.stream().filter(f -> f.getAttrs().getId().equals(flowNodeRelVO.getTargetId())).findAny();
			if (!targetFlowNodeVO.isPresent()) throw new ValidationException("目标节点不能为空");
			flowNodeRelVO.setToFlowNodeId(flowNodeRelVO.getTargetId());
			flowNodeRelVO.setToNodeType(NodeTypeEnum.getEnumByNodeType(targetFlowNodeVO.get().getType()).getType());
			flowNodeRelVO.setType(CommonNbrPool.STR_0);
		} else {
			flowNodeRelVO.setToNodeJobId(existRunJob.get().getId());
			flowNodeRelVO.setType(CommonNbrPool.STR_1);
		}
		flowNodeRelService.saveOrUpdate(flowNodeRelVO, linkList, runJobVOS);
	}

	private void handleFlowNodeVOAttr(DefFlowVO defFlow, RunNodeVO runNodeVO, String fromType) {
		runNodeVO.setDefFlowId(defFlow.getId());
		runNodeVO.setFlowKey(defFlow.getFlowKey());
		if (Objects.nonNull(defFlow.getFlowInstId())) {
			runNodeVO.setFlowInstId(defFlow.getFlowInstId());
			Long runNodeId = runNodeVO.getRunNodeId();
			if (Objects.isNull(runNodeId)) {
				runNodeVO.setRunNodeId(runNodeVO.getId());
				runNodeVO.setFlowNodeId(runNodeVO.getId());
			} else runNodeVO.setId(runNodeId);
			runNodeService.saveOrUpdate(runNodeVO, fromType);
			// 还原ID
			runNodeVO.setRunNodeId(runNodeVO.getId());
			runNodeVO.setId(runNodeVO.getFlowNodeId());
		} else {
			flowNodeService.saveOrUpdate(runNodeVO, fromType);
		}
	}

	/**
	 * 判断是否没有单独配置节点任务
	 *
	 * @param linkList 连线
	 * @param nodeJobVOList 节点任务
	 * @param attrs 当前节点
	 * @return boolean
	 */
	private boolean validateNotExistNodeJobs(List<FlowNodeRelVO> linkList, List<RunNodeVO> nodeJobVOList, FlowNodeVO attrs, FlowNodeVO sequential) {
		if (FlowCommonConstants.YES.equals(attrs.getIsGateway())) return false;
		// if (FlowCommonConstants.YES.equals(attrs.getIsAutoAudit())) return false;
		List<FlowNodeRelVO> flowNodeRelVOS = linkList.stream().filter(f -> f.getSourceId().equals(attrs.getId())).collect(Collectors.toList());
		List<Long> targetJobIds = flowNodeRelVOS.stream().map(FlowNodeRelVO::getTargetId).collect(Collectors.toList());
		List<FlowNodeVO> existNodeJobs = nodeJobVOList.stream().filter(f -> (targetJobIds.contains(f.getDefJob().getId()) && NodeTypeEnum.JOB.getNodeType().equals(f.getType()))
				// 不分离任务不再有连线关系
				|| attrs.getId().equals(f.getDefJob().getFlowNodeId())).collect(Collectors.toList());
		// 允许顺序签存在相同sort的job
		// List<NodeJobVO> nodeJobVOS = existNodeJobs.stream().map(FlowNodeVO::getDefJob).collect(Collectors.toList());
		// if (Objects.nonNull(sequential)) this.handleSequentialJob(sequential, nodeJobVOS);
		return CollUtil.isEmpty(existNodeJobs);
	}

	private void handleSequentialJob(FlowNodeVO flowNodeVO, List<NodeJobVO> nodeJobVOS) {
		if (CollUtil.size(nodeJobVOS) <= 1 || !JobApproveMethodEnum.SEQUENTIAL.getType().equals(flowNodeVO.getApproveMethod())) return;
		if (NodeTypeEnum.SERIAL.getType().equals(flowNodeVO.getNodeType()) || NodeTypeEnum.PARALLEL.getType().equals(flowNodeVO.getNodeType())) {
			Map<Integer, List<NodeJobVO>> groups = nodeJobVOS.stream().collect(Collectors.groupingBy(NodeJobVO::getSort));
			groups.forEach((each, list) -> {
				if (CollUtil.size(list) > 1) throw new ValidationException("多人审批方式【依次审批】的节点【" + flowNodeVO.getNodeName() + "】下的任务排序值不能存在相同值");
			});
		}
	}

	private void handleSaveFormPerms(DefFlowVO defFlow, FlowNodeVO attrs, List<TabsOption> tabsOptions) {
		Long formTabsId = attrs.getFormTabsId();
		List<FormOption> formFieldPerms = attrs.getFormFieldPerms();
		if (Objects.isNull(formTabsId) || CollUtil.isEmpty(formFieldPerms)) return;
		// 未配置权限不保存
		formFieldPerms = formFieldPerms.stream().filter(f -> StrUtil.isNotBlank(f.getPermType())).collect(Collectors.toList());
		if (CollUtil.isEmpty(formFieldPerms)) return;
		Optional<TabsOption> any = tabsOptions.stream().filter(f -> f.getId().equals(formTabsId)).findAny();
		TabsOption exist = any.orElseThrow(() -> new ValidationException("节点【" + attrs.getNodeName() + "】表单权限选择的表单不存在"));
		TabsOption tabsOption = new TabsOption();
		BeanUtil.copyProperties(exist, tabsOption);
		if (Objects.nonNull(attrs.getFormId())) {
			tabsOption.setId(attrs.getFormId());
			tabsOption.setType(CommonNbrPool.STR_1);
			tabsOption.setLabel(null);
			tabsOption.setPath(null);
		}
		formFieldPerms.forEach(perm -> {
			perm.setId(null);
			perm.setType(CommonNbrPool.STR_1);
			perm.setFormTabsId(formTabsId);
			perm.setFormType(tabsOption.getType());
			perm.setFormId(tabsOption.getId());
			perm.setFormName(tabsOption.getLabel());
			perm.setPath(tabsOption.getPath());
			perm.setDefFlowId(defFlow.getId());
			perm.setFlowInstId(defFlow.getFlowInstId());
			perm.setFlowKey(defFlow.getFlowKey());
			perm.setFlowNodeId(attrs.getId());
		});
		formOptionService.saveBatch(formFieldPerms);
	}

	private void handleFlowClazz(DefFlowVO defFlow, List<FlowClazzVO> clazzes, Long flowNodeId) {
		if (CollUtil.isEmpty(clazzes)) return;
		clazzes.forEach(flowClazzVO ->{
			String clazzName = StrUtil.trim(flowClazzVO.getClazz());
			String httpUrl = StrUtil.trim(flowClazzVO.getHttpUrl());
			if (StrUtil.isBlank(clazzName) && StrUtil.isBlank(httpUrl)) return;
			flowClazzVO.setClazz(clazzName);
			flowClazzVO.setHttpUrl(httpUrl);
			flowClazzVO.setDefFlowId(defFlow.getId());
			flowClazzVO.setFlowInstId(defFlow.getFlowInstId());
			flowClazzVO.setFlowKey(defFlow.getFlowKey());
			if (Objects.nonNull(flowNodeId)) {
				flowClazzVO.setFlowNodeId(flowNodeId);
				flowClazzVO.setType(CommonNbrPool.STR_0);
			} else {
				flowClazzVO.setType(CommonNbrPool.STR_1);
			}
			flowClazzVO.setId(null);
			flowClazzService.saveOrUpdate(flowClazzVO);
			// 处理Http请求参数
			FlowRuleVO flowRuleVO = new FlowRuleVO();
			flowRuleVO.setHttpUrl(flowClazzVO.getHttpUrl());
			if (Objects.nonNull(flowNodeId)) {
				flowRuleVO.setRelObjId(flowNodeId);
			}
			flowRuleVO.setFlowClazzId(flowClazzVO.getId());
			flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
			flowRuleVO.setType(FlowParamRuleEnum.LISTENER.getType());
			this.handleFlowRuleParams(defFlow, flowClazzVO.getHttpParams(), flowRuleVO);
		});
	}

	private void saveOrUpdateJob(DefFlowVO defFlow, RunJobVO runJobVO) {
		runJobVO.setDefFlowId(defFlow.getId());
		runJobVO.setFlowKey(defFlow.getFlowKey());
		if (Objects.nonNull(defFlow.getFlowInstId())) {
			runJobVO.setFlowInstId(defFlow.getFlowInstId());
			runJobVO.setIsConfigJob(FlowCommonConstants.YES);
			Long runJobId = runJobVO.getRunJobId();
			if (Objects.isNull(runJobId)) {
				Long id = idGenerator.nextId(null).longValue();
				runJobVO.setId(id);
				runJobVO.setRunJobId(runJobVO.getId());
				runJobVO.setNodeJobId(runJobVO.getId());
			} else runJobVO.setId(runJobId);
			runJobService.saveOrUpdate(runJobVO);
			// 还原ID
			runJobVO.setRunJobId(runJobVO.getId());
			runJobVO.setId(runJobVO.getNodeJobId());
		} else {
			nodeJobService.saveOrUpdate(runJobVO);
		}
	}

	@Override
	public RunJobVO recordRunJobs(List<RunNode> runNodes) {
		// 查询所有节点及配置
		Long defFlowId = runNodes.get(0).getDefFlowId();
		Long createUser = runNodes.get(0).getCreateUser();
		List<NodeJob> nodeJobs = nodeJobService.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getDefFlowId, defFlowId));
		List<RunJob> runJobs = new ArrayList<>();
		AtomicReference<RunNodeVO> runNodeVOAtomic = new AtomicReference<>();
		runNodes.forEach(runNode -> {
			List<NodeJob> existNodeJobs = nodeJobs.stream().filter(f -> f.getFlowNodeId().equals(runNode.getFlowNodeId())).collect(Collectors.toList());
			existNodeJobs.forEach(nodeJob -> {
				RunJob runJob = new RunJob();
				BeanUtil.copyProperties(nodeJob, runJob, FlowEntityInfoConstants.ID);
				runJob.setIsConfigJob(FlowCommonConstants.YES);
				runJob.setNodeJobId(nodeJob.getId());
				runJob.setFlowInstId(runNode.getFlowInstId());
				runJob.setRunNodeId(runNode.getId());
				runJob.setStatus(runNode.getStatus());
				runJob.setCreateTime(LocalDateTime.now());
				runJob.setCreateUser(createUser);
				// 发起人审批节点 个人任务（优先） 组任务
				if (NodeTypeEnum.START.getType().equals(runNode.getNodeType())) {
					runJob.setStartTime(runNode.getStartTime());
					this.extractedUserRoleId(createUser, runJob);
					// 处理返回 获取runJob ID
					RunNodeVO runNodeVO = new RunNodeVO();
					BeanUtil.copyProperties(runNode, runNodeVO);
					runNodeVOAtomic.set(runNodeVO);
				}
				// 虚拟节点设置发起人自己
				if (NodeTypeEnum.VIRTUAL.getType().equals(runNode.getNodeType())) {
					this.extractedUserRoleId(createUser, runJob);
				}
				// 判断是否默认是发起人自己
				this.initCurrUserRoleId(runJob);
				runJobs.add(runJob);
			});
		});
		log.info("当前流程审批共 {} 人", runJobs.size());
		runJobs.forEach(runJobService::save);
		// baseMapper.insertBatchSomeColumn(runJobs);
		// 处理返回 获取runJob ID
		RunNodeVO runNodeVO = runNodeVOAtomic.get();
		Optional<RunJob> any = runJobs.stream().filter(f -> runNodeVO.getId().equals(f.getRunNodeId())).findAny();
		RunJob runJob = any.orElseThrow(() -> new ValidationException("开始节点不存在节点ID"));
		RunJobVO runJobVO = new RunJobVO();
		BeanUtil.copyProperties(runJob, runJobVO);
		runJobVO.setRunNodeVO(runNodeVO);
		return runJobVO;
	}

	private void extractedUserRoleId(Long createUser, RunJob runJob) {
		runJob.setUserId(createUser);
		runJob.setRoleId(createUser);
		runJob.setJobType(JobUserTypeEnum.USER.getType());
	}

	private void initCurrUserRoleId(RunJob runJob) {
		if (!FlowCommonConstants.IS_ENABLED.equals(flowCommonProperties.getIsAllowInitSelf())) {
			return;
		}
		Long createUser = SecurityUtils.getUser().getId();
		boolean notExistUser = Objects.isNull(runJob.getUserId()) && Objects.isNull(runJob.getRoleId());
		boolean notExistKey = StrUtil.isBlank(runJob.getUserKey()) && StrUtil.isBlank(runJob.getUserKeyVal());
		if (notExistUser && notExistKey && JobUserTypeEnum.NONE.getType().equals(runJob.getJobType())) {
			runJob.setUserId(createUser);
			runJob.setRoleId(createUser);
			runJob.setJobType(JobUserTypeEnum.USER.getType());
		}
	}

	@Override
	public boolean handleSignatureType(RunJobVO runJobVO) {
		List<RunJob> currRunJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runJobVO.getRunNodeId()));
		// 处理加签类型
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		String approveMethod = runNodeVO.getApproveMethod();
		String signedType = runJobVO.getSignedType();
		Optional<RunJob> currRunJob = currRunJobs.stream().filter(f -> f.getId().equals(runJobVO.getId())).findAny();
		RunJob runJob = currRunJob.orElseThrow(() -> new ValidationException("当前任务不存在"));
		// 加签关联前置任务ID
		RunJob newRunJob = runJobService.resetRunJob(runJob, signedType);
		// 指定加签审批参与者
		DistPerson nextUserRole = runJobVO.getNextUserRole();
		runJobService.resetByDistPerson(nextUserRole, newRunJob);
		newRunJob.setBelongType(runJobVO.getBelongType());
		if (JobSignatureTypeEnum.BEFORE_SIGNATURE.getType().equals(signedType)) {
			this.handleBeforeSignature(currRunJobs, approveMethod, runJob, newRunJob, true);
		} else if (JobSignatureTypeEnum.AFTER_SIGNATURE.getType().equals(signedType)) {
			this.handleAfterSignature(runJobVO, currRunJobs, approveMethod, runJob, newRunJob, true);
		}
		this.saveNotifySignatureType(runJobVO, newRunJob, true, false);
		return true;
	}

	private void handleBeforeSignature(List<RunJob> currRunJobs, String approveMethod, RunJob runJob, RunJob newRunJob, boolean isSort) {
		if (JobApproveMethodEnum.ALL_AUDIT.getType().equals(approveMethod)) {
			runJob.setStatus(NodeJobStatusEnum.NO_START.getStatus());
			runJobService.updateById(runJob);
		} else if (JobApproveMethodEnum.ANY_ONE.getType().equals(approveMethod)) {
			currRunJobs.forEach(each ->
					runJobService.lambdaUpdate().set(RunJob::getStatus, NodeJobStatusEnum.NO_START.getStatus())
							.eq(RunJob::getId, each.getId())
							.update()
			);
		} else if (JobApproveMethodEnum.SEQUENTIAL.getType().equals(approveMethod)) {
			runJob.setStatus(NodeJobStatusEnum.NO_START.getStatus());
			runJobService.updateById(runJob);
			if (isSort) newRunJob.setSort(runJob.getSort() - 1);
		}
	}

	private void handleAfterSignature(RunJobVO runJobVO, List<RunJob> currRunJobs, String approveMethod, RunJob runJob, RunJob newRunJob, boolean isSort) {
		this.doCompleteRunJob(runJobVO);
		if (JobApproveMethodEnum.ANY_ONE.getType().equals(approveMethod)) {
			// 或签自动完成现有任务
			List<Long> runJobIds = runJobService.listRunJobsByStatus(currRunJobs);
			runJobService.completeRunJobStatus(null, runJobIds, NodeJobStatusEnum.ACTIVE.getStatus());
		} else if (JobApproveMethodEnum.SEQUENTIAL.getType().equals(approveMethod)) {
			if (isSort) newRunJob.setSort(runJob.getSort() + 1);
		}
	}

	private void saveNotifySignatureType(RunJobVO runJobVO, RunJob newRunJob, boolean isRunJob, boolean isSaveRel) {
		runJobService.save(newRunJob);
		if (isRunJob) flowNodeRelService.handleRunJobFlowNodeRel(newRunJob);
		// 通知待办任务及执行监听
		this.nextNotifyClazzes(runJobVO, CollUtil.newArrayList(newRunJob), true, isSaveRel);
	}

	@Override
	public boolean handleNodeSignatureType(RunJobVO runJobVO) {
		// 判断是否配置数据独立
		RunFlow runFlow = runFlowService.getById(runJobVO.getFlowInstId());
		if (!FlowCommonConstants.YES.equals(runFlow.getIsIndependent())) {
			RunFlowVO runFlowVO = new RunFlowVO();
			BeanUtil.copyProperties(runFlow, runFlowVO);
 			this.handleIndependent(runFlowVO);
			// 更新流程配置
			runFlowService.lambdaUpdate().set(RunFlow::getAutoLayout, FlowCommonConstants.AUTO_LAYOUT)
					.set(RunFlow::getIsIndependent, FlowCommonConstants.YES)
					.eq(RunFlow::getId, runJobVO.getFlowInstId())
					.update();
		}
		// 处理加签节点
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		String nodeSignedType = runJobVO.getNodeSignedType();
		String nodeApproveMethod = runNodeVO.getNodeApproveMethod();
		String signedType = runJobVO.getSignedType();
		RunNode runNode = runNodeService.buildRunNode(runJobVO, runNodeVO);
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listFlowNodeRels(runNodeVO);
		if (NodeSignatureTypeEnum.BEFORE_NODE.getType().equals(nodeSignedType)) {
			List<FlowNodeRel> toFlowNodeRels = flowNodeRels.stream().filter(f -> f.getToFlowNodeId().equals(runNodeVO.getFlowNodeId())).collect(Collectors.toList());
			toFlowNodeRels.forEach(each -> each.setToFlowNodeId(runNode.getFlowNodeId()));
			this.buildFlowNodeRel(toFlowNodeRels.get(0), runNode.getFlowNodeId(), runNode.getNodeType(), runNodeVO.getFlowNodeId(), runNodeVO.getNodeType(), runNodeVO.getFlowInstId());
			flowNodeRelService.saveOrUpdateBatch(toFlowNodeRels);
		} else if (NodeSignatureTypeEnum.AFTER_NODE.getType().equals(nodeSignedType)) {
			List<FlowNodeRel> fromFlowNodeRels = flowNodeRels.stream().filter(f -> f.getFromFlowNodeId().equals(runNodeVO.getFlowNodeId())).collect(Collectors.toList());
			fromFlowNodeRels.forEach(each -> each.setFromFlowNodeId(runNode.getFlowNodeId()));
			this.buildFlowNodeRel(fromFlowNodeRels.get(0), runNodeVO.getFlowNodeId(), runNodeVO.getNodeType(), runNode.getFlowNodeId(), runNode.getNodeType(), runNodeVO.getFlowInstId());
			flowNodeRelService.saveOrUpdateBatch(fromFlowNodeRels);
		} else if (NodeSignatureTypeEnum.SIGN_NODE.getType().equals(nodeSignedType)) {
			if (NodeApproveMethodEnum.SEQUENTIAL.getType().equals(nodeApproveMethod)) {
				if (JobSignatureTypeEnum.BEFORE_SIGNATURE.getType().equals(signedType)) {
					runNode.setSort(runNodeVO.getSort() - 1);
				} else if (JobSignatureTypeEnum.AFTER_SIGNATURE.getType().equals(signedType)) {
					runNode.setSort(runNodeVO.getSort() + 1);
				}
			}
			List<FlowNodeRel> toFlowNodeRels = flowNodeRels.stream().filter(f -> f.getToFlowNodeId().equals(runNodeVO.getFlowNodeId())).collect(Collectors.toList());
			List<FlowNodeRel> fromFlowNodeRels = flowNodeRels.stream().filter(f -> f.getFromFlowNodeId().equals(runNodeVO.getFlowNodeId())).collect(Collectors.toList());
			List<FlowNodeRel> newFlowNodeRels = new ArrayList<>();
			fromFlowNodeRels.forEach(each -> {
				FlowNodeRel flowNodeRel = this.buildFromToFlowNodeRel(runNode, each, true);
				newFlowNodeRels.add(flowNodeRel);
			});
			toFlowNodeRels.forEach(each -> {
				FlowNodeRel flowNodeRel  = this.buildFromToFlowNodeRel(runNode, each, false);
				newFlowNodeRels.add(flowNodeRel);
			});
			flowNodeRelService.saveBatch(newFlowNodeRels);
		}
		List<RunJob> currRunJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runJobVO.getRunNodeId()));
		// 处理加签类型
		String approveMethod = runNodeVO.getApproveMethod();
		Optional<RunJob> currRunJob = currRunJobs.stream().filter(f -> f.getId().equals(runJobVO.getId())).findAny();
		RunJob runJob = currRunJob.orElseThrow(() -> new ValidationException("当前任务不存在"));
		// 指定加签审批参与者
		RunJob newRunJob = runJobService.buildRunJob(runJobVO, runNodeVO, runNode);
		if (NodeSignatureTypeEnum.BEFORE_NODE.getType().equals(nodeSignedType)) {
			runNodeService.lambdaUpdate().set(RunNode::getStatus, NodeJobStatusEnum.NO_START.getStatus())
					.eq(RunNode::getId, runNodeVO.getId())
					.update();
			this.handleBeforeSignature(currRunJobs, approveMethod, runJob, newRunJob, false);
		} else if (NodeSignatureTypeEnum.AFTER_NODE.getType().equals(nodeSignedType)) {
			this.doCompleteRunNode(runJobVO, runNodeVO);
			this.handleAfterSignature(runJobVO, currRunJobs, approveMethod, runJob, newRunJob, false);
		}
		runNodeService.save(runNode);
		this.saveNotifySignatureType(runJobVO, newRunJob, false, true);
		return true;
	}

	private FlowNodeRel buildFromToFlowNodeRel(RunNode runNode, FlowNodeRel each, boolean isFrom) {
		FlowNodeRel flowNodeRel = new FlowNodeRel();
		BeanUtil.copyProperties(each, flowNodeRel);
		flowNodeRel.setVarKeyVal(null);
		flowNodeRel.setValType(null);
		flowNodeRel.setLabel(null);
		flowNodeRel.setHttpMethod(null);
		flowNodeRel.setVertices(null);
		Long id = idGenerator.nextId(null).longValue();
		flowNodeRel.setId(id);
		flowNodeRel.setFlowInstId(runNode.getFlowInstId());
		if (isFrom) {
			flowNodeRel.setFromFlowNodeId(runNode.getFlowNodeId());
			flowNodeRel.setFromNodeType(runNode.getNodeType());
		} else {
			flowNodeRel.setToFlowNodeId(runNode.getFlowNodeId());
			flowNodeRel.setToNodeType(runNode.getNodeType());
		}
		return flowNodeRel;
	}

	private void buildFlowNodeRel(FlowNodeRel each, Long fromFlowNodeId, String fromNodeType, Long toFlowNodeId, String toNodeType, Long flowInstId) {
		FlowNodeRel flowNodeRel = new FlowNodeRel();
		BeanUtil.copyProperties(each, flowNodeRel);
		flowNodeRel.setVarKeyVal(null);
		flowNodeRel.setValType(null);
		flowNodeRel.setLabel(null);
		flowNodeRel.setHttpMethod(null);
		flowNodeRel.setVertices(null);
		Long id = idGenerator.nextId(null).longValue();
		flowNodeRel.setId(id);
		flowNodeRel.setFlowInstId(flowInstId);
		flowNodeRel.setFromFlowNodeId(fromFlowNodeId);
		flowNodeRel.setFromNodeType(fromNodeType);
		flowNodeRel.setToFlowNodeId(toFlowNodeId);
		flowNodeRel.setToNodeType(toNodeType);
		flowNodeRelService.save(flowNodeRel);
	}

	public boolean completeRunJob(RunJobVO runJobVO) {
		if (this.doCompleteRunJob(runJobVO)) return true;
		List<RunJob> currRunJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runJobVO.getRunNodeId()));
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		currRunJobs = this.completeSameRunJobs(runNodeVO, currRunJobs, runJobVO.getId());
		// 处理加签类型
		String signatureType = runJobVO.getSignatureType();
		String approveMethod = runNodeVO.getApproveMethod();
		List<RunJob> runJobs = new ArrayList<>();
		if (JobSignatureTypeEnum.BEFORE_SIGNATURE.getType().equals(signatureType)) {
			this.doBeforeSignature(runJobVO, currRunJobs, approveMethod, runJobs);
			if (CollUtil.isNotEmpty(runJobs)) {
				// 设置办理人及通知待办任务
				this.notifyNextUsers(runJobVO, CollUtil.newArrayList(runJobs), false, false);
				return true;
			}
		}
		// 处理多人审批方式
		if (JobApproveMethodEnum.SEQUENTIAL.getType().equals(approveMethod)) {
			// 是否存在同级加并签正在运行（排除当前任务）
			List<RunJob> signRunJobs = currRunJobs.stream().filter(f -> f.getSort().equals(runJobVO.getSort())
					&& NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus())).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(signRunJobs)) {
				this.notifyNextUsers(runJobVO, signRunJobs, false, false);
				return true;
			}
			// 开启下一顺位的任务
			Optional<RunJob> first = currRunJobs.stream().filter(f -> f.getSort() > runJobVO.getSort()).min(Comparator.comparing(RunJob::getSort));
			if (first.isPresent()) {
				// 处理加并签
				List<RunJob> nextRunJobs = currRunJobs.stream().filter(f -> f.getSort().equals(first.get().getSort())).collect(Collectors.toList());
				this.notifyNextUsers(runJobVO, nextRunJobs, false, false);
				return true;
			}
		}
		// 当前节点任务是否都结束
		if (currRunJobs.stream().allMatch(all -> StrUtil.equalsAny(all.getStatus(), NodeJobStatusEnum.COMPLETE.getStatus(),
				NodeJobStatusEnum.NO_START.getStatus(), NodeJobStatusEnum.SKIP.getStatus()))) {
			runJobVO.setNodeCompleteType(CommonNbrPool.STR_1);
			this.completeRunNode(runJobVO);
			return true;
		}
		// 或签任务则一人审批则完成
		if (JobApproveMethodEnum.ANY_ONE.getType().equals(approveMethod)) {
			List<Long> runJobIds = runJobService.listRunJobsByStatus(currRunJobs);
			runJobService.completeRunJobStatus(null, runJobIds, runJobVO.getSuspension());
			runJobVO.setNodeCompleteType(CommonNbrPool.STR_1);
			this.completeRunNode(runJobVO);
		}
		return true;
	}

	private void doBeforeSignature(RunJobVO runJobVO, List<RunJob> currRunJobs, String approveMethod, List<RunJob> runJobs) {
		Optional<RunJob> currRunJob = currRunJobs.stream().filter(f -> f.getId().equals(runJobVO.getId())).findAny();
		RunJob runJob = currRunJob.orElseThrow(() -> new ValidationException("当前任务不存在"));
		if (JobApproveMethodEnum.ALL_AUDIT.getType().equals(approveMethod)) {
			Long signatureId = runJob.getSignatureId();
			Optional<RunJob> existSignRunJob = currRunJobs.stream().filter(f -> f.getId().equals(signatureId)).findAny();
			RunJob signRunJob = existSignRunJob.orElseThrow(() -> new ValidationException("当前任务的【前加签任务】不存在"));
			runJobs.add(signRunJob);
		} else if (JobApproveMethodEnum.ANY_ONE.getType().equals(approveMethod)) {
			List<RunJob> anyRunJobs = currRunJobs.stream().filter(f -> !f.getId().equals(runJobVO.getId())).collect(Collectors.toList());
			runJobs.addAll(anyRunJobs);
		} else if (JobApproveMethodEnum.SEQUENTIAL.getType().equals(approveMethod)) {
			// 开启下一顺位的任务
			Optional<RunJob> first = currRunJobs.stream().filter(f -> f.getSort() > runJobVO.getSort()).min(Comparator.comparing(RunJob::getSort));
			RunJob nextRunJob = first.orElseThrow(() -> new ValidationException("当前任务的【下一顺位的任务】不存在"));
			// 先加并签再前加签
			List<RunJob> nextRunJobs = currRunJobs.stream().filter(f -> f.getSort().equals(nextRunJob.getSort())).collect(Collectors.toList());
			runJobs.addAll(nextRunJobs);
		}
	}

	private List<RunJob> completeSameRunJobs(RunNodeVO runNodeVO, List<RunJob> currRunJobs, Long id) {
		if (!FlowCommonConstants.YES.equals(runNodeVO.getIsPassSame())) return currRunJobs;
		List<Long> runJobIds = currRunJobs.stream().filter(f -> NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus()) &&
				SecurityUtils.getUser().getId().equals(f.getRoleId()) && JobUserTypeEnum.USER.getType().equals(f.getJobType()) &&
				!f.getId().equals(id)).map(RunJob::getId).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(runJobIds)) {
			runJobService.completeRunJobStatus(null, runJobIds, NodeJobStatusEnum.ACTIVE.getStatus());
			currRunJobs = currRunJobs.stream().filter(f -> !runJobIds.contains(f.getId())).collect(Collectors.toList());
		}
		return currRunJobs;
	}

	private boolean doCompleteRunJob(RunJobVO runJobVO) {
		if (Objects.isNull(runJobVO.getRunNodeId())) throw new ValidationException("不存在节点ID");
		// 处理同任务多参与者同时审批、或签节点相同参与者自动审批、或签任务相同参与者自动审批
		RunJob isComplete = runJobService.getById(runJobVO.getId());
		if (!FlowCommonConstants.YES.equals(runJobVO.getIsForceAudit())) {
			if (NodeJobStatusEnum.COMPLETE.getStatus().equals(isComplete.getStatus()) || !NodeJobStatusEnum.getRunRejectedStatuses().contains(isComplete.getStatus())) {
				return true;
			}
		}
		// 完成任务时监听事件
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.COMPLETE_JOB.getMethod());
		flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		// 判断是否挂起
		String suspension = runJobVO.getSuspension();
		if (NodeJobStatusEnum.SUSPEND.getStatus().equals(suspension)) runJobVO.setSuspension(NodeJobStatusEnum.ACTIVE.getStatus());
		// 完成审批
		runJobService.lambdaUpdate().set(RunJob::getSuspension, runJobVO.getSuspension())
				// 不更新角色码，角色码为默认的
				.set(RunJob::getUserId, SecurityUtils.getUser().getId())
				.set(RunJob::getEndTime, LocalDateTime.now())
				.set(RunJob::getStatus, NodeJobStatusEnum.COMPLETE.getStatus())
				.eq(RunJob::getId, runJobVO.getId())
				.update();
		// 记录审批意见
		commentService.saveOrUpdateComment(runJobVO, false);
		return false;
	}

	@Override
	public boolean matchRunJobs(RunJobVO runJobVO, List<RunNode> nextRunNodes, List<RunReject> runRejects, boolean isExecStart, boolean isSaveRel) {
		List<Long> nextRunIds = nextRunNodes.stream().map(RunNode::getId).collect(Collectors.toList());
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, nextRunIds));
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("下一步开始的任务不能为空");
		// 处理直接驳回逻辑
		if (NodeCircTypeEnum.DIRECT_RETURN.getType().equals(runJobVO.getCircularType()) && CollUtil.isNotEmpty(runRejects)) {
			Long currUserId = SecurityUtils.getUser().getId();
			List<RunReject> existRejects = runRejects.stream().filter(f -> currUserId.equals(f.getHandleUserId())).collect(Collectors.toList());
			if (CollUtil.isEmpty(existRejects)) existRejects.addAll(runRejects);
			runJobs = runJobs.stream().filter(f -> existRejects.stream().anyMatch(any -> any.getFromRunJobId().equals(f.getId()))).collect(Collectors.toList());
		}
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("下一步开始的任务不能为空");
		// 判断符合条件的任务
		runJobs = flowVariableService.handleFlowNodeJobVariable(runJobs);
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("下一步开始的任务不能为空");
		// 判断任务条件是否都跳过
		List<RunJob> runJobSkips = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(runJobSkips)) runJobs = runJobSkips;
		// 处理多人审批方式
		List<RunJob> finalRunJobs = runJobs;
		nextRunNodes.forEach(each -> {
			if (JobApproveMethodEnum.SEQUENTIAL.getType().equals(each.getApproveMethod())) {
				List<RunJob> sequential = finalRunJobs.stream().filter(f -> f.getRunNodeId().equals(each.getId())).collect(Collectors.toList());
				if (CollUtil.size(sequential) <= 1) return;
				// 只开启第一顺位的任务
				RunJob first = sequential.stream().min(Comparator.comparing(RunJob::getSort)).orElseThrow(() -> new ValidationException("下一步开始的任务不能为空"));
				// 处理加并签
				List<RunJob> nextRunJobs = sequential.stream().filter(f -> f.getSort().equals(first.getSort())).collect(Collectors.toList());
				finalRunJobs.removeIf(r -> sequential.stream().anyMatch(any -> any.getId().equals(r.getId())));
				finalRunJobs.addAll(nextRunJobs);
			}
		});
		// 设置办理人及通知待办任务
		this.notifyNextUsers(runJobVO, finalRunJobs, isExecStart, isSaveRel);
		// 处理下一步节点相同审批人自动审批
		this.completeSameRunNodes(runJobVO, nextRunNodes, finalRunJobs);
		return true;
	}

	private void completeSameRunNodes(RunJobVO runJobVO, List<RunNode> nextRunNodes, List<RunJob> finalRunJobs) {
		if (!FlowCommonConstants.YES.equals(runJobVO.getRunNodeVO().getIsPassSame())) return;
		Map<Long, List<RunJob>> existSameRunJobs = finalRunJobs.stream().filter(f -> NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus()) &&
				SecurityUtils.getUser().getId().equals(f.getRoleId()) && JobUserTypeEnum.USER.getType().equals(f.getJobType())).collect(Collectors.groupingBy(RunJob::getRunNodeId));
		if (existSameRunJobs.isEmpty()) return;
		existSameRunJobs.forEach((key, val) -> {
			RunJob runJob = val.get(0);
			Optional<RunNode> anyRunNode = nextRunNodes.stream().filter(f -> f.getId().equals(runJob.getRunNodeId())).findAny();
			RunNode runNode = anyRunNode.orElseThrow(() -> new ValidationException("下一步开始的节点不能为空"));
			RunJobVO runJobVONext = this.buildNextRunJobVO(null, runJob, runNode);
			this.completeRunJob(runJobVONext);
		});
	}

	// 设置办理人及通知待办任务
	private void notifyNextUsers(RunJobVO runJobVO, List<RunJob> runJobs, boolean isExecStart, boolean isSaveRel) {
		// 设置办理人
		if (isExecStart) {
			// 节点分配参与者监听事件
			LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.DIST_PERSON.getMethod());
			flowClazzService.handleFlowJobClazz(runJobVO, linkedList, true);
			nodeJobHandler.setNextUsers(runJobVO, runJobs);
		}
		// 开始符合条件任务并设置办理人
		runJobService.handleRunJobsUser(runJobs, NodeJobStatusEnum.RUN.getStatus());
		this.nextNotifyClazzes(runJobVO, runJobs, isExecStart, isSaveRel);
	}

	@Override
	public void nextNotifyClazzes(RunJobVO runJobVO, List<RunJob> runJobs, boolean isExecStart, boolean isSaveRel) {
		List<RunJob> nextRunJobs = runJobs.stream().filter(f -> !f.getRunNodeId().equals(runJobVO.getRunNodeId())).collect(Collectors.toList());
		// 任务开启监听事件
		if (isExecStart) {
			LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.START_JOB.getMethod());
			flowClazzService.handleNextJobClazzes(runJobs, linkedList);
			if (CollUtil.isNotEmpty(nextRunJobs)) {
				LinkedList<String> linkedNext = CollUtil.newLinkedList(FlowClazzMethodEnum.START_NEXT_JOB.getMethod());
				flowClazzService.handleNextJobClazz(runJobVO, nextRunJobs, linkedNext);
			}
		}
		if (CollUtil.isNotEmpty(nextRunJobs)) {
			// 记录开启和来源的节点ID
			List<Long> toRunNodeIds = nextRunJobs.stream().map(RunJob::getRunNodeId).distinct().collect(Collectors.toList());
			this.saveNextRunNodeRel(runJobVO.getRunNodeId(), toRunNodeIds, isSaveRel);
		}
		// 为了去重最后通知
		nodeJobHandler.notify(runJobVO, runJobs);
	}

	private void saveNextRunNodeRel(Long runNodeId, List<Long> toRunNodeIds, boolean isSaveRel) {
		if (Objects.isNull(runNodeId) || !isSaveRel) return;
		RunNode runNode = runNodeService.getById(runNodeId);
		if (FlowCommonConstants.NO.equals(runNode.getNextStatus())) {
			String[] oldToRunNodeIds = runNode.getToRunNodeIds();
			if (ArrayUtil.isNotEmpty(oldToRunNodeIds)) {
				List<Long> runNodeIds = Arrays.stream(oldToRunNodeIds).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
				Collection<Long> subtract = CollUtil.subtract(toRunNodeIds, runNodeIds);
				if (CollUtil.isNotEmpty(subtract)) {
					runNodeIds.addAll(subtract);
					this.updateToRunNodeIds(runNodeId, runNodeIds, false);
				}
			} else {
				this.updateToRunNodeIds(runNodeId, toRunNodeIds, false);
			}
		} else if (FlowCommonConstants.YES.equals(runNode.getNextStatus())) {
			this.updateToRunNodeIds(runNodeId, toRunNodeIds, true);
		}
		toRunNodeIds.forEach(nextRunNodeId -> {
			List<Long> fromRunNodeIds = CollUtil.newArrayList(runNodeId);
			RunNode nextRunNode = runNodeService.getById(nextRunNodeId);
			if (FlowCommonConstants.NO.equals(nextRunNode.getCurrStatus())) {
				String[] oldFromRunNodeIds = nextRunNode.getFromRunNodeIds();
				if (ArrayUtil.isNotEmpty(oldFromRunNodeIds)) {
					List<Long> runNodeIds = Arrays.stream(oldFromRunNodeIds).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
					if (!CollUtil.contains(runNodeIds, runNodeId)) {
						runNodeIds.add(runNodeId);
						this.updateFromRunNodeIds(nextRunNodeId, runNodeIds, false);
					}
				} else {
					this.updateFromRunNodeIds(nextRunNodeId, fromRunNodeIds, false);
				}
			} else if (FlowCommonConstants.YES.equals(nextRunNode.getCurrStatus())) {
				this.updateFromRunNodeIds(nextRunNodeId, fromRunNodeIds, true);
			}
		});
	}

	private void updateToRunNodeIds(Long runNodeId, List<Long> toRunNodeIds, boolean isUpdateNextStatus) {
		String[] newToRunNodeIds = toRunNodeIds.stream().map(String::valueOf).distinct().toArray(String[]::new);
		runNodeService.lambdaUpdate().set(RunNode::getToRunNodeIds, newToRunNodeIds)
				.set(isUpdateNextStatus, RunNode::getNextStatus, FlowCommonConstants.NO)
				.eq(RunNode::getId, runNodeId)
				.update();
	}

	private void updateFromRunNodeIds(Long nextRunNodeId, List<Long> fromRunNodeIds, boolean isUpdateCurrStatus) {
		String[] newFromRunNodeIds = fromRunNodeIds.stream().map(String::valueOf).distinct().toArray(String[]::new);
		runNodeService.lambdaUpdate().set(RunNode::getFromRunNodeIds, newFromRunNodeIds)
				.set(isUpdateCurrStatus, RunNode::getCurrStatus, FlowCommonConstants.NO)
				.eq(RunNode::getId, nextRunNodeId)
				.update();
	}

	@Override
	public boolean anyJump(RunJobVO runJobVO) {
		if (this.doCompleteRunJob(runJobVO)) return true;
		RunRejectVO runRejectVO = runJobVO.getRunRejectVO();
		// 结束当前节点任务
		runJobService.completeRunJobStatus(runJobVO.getRunNodeId(), null, NodeJobStatusEnum.ACTIVE.getStatus());
		runNodeService.completeRunNodeStatus(runJobVO.getRunNodeId(), runJobVO.getRunNodeId(), NodeJobStatusEnum.ACTIVE.getStatus());
		// 开启待跳转节点任务
		Long toRunNodeId = runRejectVO.getToRunNodeId();
		RunNode runNode = runNodeService.getById(toRunNodeId);
		runNodeService.handleRunNodeStatus(CollUtil.newArrayList(runNode), NodeJobStatusEnum.RUN.getStatus());
		// 指定待跳转人或角色
		List<RunJob> runJobs = runNodeService.anyJump(runJobVO);
		runJobService.handleRunJobsUser(runJobs, NodeJobStatusEnum.RUN.getStatus());
		// 节点跳转监听事件
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.ANY_JUMP.getMethod());
		flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		// 节点被跳转监听事件
		RunJobVO rejectedRunJobVO = new RunJobVO();
		BeanUtil.copyProperties(runJobs.get(0), rejectedRunJobVO);
		linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.ANY_JUMPED.getMethod());
		flowClazzService.handleFlowJobClazz(rejectedRunJobVO, linkedList, false);
		// 通知待办任务
		this.nextNotifyClazzes(runJobVO, runJobs, false, true);
		return true;
	}

	@Override
	public boolean backPreJob(RunJobVO runJobVO) {
		List<Long> runNodeIds = this.buildFromRunNodeIds(runJobVO, "当前节点不存在来源的节点ID，不能做退回上一步操作");
		this.validateToRunNodeIds(runNodeIds, "当前节点或其他并行节点已经被审批，不能做退回上一步操作");
		// 开启来源的节点
		List<RunJob> preRunJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, runNodeIds));
		runJobService.handleRunNodeJobStatus(preRunJobs, NodeJobStatusEnum.RUN.getStatus(), false, false);
		// 通知待办任务
		this.nextNotifyClazzes(runJobVO, preRunJobs, false, false);
		return true;
	}

	private List<Long> buildFromRunNodeIds(RunJobVO runJobVO, String message) {
		RunNode runNode = runNodeService.getById(runJobVO.getRunNodeId());
		String[] fromRunNodeIds = runNode.getFromRunNodeIds();
		List<Long> runNodeIds = null;
		if (ArrayUtil.isNotEmpty(fromRunNodeIds)) {
			runNodeIds = Arrays.stream(fromRunNodeIds).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
		}
		if (CollUtil.isEmpty(runNodeIds)) throw new ValidationException(message);
		return runNodeIds;
	}

	private void validateToRunNodeIds(List<Long> runNodeIds, String message) {
		List<RunNode> runNodes = runNodeService.listByIds(runNodeIds);
		List<Long> runNodeIdsMap = runNodes.stream().flatMap(f -> {
				List<Long> runNodeIdList = new ArrayList<>();
				String[] toRunNodeIds = f.getToRunNodeIds();
				if (ArrayUtil.isNotEmpty(toRunNodeIds)) {
					runNodeIdList = Arrays.stream(toRunNodeIds).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
				}
				return runNodeIdList.stream();
			}).collect(Collectors.toList());
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, runNodeIdsMap));
		boolean anyMatch = runJobs.stream().anyMatch(any -> NodeJobStatusEnum.getRejectRejectedCompleteStatuses().contains(any.getStatus()));
		if (anyMatch) throw new ValidationException(message);
		List<RunJob> runJobList = runJobs.stream().filter(f -> NodeJobStatusEnum.RUN.getStatus().equals(f.getStatus())).collect(Collectors.toList());
		runJobService.handleRunNodeJobStatus(runJobList, NodeJobStatusEnum.NO_START.getStatus(), false, false);
	}

	@Override
	public boolean backFirstJob(RunJobVO runJobVO) {
		List<Long> runNodeIds = this.buildFromRunNodeIds(runJobVO, "当前节点不存在来源的节点ID，不能做退回首节点操作");
		this.validateToRunNodeIds(runNodeIds, "当前节点或其他并行节点已经被审批，不能做退回首节点操作");
		// 开启首节点
		RunNode start = runNodeService.getOne(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, runJobVO.getFlowInstId())
				.eq(RunNode::getNodeType, NodeTypeEnum.START.getType()));
		List<RunJob> preRunJobs = CollUtil.newArrayList(runJobService.getOne(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, start.getId())));
		runJobService.handleRunNodeJobStatus(preRunJobs, NodeJobStatusEnum.RUN.getStatus(), false, false);
		// 通知待办任务
		this.nextNotifyClazzes(runJobVO, preRunJobs, false, false);
		return true;
	}

	@Override
	public boolean reject(RunJobVO runJobVO) {
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		RunRejectVO runRejectVO = runJobVO.getRunRejectVO();
		// 非虚拟节点不能往后驳回,只能往前驳回
		if (!FlowCommonConstants.IS_ENABLED.equals(flowCommonProperties.getIsRejectAnyNode()) && !runRejectVO.getNodeType().equals(NodeTypeEnum.VIRTUAL.getType())) {
			// 被驳回节点是否为当前节点祖先节点
			if (!this.isCurrAncestorNode(runNodeVO, runRejectVO)) throw new ValidationException("不能往后驳回,只能往前驳回");
		}
		// 处理驳回记录
		List<RunJob> runJobs = runRejectService.reject(runJobVO);
		// 当前节点为驳回中、驳回到节点为被驳回
		runNodeService.reject(runJobVO);
		// 驳回到任务为被驳回
		runJobService.handleRunJobsUser(runJobs, NodeJobStatusEnum.REJECTED.getStatus());
		// 当前任务为驳回中
		RunJob reject = runJobService.getById(runRejectVO.getFromRunJobId());
		runJobService.handleRunJobStatus(CollUtil.newArrayList(reject), NodeJobStatusEnum.REJECT.getStatus());
		// 节点驳回监听事件
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.REJECT.getMethod());
		flowClazzService.handleFlowJobClazz(runJobVO, linkedList, false);
		// 节点被驳回后监听事件
		RunJobVO rejectedRunJobVO = new RunJobVO();
		BeanUtil.copyProperties(runJobs.get(0), rejectedRunJobVO);
		linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.REJECTED.getMethod());
		flowClazzService.handleFlowJobClazz(rejectedRunJobVO, linkedList, false);
		// 通知待办任务
		this.nextNotifyClazzes(runJobVO, runJobs, false, false);
		return true;
	}

	/**
	 * 被驳回节点是否为当前节点祖先节点
	 *
	 * @param runNodeVO   运行节点
	 * @param runRejectVO 运行驳回
	 * @return boolean
	 */
	public boolean isCurrAncestorNode(RunNodeVO runNodeVO, RunRejectVO runRejectVO) {
		// 获取当前流程节点关系
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listFlowNodeRels(runNodeVO);
		Set<FlowNodeRel> exists = new HashSet<>();
		this.findCurrAncestorNode(flowNodeRels, runNodeVO.getFlowNodeId(), runRejectVO.getToFlowNodeId(), exists, runNodeVO.getFlowNodeId(), runNodeVO.getFlowInstId());
		return CollUtil.isNotEmpty(exists);
	}

	/**
	 * 查询当前节点给定被驳回节点是否为其祖先节点
	 *
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 * @param toFlowNodeId 到达流程节点ID
	 * @param res          返回数据
	 */
	private void findCurrAncestorNode(List<FlowNodeRel> flowNodeRels, Long flowNodeId, Long toFlowNodeId, Set<FlowNodeRel> res, Long currFlowNodeId, Long flowInstId) {
		if (CollUtil.isEmpty(flowNodeRels)) throw new ValidationException("当前流程节点关系不存在");
		// 获取当前节点的所有上一节点
		List<FlowNodeRel> fromRunNodes = flowNodeRels.stream().filter(f -> f.getToFlowNodeId().equals(flowNodeId) &&
				!f.getFromFlowNodeId().equals(currFlowNodeId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(fromRunNodes)) return;
		// 判断串行或并行条件
		fromRunNodes = flowVariableService.handleFlowNodeRelSerialParallelVariable(fromRunNodes, flowInstId);
		Optional<FlowNodeRel> any = fromRunNodes.stream().filter(f -> f.getFromFlowNodeId().equals(toFlowNodeId)).findAny();
		if (any.isPresent()) {
			res.add(any.get());
			return;
		}
		// 判断邻接表节点是否访问过
		fromRunNodes = fromRunNodes.stream().filter(f -> !FlowCommonConstants.YES.equals(f.getIsVisited())).collect(Collectors.toList());
		if (CollUtil.isEmpty(fromRunNodes)) return;
		fromRunNodes.forEach(runNode -> runNode.setIsVisited(FlowCommonConstants.YES));
		// 处理无限级并行分支或串行多个节点
		fromRunNodes.forEach(each -> this.findCurrAncestorNode(flowNodeRels, each.getFromFlowNodeId(), toFlowNodeId, res, currFlowNodeId, flowInstId));
	}

	@Override
	public RunJobVO recordRunNodes(RunFlowVO runFlowVO) {
		// 查询所有节点及配置
		List<FlowNode> flowNodes = flowNodeService.list(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, runFlowVO.getDefFlowId()));
		// 保存流程所有节点及审批人
		List<RunNode> runNodes = new ArrayList<>();
		flowNodes.forEach(flowNode -> {
			RunNode runNode = new RunNode();
			BeanUtil.copyProperties(flowNode, runNode, FlowEntityInfoConstants.ID);
			runNode.setFlowInstId(runFlowVO.getId());
			runNode.setFlowNodeId(flowNode.getId());
			runNode.setSuspension(FlowCommonConstants.NO);
			runNode.setCreateUser(runFlowVO.getCreateUser());
			runNode.setCreateTime(LocalDateTime.now());
			// 开始节点单独处理
			if (NodeTypeEnum.START.getType().equals(flowNode.getNodeType())) {
				runNode.setStartTime(LocalDateTime.now().minusSeconds(10));
				runNode.setStatus(NodeJobStatusEnum.RUN.getStatus());
			} else {
				runNode.setStatus(NodeJobStatusEnum.NO_START.getStatus());
			}
			runNodes.add(runNode);
		});
		log.info("当前流程共 {} 个节点", runNodes.size());
		runNodes.forEach(runNodeService::save);
		// baseMapper.insertBatchSomeColumn(runNodes);
		return this.recordRunJobs(runNodes);
	}

	@Override
	public boolean completeRunNode(RunJobVO runJobVO) {
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		// 判断是否挂起
		String suspension = runNodeVO.getSuspension();
		if (NodeJobStatusEnum.SUSPEND.getStatus().equals(suspension)) runNodeVO.setSuspension(suspension);
		// 整个节点完成审批
		if (this.completeRunNode(runJobVO, runNodeVO)) return true;
		List<RunNode> allRunNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, runJobVO.getFlowInstId()));
		// 处理驳回状态逻辑
		List<RunReject> runRejects;
		if (!CommonNbrPool.STR_0.equals(runJobVO.getNodeCompleteType())) {
			runRejects = runRejectService.handleRejectNodes(CollUtil.newArrayList(runJobVO.getRunNodeId()), null, true);
		} else {
			runRejects = runRejectService.handleRejectNodes(null, CollUtil.newArrayList(runJobVO.getId()), true);
		}
		// 获取当前流程节点关系
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listFlowNodeRels(runNodeVO);
		// 非自动流转，处理逻辑
		if (FlowCommonConstants.NO.equals(runNodeVO.getIsAutoNext())) {
			if (this.handleNoAutoNextEndNode(allRunNodes, runJobVO)) return true;
			if (!NodeJobStatusEnum.REJECTED.getStatus().equals(runJobVO.getStatus())) {
				this.handleRejectNoAutoNext(runJobVO, allRunNodes, flowNodeRels);
				return true;
			}
		}
		// 结束时检查节点是否全部审批完成
		if (NodeTypeEnum.END.getType().equals(runNodeVO.getNodeType())) {
			// this.endCheckAllSkipComplete(allRunNodes);
			runFlowService.complete(runJobVO);
			return true;
		}
		// 处理多节点审批方式
		String nodeApproveMethod = runNodeVO.getNodeApproveMethod();
		if (NodeApproveMethodEnum.SEQUENTIAL.getType().equals(nodeApproveMethod)) {
			List<RunNode> nodeGroupRunNodes = allRunNodes.stream().filter(f -> runNodeVO.getNodeGroupId().equals(f.getNodeGroupId())).collect(Collectors.toList());
			// 是否存在同级加并签正在运行（排除当前节点）
			List<RunNode> signRunNodes = nodeGroupRunNodes.stream().filter(f -> f.getSort().equals(runNodeVO.getSort())
					&& NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus())).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(signRunNodes)) {
				this.doMatchConRunNodes(runJobVO, allRunNodes, null, null, signRunNodes, null, true, true);
				return true;
			}
			// 开启下一顺位的节点
			Optional<RunNode> first = nodeGroupRunNodes.stream().filter(f -> f.getSort() > runNodeVO.getSort()).min(Comparator.comparing(RunNode::getSort));
			if (first.isPresent()) {
				// 处理加并签
				List<RunNode> nextRunNodes = nodeGroupRunNodes.stream().filter(f -> f.getSort().equals(first.get().getSort())).collect(Collectors.toList());
				this.doMatchConRunNodes(runJobVO, allRunNodes, null, null, nextRunNodes, null, true, true);
				return true;
			}
		}
		// 下一节点为非并行节点
		this.handleNextRunNodeNonParallel(runJobVO, runNodeVO, allRunNodes, runRejects, flowNodeRels, nodeApproveMethod);
		return true;
	}

	private void handleNextRunNodeNonParallel(RunJobVO runJobVO, RunNodeVO runNodeVO, List<RunNode> allRunNodes, List<RunReject> runRejects, List<FlowNodeRel> flowNodeRels, String nodeApproveMethod) {
		if (NodeTypeEnum.PARALLEL.getType().equals(runNodeVO.getNodeType())) {
			boolean nonParallel = this.checkNextRunNodeNonParallel(runJobVO, allRunNodes, flowNodeRels, runNodeVO.getFlowNodeId());
			if (nonParallel) {
				boolean isQueryAllRunNodes = false;
				// 或签节点则一个节点通过则完成（排除当前节点）
				if (NodeApproveMethodEnum.ANY_ONE.getType().equals(nodeApproveMethod)) {
					List<RunNode> nodeGroupRunNodes = allRunNodes.stream().filter(f -> runNodeVO.getNodeGroupId().equals(f.getNodeGroupId())).collect(Collectors.toList());
					List<RunNode> signRunNodes = nodeGroupRunNodes.stream().filter(f -> NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus())).collect(Collectors.toList());
					if (CollUtil.isNotEmpty(signRunNodes)) {
						// 自动审批运行中的任务
						List<Long> runNodeIds = signRunNodes.stream().map(RunNode::getId).collect(Collectors.toList());
						runNodeService.handleRunNodeStatus(signRunNodes, NodeJobStatusEnum.COMPLETE.getStatus());
						runJobService.handleRunJobStatusByRunNodeIds(runNodeIds, NodeJobStatusEnum.COMPLETE.getStatus());
						isQueryAllRunNodes = true;
					}
				}
				if (isQueryAllRunNodes) {
					allRunNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, runJobVO.getFlowInstId()));
					nonParallel = this.checkNextRunNodeNonParallel(runJobVO, allRunNodes, flowNodeRels, runNodeVO.getFlowNodeId());
					if (nonParallel) return;
				} else {
					return;
				}
			}
		}
		this.handleNextRunNodes(runJobVO, allRunNodes, flowNodeRels, runRejects);
	}

	// 节点完成监听事件
	private boolean completeRunNode(RunJobVO runJobVO, RunNodeVO runNodeVO) {
		if (CommonNbrPool.STR_1.equals(runJobVO.getNodeCompleteType())) {
			// 发起子流程暂停当前节点
			if(Objects.nonNull(runNodeVO.getSubDefFlowId()) && Objects.isNull(runNodeVO.getSubFlowInstId())) {
				this.startSubFlow(runJobVO, runNodeVO);
				return true;
			} else if (Objects.nonNull(runNodeVO.getSubDefFlowId()) && Objects.nonNull(runNodeVO.getSubFlowInstId())){
				// 重入或重启子流程，更新关联数据
				this.restartSubFlow(runJobVO, runNodeVO);
				return true;
			} else {
				this.doCompleteRunNode(runJobVO, runNodeVO);
			}
		} else if (CommonNbrPool.STR_2.equals(runJobVO.getNodeCompleteType())){
			this.doCompleteRunNode(runJobVO, runNodeVO);
		}
		return false;
	}

	private void doCompleteRunNode(RunJobVO runJobVO, RunNodeVO runNodeVO) {
		LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.COMPLETE_NODE.getMethod());
		flowClazzService.handleFlowNodeClazz(runJobVO, linkedList, false);
		runNodeService.completeRunNodeStatus(runNodeVO.getId(), runJobVO.getRunNodeId(), runNodeVO.getSuspension());
		this.handleCarbonCopyUser(runJobVO, runNodeVO);
	}

	@Override
	public Long startSubFlow(RunJobVO runJobVO, RunNodeVO runNodeVO) {
		Long runJobId = runJobVO.getId();
		Long parFlowInstId = runJobVO.getFlowInstId();
		Long subDefFlowId = runNodeVO.getSubDefFlowId();
		Long runNodeId = runNodeVO.getId();
		DefFlow defFlow = defFlowService.getById(subDefFlowId);
		// 构造父子流程传参回参
		Map<String, Object> params = new HashMap<>();
		Long subFlowInstId = idGenerator.nextId(null).longValue();
		params.put(FlowEntityInfoConstants.FLOW_INST_ID, subFlowInstId);
		params.put(FlowEntityInfoConstants.FLOW_KEY, defFlow.getFlowKey());
		params.put(FlowEntityInfoConstants.DEF_FLOW_ID, defFlow.getId());
		Map<String, Object> subOrder = flowRuleService.buildSubFlowParams(runNodeVO, CommonNbrPool.STR_1);
		Object resOrderVo = FlowFormHttpInvokeUtils.startSubFlow(subOrder, params, runNodeVO);
		JSONObject orderVo = JSONUtil.parseObj(resOrderVo);
		runFlowService.startFlow(orderVo, orderVo.getJSONObject(FlowCommonConstants.FLOW_VAR_USER));
		runNodeService.lambdaUpdate().set(RunNode::getSubFlowInstId, subFlowInstId)
				.set(RunNode::getSubRunJobId, runJobId)
				.set(RunNode::getSubFlowStatus, SubFlowStatusEnum.SUB_FLOW_RUN.getStatus())
				.eq(RunNode::getId, runNodeId)
				.update();
		runFlowService.lambdaUpdate().set(RunFlow::getParFlowInstId, parFlowInstId)
				.eq(RunFlow::getId, subFlowInstId)
				.update();
		return subFlowInstId;
	}

	@Override
	public void restartSubFlow(RunJobVO runJobVO, RunNodeVO runNodeVO) {
		Long subFlowInstId = runNodeVO.getSubFlowInstId();
		Long runNodeId = runNodeVO.getId();
		RunFlow subRunFlow = runFlowService.getById(subFlowInstId);
		// 构造父子流程传参回参
		Map<String, Object> params = new HashMap<>();
		params.put(FlowEntityInfoConstants.FLOW_INST_ID, subFlowInstId);
		params.put(FlowEntityInfoConstants.FLOW_KEY, subRunFlow.getFlowKey());
		Map<String, Object> subOrder = flowRuleService.buildSubFlowParams(runNodeVO, CommonNbrPool.STR_1);
		FlowFormHttpInvokeUtils.restartSubFlow(subOrder, params, runNodeVO);
		if (!FlowStatusEnum.RUN.getStatus().equals(subRunFlow.getStatus())) {
			runFlowService.lambdaUpdate().set(RunFlow::getStatus, FlowStatusEnum.RUN.getStatus())
					.eq(RunFlow::getId, subFlowInstId)
					.update();
			if (FlowStatusEnum.FINISH.getStatus().equals(subRunFlow.getStatus())) {
				RunNode firstRunNode = runNodeService.getOne(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, subFlowInstId)
						.eq(RunNode::getNodeType, NodeTypeEnum.START.getType()));
				RunNodeVO firstRunNodeVO = new RunNodeVO();
				BeanUtil.copyProperties(firstRunNode, firstRunNodeVO);
				RunJob firstRunJob = runJobService.getOne(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, firstRunNode.getId()));
				RunJobVO firstRunJobVO = new RunJobVO();
				BeanUtil.copyProperties(firstRunJob, firstRunJobVO);
				firstRunJobVO.setRunNodeVO(firstRunNodeVO);
				firstRunJobVO.setStatus(NodeJobStatusEnum.RUN.getStatus());
				firstRunJobVO.setJobBtn(JobBtnTypeEnum.INITIATE.getDesc());
				firstRunJobVO.setIsForceAudit(FlowCommonConstants.YES);
				this.completeRunJob(firstRunJobVO);
			}
		}
		// 防止多任务被驳回后审批顺序问题
		runNodeService.lambdaUpdate().set(RunNode::getSubRunJobId, runJobVO.getId())
				.set(RunNode::getSubFlowStatus, SubFlowStatusEnum.SUB_FLOW_RUN.getStatus())
				.eq(RunNode::getId, runNodeId)
				.update();
	}

	private void handleCarbonCopyUser(RunJobVO runJobVO, RunNodeVO runNodeVO) {
		List<Long> userIds = null;
		String[] carbonCopy = runNodeVO.getCarbonCopy();
		if (ArrayUtil.isNotEmpty(carbonCopy)) {
			userIds = Arrays.stream(carbonCopy).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
		}
		if (CollUtil.isEmpty(userIds)) return;
		String currUserRealName = SecurityUtils.getUser().getName();
		String ddText = "(抄送)您有一条新的抄送消息: [" + currUserRealName + "]审批通过了 [" + runNodeVO.getNodeName() +"] 节点任务";
		List<SysUser> ddUsers = auditorService.listUsersByUserIds(userIds);
		if (CollUtil.isEmpty(ddUsers)) return;
		SimpMsgTempUtils.nodeCopyNotifyUsers(ddUsers, ddText, runJobVO.getFlowInstId(), runJobVO.getFlowKey());
	}

	/**
	 * 获取当前节点之前的并行节点区间，判断是否都审批完成
	 *
	 * @param runNodes     运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 * @param res          返回数据
	 */
	private void handlePreParallelSectionRunNodes(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId, Set<RunNode> res, Long currFlowNodeId) {
		List<FlowNodeRel> fromRunNodes = buildFromNodeRels(runNodes, flowNodeRels, flowNodeId, currFlowNodeId);
		// 判断串行或并行条件
		fromRunNodes = flowVariableService.handleFlowNodeRelSerialParallelVariable(fromRunNodes, runNodes.get(0).getFlowInstId());
		fromRunNodes = fromRunNodes.stream().filter(f -> NodeTypeEnum.PARALLEL.getType().equals(f.getFromNodeType())).collect(Collectors.toList());
		if (CollUtil.isEmpty(fromRunNodes)) return;
		List<RunNode> nextRunNodes = this.filterNextRunNodes(runNodes, res, fromRunNodes, null);
		// 处理无限级并行分支或串行多个节点
		nextRunNodes.forEach(each -> this.handlePreParallelSectionRunNodes(runNodes, flowNodeRels, each.getFlowNodeId(), res, currFlowNodeId));
	}

	private List<FlowNodeRel> buildFromNodeRels(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId, Long currFlowNodeId) {
		if (CollUtil.isEmpty(runNodes)) throw new ValidationException("当前流程不存在可用节点");
		// 获取当前节点的所有上一节点
		List<FlowNodeRel> fromRunNodes = flowNodeRels.stream().filter(f -> f.getToFlowNodeId().equals(flowNodeId) &&
				!f.getFromFlowNodeId().equals(currFlowNodeId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(fromRunNodes)) throw new ValidationException("当前节点未配置与上一节点关系");
		return fromRunNodes;
	}

	/**
	 * 获取作为来源的运行节点
	 *
	 * @param runNodes     运行节点集合
	 * @param res          返回数据
	 * @param fromRunNodes 来源运行节点集合
	 */
	public List<RunNode> filterNextRunNodes(List<RunNode> runNodes, Set<RunNode> res, List<FlowNodeRel> fromRunNodes, List<FlowNodeRel> flowNodeRels) {
		List<RunNode> nextRunNodes = runNodes.stream().filter(f -> fromRunNodes.stream().anyMatch(any -> any.getFromFlowNodeId().equals(f.getFlowNodeId()))).collect(Collectors.toList());
		// 判断邻接表节点是否访问过
		nextRunNodes = nextRunNodes.stream().filter(f -> !FlowCommonConstants.YES.equals(f.getIsVisited())).collect(Collectors.toList());
		if (CollUtil.isEmpty(nextRunNodes)) return nextRunNodes;
		nextRunNodes.forEach(runNode -> runNode.setIsVisited(FlowCommonConstants.YES));
		res.addAll(nextRunNodes);
		return nextRunNodes;
	}

	/**
	 * 1.获取当前节点之后的并行节点区间
	 * 2.直到下一节点为非并行，则判断之后并行区域节点是否都审批完成
	 * 3.无限级并行节点【出入度】判断
	 *
	 * @param runNodes     运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 * @param res          返回数据
	 */
	private void handleNextParallelSectionRunNodes(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId, Set<RunNode> res, Long currFlowNodeId) {
		List<FlowNodeRel> toRunNodes = validateFlowNodeRels(runNodes, flowNodeRels, flowNodeId, currFlowNodeId);
		// 判断串行与并行条件
		toRunNodes = flowVariableService.handleFlowNodeRelSerialParallelVariable(toRunNodes, runNodes.get(0).getFlowInstId());
		String toNodeType = toRunNodes.get(0).getToNodeType();
		if (!NodeTypeEnum.PARALLEL.getType().equals(toNodeType)) return;
		List<FlowNodeRel> finalToRunNodes = toRunNodes;
		List<RunNode> nextRunNodes = runNodes.stream().filter(f -> finalToRunNodes.stream().anyMatch(any -> any.getToFlowNodeId().equals(f.getFlowNodeId())))
				.collect(Collectors.toList());
		res.addAll(nextRunNodes);
		// 处理无限级并行分支或串行多个节点
		nextRunNodes.forEach(each -> this.handleNextParallelSectionRunNodes(runNodes, flowNodeRels, each.getFlowNodeId(), res, currFlowNodeId));
	}

	/**
	 * 获取下一节点关系
	 *
	 * @param runNodes     运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 */
	private List<FlowNodeRel> validateFlowNodeRels(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId, Long currFlowNodeId) {
		if (CollUtil.isEmpty(runNodes)) throw new ValidationException("当前流程不存在可用节点");
		// 获取当前节点的所有下一节点
		List<FlowNodeRel> toRunNodes = flowNodeRels.stream().filter(f -> f.getFromFlowNodeId().equals(flowNodeId) && !f.getToFlowNodeId().equals(currFlowNodeId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(toRunNodes)) throw new ValidationException("当前节点未配置与下一节点关系");
		return toRunNodes;
	}

	/**
	 * 1.无限级并行节点【出入度】判断
	 * 2.判断下一节点是否为非并行节点
	 *
	 * @param runJobVO 运行任务
	 * @param allRunNodes  运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 */
	private boolean checkNextRunNodeNonParallel(RunJobVO runJobVO, List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId) {
		// 获取当前节点的所有下一节点
		List<FlowNodeRel> toRunNodes = flowNodeRels.stream().filter(f -> f.getFromFlowNodeId().equals(flowNodeId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(toRunNodes)) throw new ValidationException("当前节点未配置与下一节点关系");
		// 判断串行或并行条件
		toRunNodes = flowVariableService.handleFlowNodeRelSerialParallelVariable(toRunNodes, allRunNodes.get(0).getFlowInstId());
		FlowNodeRel nextFlowNodeRel = toRunNodes.get(0);
		Long toFlowNodeId = nextFlowNodeRel.getToFlowNodeId();
		String toNodeType = nextFlowNodeRel.getToNodeType();
		// 计算下一节点入度关联节点含祖先节点
		if (!NodeTypeEnum.PARALLEL.getType().equals(toNodeType)) {
			Set<RunNode> exists = new HashSet<>();
			this.handlePreParallelSectionRunNodes(allRunNodes, flowNodeRels, toFlowNodeId, exists, toFlowNodeId);
			if (CollUtil.isNotEmpty(exists)) {
				boolean b = !this.checkAllSkipComplete(CollUtil.newArrayList(exists));
				// 记录开启和来源的节点ID
				if (b) this.checkNextRunNodeRel(runJobVO.getRunNodeId(), allRunNodes, toRunNodes);
				return b;
			}
			return false;
		}
		return false;
	}

	/**
	 * 处理非自动流转是否开启（自动）结束节点
	 *
	 * @param allRunNodes 运行节点集合
	 * @param runJobVO    运行任务
	 */
	private boolean handleNoAutoNextEndNode(List<RunNode> allRunNodes, RunJobVO runJobVO) {
		// 判断是否已开启结束节点
		boolean noneMatch = allRunNodes.stream().noneMatch(any -> any.getNodeType().equals(NodeTypeEnum.END.getType()) && any.getStatus().equals(NodeJobStatusEnum.RUN.getStatus()));
		// 非自动流转节点结束后是否开启结束节点
		if (this.checkAllSkipComplete(allRunNodes) && noneMatch) {
			Optional<RunNode> any = allRunNodes.stream().filter(f -> f.getNodeType().equals(NodeTypeEnum.END.getType())).findAny();
			RunNode endNode = any.orElseThrow(() -> new ValidationException("不存在(自动)结束节点"));
			boolean isInitEnd = !NodeJobStatusEnum.REJECT.getStatus().equals(endNode.getStatus());
			this.doMatchConRunNodes(runJobVO, allRunNodes, null, null, CollUtil.newArrayList(endNode), null, isInitEnd, isInitEnd);
			return true;
		}
		return false;
	}

	/**
	 * 处理驳回再次经过非自动流转节点，判断是否自动激活之后的节点任务
	 *
	 * @param runJobVO    运行任务
	 * @param allRunNodes 运行节点集合
	 */
	private void handleRejectNoAutoNext(RunJobVO runJobVO, List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels) {
		Set<RunNode> exists = new HashSet<>();
		boolean anyRunMatch = allRunNodes.stream().anyMatch(all -> NodeJobStatusEnum.REJECTED.getStatus().equals(all.getStatus()) ||
				NodeJobStatusEnum.RUN.getStatus().equals(all.getStatus()));
		List<RunNode> rejects = allRunNodes.stream().filter(f -> NodeJobStatusEnum.REJECT.getStatus().equals(f.getStatus())).collect(Collectors.toList());
		if (!anyRunMatch && CollUtil.isNotEmpty(rejects)) {
			exists.addAll(rejects);
		} else if (anyRunMatch && CollUtil.isNotEmpty(rejects)) {
			RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
			// 获取当前节点之后的驳回中任务
			this.handleNextRejectRunNode(rejects, flowNodeRels, runNodeVO.getFlowNodeId(), exists, runNodeVO.getFlowNodeId());
		} else if (!anyRunMatch) {
			throw new ValidationException("非自动审批节点的下一节点不存在已开启的节点");
		} else {
			return;
		}
		if (CollUtil.isNotEmpty(exists)) {
			this.doMatchConRunNodes(runJobVO, allRunNodes, null, null, CollUtil.newArrayList(exists), null, false, false);
		}
	}

	/**
	 * 获取当前节点之后的驳回中任务
	 *
	 * @param runNodes     运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 * @param res          返回数据
	 */
	private void handleNextRejectRunNode(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId, Set<RunNode> res, Long currFlowNodeId) {
		List<FlowNodeRel> toRunNodes = validateFlowNodeRels(runNodes, flowNodeRels, flowNodeId, currFlowNodeId);
		List<RunNode> nextRunNodes = runNodes.stream().filter(f -> toRunNodes.stream().anyMatch(any -> any.getToFlowNodeId().equals(f.getFlowNodeId()))).collect(Collectors.toList());
		List<RunNode> runNodeList = nextRunNodes.stream().filter(f -> NodeJobStatusEnum.getRunRejectedStatuses().contains(f.getStatus())).collect(Collectors.toList());
		// 排除中间已开启的节点
		if (CollUtil.isNotEmpty(runNodeList)) return;
		List<RunNode> rejects = nextRunNodes.stream().filter(f -> NodeJobStatusEnum.REJECT.getStatus().equals(f.getStatus())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(rejects)) {
			res.addAll(rejects);
			return;
		}
		// 处理无限级并行分支或串行多个节点
		nextRunNodes.forEach(each -> this.handleNextRejectRunNode(runNodes, flowNodeRels, each.getFlowNodeId(), res, currFlowNodeId));
	}

	private void checkNextRunNodeRel(Long runNodeId, List<RunNode> allRunNodes, List<FlowNodeRel> toRunNodes) {
		List<RunNode> nextRunNodes = allRunNodes.stream().filter(f -> toRunNodes.stream().anyMatch(any -> any.getToFlowNodeId().equals(f.getFlowNodeId()))).collect(Collectors.toList());
		List<Long> toRunNodeIds = nextRunNodes.stream().map(RunNode::getId).distinct().collect(Collectors.toList());
		this.saveNextRunNodeRel(runNodeId, toRunNodeIds, true);
	}

	/**
	 * 结束时检查节点是否全部审批完成
	 *
	 * @param allRunNodes 运行节点集合
	 */
	private void endCheckAllSkipComplete(List<RunNode> allRunNodes) {
		// 当都是跳过或结束则结束整个流程
		if (!this.checkAllSkipComplete(allRunNodes)) {
			throw new ValidationException("流程存在其他节点未审批，不能结束");
		}
		// 校验流程任务是否全审批
		runJobService.endCheckAllSkipComplete(allRunNodes.get(0));
	}

	/**
	 * 结束时检查节点是否全部审批完成
	 *
	 * @param allRunNodes 运行节点集合
	 * @return boolean
	 */
	private boolean checkAllSkipComplete(List<RunNode> allRunNodes) {
		return /*NodeJobStatusEnum.REJECT.getStatus().equals(all.getStatus()) || */
				allRunNodes.stream().allMatch(all -> (NodeTypeEnum.END.getType().equals(all.getNodeType()) && NodeJobStatusEnum.REJECT.getStatus().equals(all.getStatus())) ||
						NodeJobStatusEnum.NO_START.getStatus().equals(all.getStatus()) ||
						NodeJobStatusEnum.SKIP.getStatus().equals(all.getStatus()) || NodeJobStatusEnum.COMPLETE.getStatus().equals(all.getStatus()));
	}

	/**
	 * 开始下一节点：
	 * 1、判断是否为虚拟节点，是则直接流转到原节点
	 * 2、先判断是否存在多个条件的（串行/并行）节点ID
	 * 3、不存在则开启下一顺序的节点（串行/并行）
	 */
	private void handleNextRunNodes(RunJobVO runJobVO, List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels, List<RunReject> runRejects) {
		List<RunNode> nextRunNodes;
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		List<FlowNodeRel> flowConditions = new ArrayList<>();
		if (this.validateRejected(runNodeVO) && CollUtil.isNotEmpty(runRejects)) {
			// 获取所有驳回到当前节点的原节点
			nextRunNodes = allRunNodes.stream().filter(f -> runRejects.stream().anyMatch(any -> any.getFromRunNodeId().equals(f.getId()))).collect(Collectors.toList());
			runJobVO.setCircularType(NodeCircTypeEnum.DIRECT_RETURN.getType());
		} else {
			List<FlowNodeRel> nodeRels = flowNodeRels.stream().filter(f -> f.getFromFlowNodeId().equals(runNodeVO.getFlowNodeId())).collect(Collectors.toList());
			flowConditions = nodeRels.stream().filter(f -> StrUtil.isNotBlank(f.getVarKeyVal())).collect(Collectors.toList());
			// 获取当前节点流转到多个条件的串行或并行节点ID
			if (CollUtil.isNotEmpty(flowConditions)) {
				final List<FlowNodeRel> finalConditions = flowConditions;
				nextRunNodes = allRunNodes.stream().filter(f -> finalConditions.stream().anyMatch(any -> any.getToFlowNodeId().equals(f.getFlowNodeId())))
						.collect(Collectors.toList());
				runJobVO.setCircularType(NodeCircTypeEnum.MULTI_CONDITION.getType());
			} else {
				// 获取当前节点的下一顺序节点（串行/并行）
				nextRunNodes = this.handleNextRunNodes(allRunNodes, flowNodeRels, runNodeVO.getFlowNodeId());
				runJobVO.setCircularType(NodeCircTypeEnum.SEQUENTIAL.getType());
			}
		}
		this.doMatchConRunNodes(runJobVO, allRunNodes, flowNodeRels, runRejects, nextRunNodes, flowConditions, true, true);
	}

	private void doMatchConRunNodes(RunJobVO runJobVO, List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels, List<RunReject> runRejects,
									List<RunNode> nextRunNodes, List<FlowNodeRel> flowConditions, boolean isExecStart, boolean isSaveRel) {
		nextRunNodes = this.matchConRunNodes(runJobVO, allRunNodes, nextRunNodes, flowConditions, flowNodeRels, isExecStart);
		if (CollUtil.isEmpty(nextRunNodes)) return;
		this.matchRunJobs(runJobVO, nextRunNodes, runRejects, isExecStart, isSaveRel);
	}

	/**
	 * 获取当前节点的下一顺序节点（串行/并行）
	 *
	 * @param runNodes     运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 */
	public List<RunNode> handleNextRunNodes(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId) {
		List<FlowNodeRel> toRunNodes = validateFlowNodeRels(runNodes, flowNodeRels, flowNodeId);
		return runNodes.stream().filter(f -> toRunNodes.stream().anyMatch(any -> any.getToFlowNodeId().equals(f.getFlowNodeId())))
				.collect(Collectors.toList());
	}

	private List<FlowNodeRel> validateFlowNodeRels(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId) {
		if (CollUtil.isEmpty(runNodes)) throw new ValidationException("当前流程不存在可用节点");
		// 获取当前节点的所有下一节点
		List<FlowNodeRel> toRunNodes = flowNodeRels.stream().filter(f -> f.getFromFlowNodeId().equals(flowNodeId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(toRunNodes)) throw new ValidationException("当前节点未配置与下一节点关系");
		return toRunNodes;
	}

	/**
	 * 被驳回状态且直接返回
	 *
	 * @param runNodeVO 运行节点
	 */
	private boolean validateRejected(RunNodeVO runNodeVO) {
		return /*runNodeVO.getStatus().equals(NodeJobStatusEnum.REJECTED.getStatus()) && */NodeCircTypeEnum.DIRECT_RETURN.getType().equals(runNodeVO.getRejectType());
	}

	/**
	 * 处理下一开始的节点
	 *
	 * @param runJobVO       运行任务
	 * @param allRunNodes    运行节点集合
	 * @param nextRunNodes   下一运行节点集合
	 * @param flowConditions 流程条件配置
	 * @param flowNodeRels   流程节点关系集合
	 */
	@SneakyThrows
	private List<RunNode> matchConRunNodes(RunJobVO runJobVO, List<RunNode> allRunNodes, List<RunNode> nextRunNodes,
										   List<FlowNodeRel> flowConditions, List<FlowNodeRel> flowNodeRels, boolean isExecStart) {
		// 查询拥有节点条件的节点（排除驳回）
		boolean directReturn = NodeCircTypeEnum.DIRECT_RETURN.getType().equals(runJobVO.getCircularType());
		if (!directReturn && CollUtil.isNotEmpty(flowNodeRels)) {
			List<RunNode> nextVarRunNodes = flowVariableService.handleFlowNodeVariable(nextRunNodes, flowConditions);
			// 下一步节点不存在，判断是否允许继续开启下一节点
			if (CollUtil.isEmpty(nextVarRunNodes))
				nextRunNodes = this.handleContinueRunNodes(runJobVO, allRunNodes, nextRunNodes, flowNodeRels);
			else nextRunNodes = nextVarRunNodes;
		}
		if (CollUtil.isEmpty(nextRunNodes)) throw new ValidationException("下一步开始的节点不能为空");
		// 流转到最后节点时必须要等其他节点都审批完成
		/*if (nextRunNodes.stream().anyMatch(any -> NodeTypeEnum.END.getType().equals(any.getNodeType()))) {
			boolean allFinish = this.checkAllSkipComplete(allRunNodes);
			if (!allFinish) throw new ValidationException("流转到结束节点前请先结束其他所有正在审批节点");
		}*/
		// 下一步除了多条件流转，其他流转必须为全是串行节点或并行节点
		this.nextNotMultiConRunNodes(nextRunNodes);
		// 判断顺序签节点与或签节点
		List<RunNode> anyOneRunNodes = nextRunNodes.stream().filter(f -> NodeApproveMethodEnum.ANY_ONE.getType().equals(f.getNodeApproveMethod())).collect(Collectors.toList());
		List<RunNode> sequentialRunNodes = nextRunNodes.stream().filter(f -> NodeApproveMethodEnum.SEQUENTIAL.getType().equals(f.getNodeApproveMethod())).collect(Collectors.toList());
		nextRunNodes = nextRunNodes.stream().filter(f -> NodeApproveMethodEnum.ALL_AUDIT.getType().equals(f.getNodeApproveMethod())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(anyOneRunNodes)) {
			this.extractedRunNodeGroupId(anyOneRunNodes);
			nextRunNodes.addAll(anyOneRunNodes);
		}
		// 只开启第一顺位的节点
		if (CollUtil.isNotEmpty(sequentialRunNodes)) {
			this.extractedRunNodeGroupId(sequentialRunNodes);
			RunNode first = sequentialRunNodes.stream().min(Comparator.comparing(RunNode::getSort)).orElseThrow(() -> new ValidationException("下一步开始的节点不能为空"));
			// 处理加并签
			List<RunNode> sequential = sequentialRunNodes.stream().filter(f -> f.getSort().equals(first.getSort())).collect(Collectors.toList());
			nextRunNodes.addAll(sequential);
		}
		runNodeService.handleRunNodeStatus(nextRunNodes, NodeJobStatusEnum.RUN.getStatus());
		// 节点开启监听事件
		if (isExecStart) {
			LinkedList<String> linkedList = CollUtil.newLinkedList(FlowClazzMethodEnum.START_NODE.getMethod());
			flowClazzService.handleNextNodeClazzes(nextRunNodes, linkedList);
		}
		// 处理自动审批的节点（含并行节点）
		List<RunNode> autoAudits = nextRunNodes.stream().filter(f -> FlowCommonConstants.YES.equals(f.getIsAutoAudit())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(autoAudits)) {
			autoAudits.forEach(each -> this.handleAutoAuditNodes(runJobVO, each));
			nextRunNodes = nextRunNodes.stream().filter(f -> autoAudits.stream().noneMatch(r -> r.getId().equals(f.getId()))).collect(Collectors.toList());
		}
		// 节点开启下一步监听事件
		if (isExecStart) {
			List<RunNode> existRunNodes = nextRunNodes.stream().filter(f -> !f.getId().equals(runJobVO.getRunNodeId())).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(existRunNodes)) {
				LinkedList<String> linkedNext = CollUtil.newLinkedList(FlowClazzMethodEnum.START_NEXT_NODE.getMethod());
				flowClazzService.handleNextNodeClazz(runJobVO, existRunNodes, linkedNext);
			}
		}
		// 开启符合条件节点
		return nextRunNodes;
	}

	private void extractedRunNodeGroupId(List<RunNode> runNodes) {
		Long nodeGroupId = idGenerator.nextId(null).longValue();
		runNodes.forEach(each -> {
			each.setNodeGroupId(nodeGroupId);
			runNodeService.lambdaUpdate().set(RunNode::getNodeGroupId, nodeGroupId).eq(RunNode::getId, each.getId()).update();
		});
	}

	/**
	 * 下一步除了多条件流转，其他流转必须为全是串行节点或并行节点
	 *
	 * @param nextRunNodes 下一运行节点集合
	 */
	private void nextNotMultiConRunNodes(List<RunNode> nextRunNodes) {
		boolean start = nextRunNodes.stream().anyMatch(any -> NodeTypeEnum.START.getType().equals(any.getNodeType()));
		boolean serial = nextRunNodes.stream().anyMatch(any -> NodeTypeEnum.SERIAL.getType().equals(any.getNodeType()));
		boolean parallel = nextRunNodes.stream().anyMatch(any -> NodeTypeEnum.PARALLEL.getType().equals(any.getNodeType()));
		boolean end = nextRunNodes.stream().anyMatch(any -> NodeTypeEnum.END.getType().equals(any.getNodeType()));
		if ((end && parallel) || (parallel && serial) || (end && serial) || (end && start) || (start && parallel) || (start && serial)) {
			throw new ValidationException("下一步节点不能同时存在开始节点或串行节点或并行节点或结束节点");
		}
		// 判断如果为开始节点或串行节点或结束节点则不能同时开启多个节点
		if ((start || serial || end) && nextRunNodes.size() > 1) throw new ValidationException("下一步开始节点或串行节点或结束节点不能同时开启多个");
	}

	/**
	 * 获取当前节点的下一满足条件的顺序节点
	 *
	 * @param runJobVO     运行任务
	 * @param allRunNodes  运行节点集合
	 * @param nextRunNodes 下一运行节点集合
	 */
	private List<RunNode> handleContinueRunNodes(RunJobVO runJobVO, List<RunNode> allRunNodes, List<RunNode> nextRunNodes, List<FlowNodeRel> flowNodeRels) {
		RunNodeVO runNodeVO = runJobVO.getRunNodeVO();
		// 多个不满足条件节点存在至少一个配置继续下一节点
		nextRunNodes = nextRunNodes.stream().filter(f -> FlowCommonConstants.YES.equals(f.getIsContinue())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(nextRunNodes)) {
			// 自动审批运行中的任务
			List<Long> runNodeIds = nextRunNodes.stream().map(RunNode::getId).collect(Collectors.toList());
			runNodeService.handleRunNodeStatus(nextRunNodes, NodeJobStatusEnum.COMPLETE.getStatus());
			runJobService.handleRunJobStatusByRunNodeIds(runNodeIds, NodeJobStatusEnum.COMPLETE.getStatus());
			// 记录审批意见
			nextRunNodes.forEach(commentService::saveOrUpdateComment);
			Set<RunNode> exists = this.handleVarNextRunNodes(allRunNodes, flowNodeRels, runNodeVO.getFlowNodeId());
			return CollUtil.newArrayList(exists);
		}
		return ListUtil.empty();
	}

	private Set<RunNode> handleVarNextRunNodes(List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId) {
		// 获取当前流程所有的条件
		List<FlowNodeRel> flowConditions = flowNodeRels.stream().filter(f -> StrUtil.isNotBlank(f.getVarKeyVal())).collect(Collectors.toList());
		// 获取当前流程所有满足条件的节点
		List<RunNode> nextVarRunNodes = flowVariableService.handleFlowNodeVariable(allRunNodes, flowConditions);
		Set<RunNode> exists = new HashSet<>();
		this.doVarNextRunNodes(nextVarRunNodes, flowNodeRels, flowNodeId, exists, flowNodeId);
		return exists;
	}

	/**
	 * 获取当前节点的下一顺序节点（串行/并行） ，不存在则继续
	 *
	 * @param allRunNodes  运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 * @param res          返回数据
	 */
	private void doVarNextRunNodes(List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId, Set<RunNode> res, Long currFlowNodeId) {
		List<FlowNodeRel> toRunNodes = validateFlowNodeRels(allRunNodes, flowNodeRels, flowNodeId, currFlowNodeId);
		List<RunNode> nextRunNodes = allRunNodes.stream().filter(f -> toRunNodes.stream().anyMatch(any -> any.getToFlowNodeId().equals(f.getFlowNodeId())))
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(nextRunNodes)) {
			toRunNodes.forEach(each -> this.doVarNextRunNodes(allRunNodes, flowNodeRels, each.getToFlowNodeId(), res, currFlowNodeId));
		} else res.addAll(nextRunNodes);
	}

	/**
	 * 更新自动审批节点状态
	 *
	 * @param runJobVO 运行任务 TODO 当前runJobVO
	 * @param runNode  运行节点
	 */
	private void handleAutoAuditNodes(RunJobVO runJobVO, RunNode runNode) {
		// 自动审批运行中的任务
		runJobService.handleRunJobStatusByRunNodeIds(CollUtil.newArrayList(runNode.getId()), NodeJobStatusEnum.COMPLETE.getStatus());
		// 记录审批意见
		commentService.saveOrUpdateComment(runNode);
		RunJobVO runJobVONext = this.buildNextRunJobVO(runJobVO, null, runNode);
		// 完成整个节点
		runJobVONext.setNodeCompleteType(CommonNbrPool.STR_1);
		this.completeRunNode(runJobVONext);
	}

	private RunJobVO buildNextRunJobVO(RunJobVO runJobVO, RunJob runJob, RunNode runNode) {
		// 防止更改来源runJobVO上下文
		RunJobVO runJobVONext = new RunJobVO();
		if (Objects.nonNull(runJobVO)) {
			BeanUtil.copyProperties(runJobVO, runJobVONext);
		} else {
			BeanUtil.copyProperties(runJob, runJobVONext);
			runJobVONext.setJobBtn(JobBtnTypeEnum.APPROVE.getType());
		}
		RunNodeVO runNodeVO = new RunNodeVO();
		BeanUtil.copyProperties(runNode, runNodeVO);
		runJobVONext.setRunNodeVO(runNodeVO);
		return runJobVONext;
	}

	@Override
	public List<RunNode> listPreByFlowNodeId(Long flowInstId, Long runNodeId) {
		RunNode runNode = runNodeService.getById(runNodeId);
		// 获取当前流程节点关系
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listFlowNodeRels(runNode);
		List<RunNode> allRunNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
		// 防止邻接表节点漏掉
		// allRunNodes = allRunNodes.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus())).collect(Collectors.toList());
		List<RunNode> virtuals = allRunNodes.stream().filter(f -> NodeTypeEnum.VIRTUAL.getType().equals(f.getNodeType())).collect(Collectors.toList());
		// 获取当前节点的祖先节点（串行/并行）
		if (!FlowCommonConstants.IS_ENABLED.equals(flowCommonProperties.getIsRejectAnyNode())) {
			Set<RunNode> exists = new HashSet<>();
			// 判断有向图是否有环
			this.handlePreRunNodes(allRunNodes, flowNodeRels, runNode, exists, runNode.getFlowNodeId());
			if (CollUtil.isEmpty(exists)) throw new ValidationException("当前节点的祖先节点不存在");
			exists.addAll(virtuals);
			allRunNodes = CollUtil.newArrayList(exists);
		}
		// 判断任意驳回到的任务是否满足条件
		List<Long> runNodeIds = allRunNodes.stream().filter(f -> !FlowCommonConstants.YES.equals(f.getIsAutoAudit())).map(RunNode::getId).collect(Collectors.toList());
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, runNodeIds));
		runJobs = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus()) && !FlowCommonConstants.YES.equals(f.getIsSkipRejected())
				&& Objects.nonNull(f.getUserId()) && Objects.nonNull(f.getRoleId())
				&& (Objects.nonNull(f.getStartTime()) || virtuals.stream().anyMatch(v -> v.getFlowNodeId().equals(f.getFlowNodeId())))).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) {
			throw new ValidationException("当前节点的祖先节点任务不满足条件");
		}
		List<RunJob> finalRunJobs = runJobs;
		allRunNodes = allRunNodes.stream().filter(f -> finalRunJobs.stream().anyMatch(any -> any.getRunNodeId().equals(f.getId()))
				&& !f.getId().equals(runNodeId)).collect(Collectors.toList());
		return allRunNodes;
	}

	@Override
	public List<RunNode> listAnyJumpByFlowNodeId(Long flowInstId, Long runNodeId) {
		List<RunNode> allRunNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
		// 判断任意跳转到的任务是否满足条件
		List<Long> runNodeIds = allRunNodes.stream().filter(f -> !FlowCommonConstants.YES.equals(f.getIsAutoAudit())).map(RunNode::getId).collect(Collectors.toList());
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, runNodeIds));
		runJobs = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) {
			throw new ValidationException("当前节点的祖先节点任务不满足条件");
		}
		List<RunJob> finalRunJobs = runJobs;
		allRunNodes = allRunNodes.stream().filter(f -> finalRunJobs.stream().anyMatch(any -> any.getRunNodeId().equals(f.getId()))
				&& !f.getId().equals(runNodeId)).collect(Collectors.toList());
		return allRunNodes;
	}

	/**
	 * 获取当前节点的祖先节点，直到上一节点不存在
	 *
	 * @param runNodes     运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param runNode      流程节点
	 * @param res          返回数据
	 */
	private void handlePreRunNodes(List<RunNode> runNodes, List<FlowNodeRel> flowNodeRels, RunNode runNode, Set<RunNode> res, Long currFlowNodeId) {
		if (CollUtil.isEmpty(runNodes)) throw new ValidationException("当前流程不存在可用节点");
		// 获取当前节点的所有上一节点
		List<FlowNodeRel> fromRunNodes = flowNodeRels.stream().filter(f -> f.getToFlowNodeId().equals(runNode.getFlowNodeId()) &&
				!f.getFromFlowNodeId().equals(currFlowNodeId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(fromRunNodes)) return;
		// 判断串行或并行条件
		fromRunNodes = flowVariableService.handleFlowNodeRelSerialParallelVariable(fromRunNodes, runNodes.get(0).getFlowInstId());
		List<RunNode> nextRunNodes = this.filterNextRunNodes(runNodes, res, fromRunNodes, flowNodeRels);
		nextRunNodes.forEach(each -> this.handlePreRunNodes(runNodes, flowNodeRels, each, res, currFlowNodeId));
	}

	@Override
	public List<RunJob> setNextAllRunJobsByDistPerson(RunJob runJob, List<DistPerson> distPersons) {
		DistPersonVO params = new DistPersonVO();
		List<RunJob> runJobs = this.listRunJobsByRunNode(runJob);
		List<DistPersonVO> distPersonVOs = this.buildDistPersonVOS(distPersons);
		// 找出已存在的运行任务
		List<RunJob> existRunJobs = this.buildExistRunJobs(params, distPersonVOs, runJobs);
		List<RunJobVO> noRunJobVOS = this.buildRunJobVOS(runJobs, existRunJobs);
		// 处理动态计算分配类型
		this.handleCalculateType(params, distPersonVOs, noRunJobVOS, runJobs.get(0));
		// 更新或新增分配参与者对应的任务
		this.updateOrAddRunJobs(params, existRunJobs, noRunJobVOS);
		return existRunJobs;
	}

	@Override
	public void calculateAllRunJobs(DistPersonVO params, List<DistPerson> distPersons) {
		// 校验当前userKey是否存在动态计算的任务
		List<RunJob> runJobs = this.listRunJobs(params);
		List<DistPersonVO> distPersonVOs = this.buildDistPersonVOS(distPersons);
		// 处理多节点任务相同userKey
		Map<Long, List<RunJob>> listMap = runJobs.stream().collect(Collectors.groupingBy(RunJob::getFlowNodeId));
		List<RunJob> allExistRunJobs = CollUtil.newArrayList();
		List<RunJobVO> allNoRunJobVOS = CollUtil.newArrayList();
		listMap.forEach((key, val) -> {
			// 找出已存在的运行任务
			List<RunJob> existRunJobs = this.buildExistRunJobs(params, distPersonVOs, val);
			List<RunJobVO> noRunJobVOS = this.buildRunJobVOS(val, existRunJobs);
			// 处理动态计算分配类型
			this.handleCalculateType(params, distPersonVOs, noRunJobVOS, val.get(0));
			allExistRunJobs.addAll(existRunJobs);
			allNoRunJobVOS.addAll(noRunJobVOS);
		});
		// 更新或新增分配参与者对应的任务
		this.updateOrAddRunJobs(params, allExistRunJobs, allNoRunJobVOS);
	}

	private List<DistPersonVO> buildDistPersonVOS(List<DistPerson> distPersons) {
		return distPersons.stream().map(m -> {
			DistPersonVO distPersonVO = new DistPersonVO();
			BeanUtil.copyProperties(m, distPersonVO);
			return distPersonVO;
		}).collect(Collectors.toList());
	}

	private List<RunJobVO> buildRunJobVOS(List<RunJob> val, List<RunJob> existRunJobs) {
		List<RunJob> noRunJobs = val.stream().filter(f -> CollUtil.isEmpty(existRunJobs) || !existRunJobs.contains(f)).collect(Collectors.toList());
		return noRunJobs.stream().map(m -> {
			RunJobVO runJobVO = new RunJobVO();
			BeanUtil.copyProperties(m, runJobVO);
			return runJobVO;
		}).collect(Collectors.toList());
	}

	private List<RunJob> listRunJobsByRunNode(RunJob runJob) {
		Long runNodeId = runJob.getRunNodeId();
		String userKey = runJob.getUserKey();
		// 同节点下多个任务
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getRunNodeId, runNodeId)
				.eq(StrUtil.isNotBlank(userKey), RunJob::getUserKey, userKey)
				.isNull(StrUtil.isBlank(userKey), RunJob::getUserKey)
				.eq(RunJob::getValType, runJob.getValType()));
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException(ValidationExceptions.NO_USER_KEY_JOB);
		return runJobs;
	}

	private List<RunJob> buildExistRunJobs(DistPersonVO params, List<DistPersonVO> distPersonVOs, List<RunJob> val) {
		return val.stream().filter(f -> {
			Optional<DistPersonVO> exist = distPersonVOs.stream().filter(any -> any.getRoleId().equals(f.getRoleId())
					&& any.getJobType().equals(f.getJobType())).findAny();
			if (!exist.isPresent()) return false;
			exist.get().setIsExistPerson(FlowCommonConstants.YES);
			// 完成后就不能更改
			if (!NodeJobStatusEnum.COMPLETE.getStatus().equals(f.getStatus())) {
				runJobService.resetByDistPerson(exist.get(), f);
				// 判断流程是否结束，结束则直接让任务结束
				this.handleCalculateStatus(f, params);
			}
			return this.extractExistRunJobs(params, f);
		}).collect(Collectors.toList());
	}

	private boolean extractExistRunJobs(DistPersonVO params, RunJob f) {
		// if (NodeJobStatusEnum.SKIP.getStatus().equals(f.getStatus())) return true;
		if (FlowCommonConstants.YES.equals(params.getIsFlowFinish())) return true;
		// 一般不自动流转下一节点则需立刻开始任务
		if (FlowCommonConstants.YES.equals(params.getIsNowRun())) f.setStatus(NodeJobStatusEnum.RUN.getStatus());
		else f.setStatus(NodeJobStatusEnum.NO_START.getStatus());
		return true;
	}

	/**
	 * 校验当前userKey是否存在动态计算的任务
	 *
	 * @param params 分配参与者
	 */
	private List<RunJob> listRunJobs(DistPersonVO params) {
		Long flowInstId = params.getFlowInstId();
		String userKey = params.getUserKey();
		// 不同节点或同节点下限定的userKey标识
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, flowInstId)
				.eq(Objects.nonNull(params.getFlowNodeId()), RunJob::getFlowNodeId, params.getFlowNodeId())
				// .eq(Objects.nonNull(params.getNodeJobId()), RunJob::getNodeJobId, params.getNodeJobId())
				.eq(StrUtil.isNotBlank(userKey), RunJob::getUserKey, userKey)
				.isNull(StrUtil.isBlank(userKey), RunJob::getUserKey)
				.eq(RunJob::getValType, params.getValType()));
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException(ValidationExceptions.NO_USER_KEY_JOB);
		return runJobs;
	}

	/**
	 * 处理动态计算分配类型
	 *
	 * @param distPersonVOs 分配参与者
	 * @param noRunJobVOS   不存在运行任务
	 * @param runJob        任务记录
	 */
	private void handleCalculateType(DistPersonVO params, List<DistPersonVO> distPersonVOs, List<RunJobVO> noRunJobVOS, RunJob runJob) {
		// 更新节点的开始和结束时间
		distPersonVOs.stream().filter(f -> !FlowCommonConstants.YES.equals(f.getIsExistPerson()))
				.forEach(distPerson -> {
					// 判断是否已动态计算过分配参与者
					Optional<RunJobVO> noExist = noRunJobVOS.stream().filter(f -> StrUtil.isEmpty(f.getCalculateType())).findAny();
					if (noExist.isPresent()) {
						RunJobVO runJobVO = this.buildRunJobVO(noExist.get(), null, distPerson);
						// 判断流程是否结束，结束则直接让任务结束
						this.handleCalculateStatus(runJobVO, params);
						runJobVO.setCalculateType(CommonNbrPool.STR_0);
					} else {
						RunJobVO runJobVO = this.buildRunJobVO(null, runJob, distPerson);
						// 判断流程是否结束，结束则直接让任务结束
						this.handleCalculateStatus(runJobVO, params);
						runJobVO.setCalculateType(CommonNbrPool.STR_1);
						noRunJobVOS.add(runJobVO);
					}
				});
		// 最后需要判断小于则设置为跳过
		noRunJobVOS.stream().filter(f -> StrUtil.isEmpty(f.getCalculateType())).forEach(f -> {
			f.setStatus(NodeJobStatusEnum.SKIP.getStatus());
			f.setCalculateType(CommonNbrPool.STR_0);
		});
	}

	private RunJobVO buildRunJobVO(RunJobVO runJobVO, RunJob runJob, DistPersonVO distPerson) {
		if (Objects.isNull(runJobVO)) {
			runJobVO = new RunJobVO();
			BeanUtil.copyProperties(runJob, runJobVO, FlowEntityInfoConstants.ID, FlowEntityInfoConstants.START_TIME,
					FlowEntityInfoConstants.END_TIME, FlowEntityInfoConstants.CREATE_TIME);
			runJobVO.setCreateTime(LocalDateTime.now());
		} else {
			runJobVO.setStartTime(null);
			runJobVO.setEndTime(null);
		}
		runJobService.resetByDistPerson(distPerson, runJobVO);
		return runJobVO;
	}

	// 判断节点运行状态等信息
	private void handleCalculateStatus(RunJob runJob, DistPersonVO params) {
		String isNowRun = params.getIsNowRun();
		String isFlowFinish = params.getIsFlowFinish();
		if (FlowCommonConstants.YES.equals(isFlowFinish)) {
			if (Objects.isNull(runJob.getStartTime())) runJob.setStartTime(LocalDateTime.now());
			if (Objects.isNull(runJob.getEndTime())) runJob.setEndTime(LocalDateTime.now());
			runJob.setStatus(NodeJobStatusEnum.COMPLETE.getStatus());
		} else {
			// 一般不自动流转下一节点则需立刻开始任务
			if (FlowCommonConstants.YES.equals(isNowRun)) {
				if (Objects.isNull(runJob.getStartTime()))runJob.setStartTime(LocalDateTime.now());
				runJob.setStatus(NodeJobStatusEnum.RUN.getStatus());
			}
			// 都是新增
			else runJob.setStatus(NodeJobStatusEnum.NO_START.getStatus());
		}
	}

	/**
	 * 更新或新增分配参与者对应的任务
	 *
	 * @param params       参数
	 * @param existRunJobs 已存在运行任务
	 * @param noRunJobVOS  不存在运行任务
	 */
	private void updateOrAddRunJobs(DistPersonVO params, List<RunJob> existRunJobs, List<RunJobVO> noRunJobVOS) {
		// 更新分配参与者对应的任务
		List<RunJob> updateRunJobs = noRunJobVOS.stream().filter(f -> CommonNbrPool.STR_0.equals(f.getCalculateType())).map(m -> {
			RunJob runJob = new RunJob();
			BeanUtil.copyProperties(m, runJob);
			return runJob;
		}).collect(Collectors.toList());
		existRunJobs.addAll(updateRunJobs);
		if (CollUtil.isNotEmpty(existRunJobs)) runJobService.updateBatchById(existRunJobs);
		// 新增分配参与者对应的任务
		this.doUpdateOrAddRunJobs(params, existRunJobs, noRunJobVOS);
	}


	/**
	 * 更新或新增分配角色对应的任务
	 *
	 * @param params       参数
	 * @param existRunJobs 已存在运行任务
	 * @param noRunJobVOS  不存在运行任务
	 */
	private void doUpdateOrAddRunJobs(DistPersonVO params, List<RunJob> existRunJobs, List<RunJobVO> noRunJobVOS) {
		// 新增分配参与者对应的任务
		List<RunJob> addRunJobs = noRunJobVOS.stream().filter(f -> CommonNbrPool.STR_1.equals(f.getCalculateType())).map(m -> {
			RunJob runJob = new RunJob();
			BeanUtil.copyProperties(m, runJob);
			return runJob;
		}).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(addRunJobs)) {
			addRunJobs.forEach(each -> {
				runJobService.save(each);
				flowNodeRelService.handleRunJobFlowNodeRel(each);
			});
			// baseMapper.insertBatchSomeColumn(addRunJobs);
		}
		existRunJobs.addAll(addRunJobs);
		if (FlowCommonConstants.YES.equals(params.getIsFlowFinish())) return;
		// 排除掉setNextUsers设置的人员，避免重复开启和通知
		List<RunJob> runJobs = existRunJobs.stream().filter(f -> NodeJobStatusEnum.RUN.getStatus().equals(f.getStatus())).collect(Collectors.toList());
		if (CollUtil.isEmpty(runJobs)) return;
		this.notifyCalculateRunNodes(params, runJobs);
	}

	/**
	 * 判断是否需立刻开启或关闭对应的节点
	 *
	 * @param params       参数
	 * @param runJobs 已存在运行任务
	 */
	private void notifyCalculateRunNodes(DistPersonVO params, List<RunJob> runJobs) {
		if (CollUtil.isEmpty(runJobs)) return;
		// 开启当前节点后最近的节点
		if (Objects.nonNull(params.getFlowNodeId())) {
			List<RunJob> nextRunJobs = this.handleNextRunJobs(runJobs, params);
			Collection<RunJob> subtract = CollUtil.subtract(runJobs, nextRunJobs);
			if (CollUtil.isNotEmpty(subtract)) {
				List<Long> ids = subtract.stream().map(RunJob::getId).collect(Collectors.toList());
				runJobService.lambdaUpdate().set(RunJob::getStatus, NodeJobStatusEnum.NO_START.getStatus())
						.in(RunJob::getId, ids)
						.update();
			}
			runJobs = nextRunJobs;
		}
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException("下一步开始的任务不能为空");
		// 开启动态计算运行中任务的节点状态
		runNodeService.startCalculateRunNodes(runJobs, false);
		// 通知办理人
		RunJobVO runJobVO = new RunJobVO();
		BeanUtil.copyProperties(params, runJobVO, FlowEntityInfoConstants.ID);
		this.nextNotifyClazzes(runJobVO, runJobs, true, true);
	}

	/**
	 * 获取当前节点的下一顺序节点任务（串行/并行）
	 *
	 * @param allRunJobs 运行任务
	 * @param params     参数
	 */
	private List<RunJob> handleNextRunJobs(List<RunJob> allRunJobs, DistPersonVO params) {
		if (CollUtil.isEmpty(allRunJobs)) throw new ValidationException("当前流程不存在可用节点");
		if (Objects.isNull(params.getFlowNodeId())) return allRunJobs;
		// 获取当前流程节点关系
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listFlowNodeRels(params);
		List<Long> runNodeIds = allRunJobs.stream().map(RunJob::getRunNodeId).collect(Collectors.toList());
		List<RunNode> allRunNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, params.getFlowInstId())
				.in(RunNode::getId, runNodeIds));
		// 获取当前节点的下一顺序节点（串行/并行）
		Set<RunNode> nextRunNodes = this.handleVarNextRunNodes(allRunNodes, flowNodeRels, params.getFlowNodeId());
		if (CollUtil.isEmpty(nextRunNodes)) throw new ValidationException("当前节点的下一顺序节点不存在");
		// 获取当前节点的下一顺序节点任务（串行/并行）
		List<RunJob> nextRunJobs = allRunJobs.stream().filter(f -> nextRunNodes.stream().anyMatch(any -> any.getId().equals(f.getRunNodeId())))
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(nextRunJobs)) throw new ValidationException("当前节点的下一顺序节点任务不存在");
		return nextRunJobs;
	}

	@Override
	public boolean startRunJobByUserKey(DistPersonVO distPersonVO) {
		String valType = distPersonVO.getValType();
		String userKey = distPersonVO.getUserKey();
		if (StrUtil.isEmpty(userKey)) throw new ValidationException(ValidationExceptions.NO_USER_KEY);
		// 筛选出业务对应分配的节点
		List<Long> roleIds = distPersonVO.getStartRoleIds();
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, distPersonVO.getFlowInstId())
				.eq(RunJob::getUserKey, userKey)
				.eq(CollUtil.isNotEmpty(roleIds), RunJob::getRoleId, roleIds)
				.in(StrUtil.isNotEmpty(valType), RunJob::getValType, valType));
		// 开启当前节点后最近的节点
		runJobs = this.handleNextRunJobs(runJobs, distPersonVO);
		if (CollUtil.isEmpty(runJobs)) throw new ValidationException(ValidationExceptions.NO_USER_KEY_JOB);
		// 开启动态计算运行中任务的节点状态
		runNodeService.startCalculateRunNodes(runJobs, true);
		// 通知办理人
		RunJobVO runJobVO = new RunJobVO();
		BeanUtil.copyProperties(distPersonVO, runJobVO, FlowEntityInfoConstants.ID);
		this.nextNotifyClazzes(runJobVO, runJobs, true, true);
		return true;
	}

	@Override
	public void incrementCalculateRunJobs(DistPersonVO params, List<DistPerson> distPersons) {
		// 校验当前userKey是否存在动态计算的任务
		List<RunJob> runJobs = this.listRunJobs(params);
		List<DistPersonVO> distPersonVOs = this.buildDistPersonVOS(distPersons);
		// 处理多节点任务相同userKey
		Map<Long, List<RunJob>> listMap = runJobs.stream().collect(Collectors.groupingBy(RunJob::getFlowNodeId));
		List<RunJob> allExistRunJobs = CollUtil.newArrayList();
		List<RunJobVO> allNoRunJobVOS = CollUtil.newArrayList();
		listMap.forEach((key, val) -> {
			// 找出已存在的运行任务
			List<RunJob> existRunJobs = this.buildExistRunJobs(params, distPersonVOs, val);
			List<RunJobVO> noRunJobVOS = this.buildRunJobVOS(val, existRunJobs);
			// 处理动态计算分配类型
			this.incrementHandleCalculateType(distPersonVOs, noRunJobVOS, val.get(0), params);
			allExistRunJobs.addAll(existRunJobs);
			allNoRunJobVOS.addAll(noRunJobVOS);
		});
		// 更新或新增分配参与者对应的任务
		this.incrementUpdateOrAddRunJobs(params, allExistRunJobs, allNoRunJobVOS);
	}

	/**
	 * 处理动态计算分配类型
	 *
	 * @param distPersonVOs 分配参与者
	 * @param noRunJobVOS   不存在运行任务
	 * @param runJob        任务记录
	 * @param params        参数
	 */
	private void incrementHandleCalculateType(List<DistPersonVO> distPersonVOs, List<RunJobVO> noRunJobVOS, RunJob runJob, DistPersonVO params) {
		// 多余的角色都新建运行任务角色和办理人
		distPersonVOs.stream().filter(f -> !FlowCommonConstants.YES.equals(f.getIsExistPerson()))
				.forEach(distPerson -> {
					RunJobVO runJobVO = this.buildRunJobVO(null, runJob, distPerson);
					// 判断流程是否结束，结束则直接让任务结束
					this.handleCalculateStatus(runJobVO, params);
					runJobVO.setCalculateType(CommonNbrPool.STR_1);
					noRunJobVOS.add(runJobVO);
				});
	}

	/**
	 * 更新或新增分配参与者对应的任务
	 *
	 * @param params       参数
	 * @param existRunJobs 已存在运行任务
	 * @param noRunJobVOS  不存在运行任务
	 */
	private void incrementUpdateOrAddRunJobs(DistPersonVO params, List<RunJob> existRunJobs, List<RunJobVO> noRunJobVOS) {
		// 更新分配参与者对应的任务
		if (CollUtil.isNotEmpty(existRunJobs)) runJobService.updateBatchById(existRunJobs);
		// 新增分配参与者对应的任务
		this.doUpdateOrAddRunJobs(params, existRunJobs, noRunJobVOS);
	}

	@Override
	public Boolean removeById(Long defFlowId) {
		flowNodeService.remove(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, defFlowId));
		flowNodeRelService.removeByDefFlowId(defFlowId, null, null);
		nodeJobService.remove(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getDefFlowId, defFlowId));
		flowClazzService.removeByDefFlowId(defFlowId, null);
		flowRuleService.removeByDefFlowId(defFlowId, null);
		formOptionService.removeByDefFlowId(defFlowId, null, null);
		return Boolean.TRUE;
	}

}
