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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.ValidationExceptions;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeRelVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.mapper.FlowNodeRelMapper;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeRelService;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程节点定义关系
 *
 * @author luolin
 * @date 2021-03-10 16:48:00
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowNodeRelServiceImpl extends ServiceImpl<FlowNodeRelMapper, FlowNodeRel> implements FlowNodeRelService {

	private final FlowNodeService flowNodeService;
	private final RunNodeService runNodeService;

	/**
	 * 串行节点、并行节点、结束节点有上一节点
	 * 虚拟节点、开始节点不能有上一节点
	 * <p>
	 * 串行节点、并行节点、开始节点有下一节点
	 * 虚拟节点、结束节点不能有下一节点
	 *
	 * @param flowNodeRelVO 流程节点关系
	 * @return Boolean
	 */
	@Override
	public Boolean saveOrUpdate(FlowNodeRelVO flowNodeRelVO, List<FlowNodeRelVO> linkList, List<RunJobVO> runJobVOS) {
		// 检验节点或任务配置条件
		if (StrUtil.isNotBlank(flowNodeRelVO.getVarKeyVal())) this.validateFlowCondition(flowNodeRelVO);

		// 来源节点不能是虚拟节点、结束节点
		String nodeType;
		if (Objects.isNull(flowNodeRelVO.getFlowInstId())) {
			FlowNode fromNode = flowNodeService.getById(flowNodeRelVO.getFromFlowNodeId());
			nodeType = fromNode.getNodeType();
		} else {
			RunNode fromNode = runNodeService.getOne(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowNodeId, flowNodeRelVO.getFromFlowNodeId())
					.eq(RunNode::getFlowInstId, flowNodeRelVO.getFlowInstId()));
			nodeType = fromNode.getNodeType();
		}
		if (StrUtil.equals(nodeType, NodeTypeEnum.VIRTUAL.getType()) || StrUtil.equals(nodeType, NodeTypeEnum.END.getType())) {
			throw new ValidationException(ValidationExceptions.NOT_FROM_FLOW_NODE);
		}
		// 设置节点定义ID
		flowNodeRelVO.setFromNodeType(nodeType);

		if (CommonNbrPool.STR_0.equals(flowNodeRelVO.getType())) {
			if (Objects.isNull(flowNodeRelVO.getToFlowNodeId())) throw new ValidationException(ValidationExceptions.PLEASE_TO_FLOW_NODE);
			if (flowNodeRelVO.getToFlowNodeId().equals(flowNodeRelVO.getFromFlowNodeId())) throw new ValidationException("来源节点和下一节点不能是同一节点");
			// TODO DFS算法判断有向图环路径
			// 判断来源节点出度关联节点的类型是否一致
			List<FlowNodeRelVO>	toFlowNodeRelVos = linkList.stream().filter(f -> f.getSourceId().equals(flowNodeRelVO.getFromFlowNodeId())).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(runJobVOS)) {
				// 排除独立任务
				toFlowNodeRelVos = toFlowNodeRelVos.stream().filter(f -> runJobVOS.stream().noneMatch(any -> any.getId().equals(f.getTargetId()))).collect(Collectors.toList());
			}
			List<Long> toFlowNodeIds = toFlowNodeRelVos.stream().map(FlowNodeRelVO::getTargetId).collect(Collectors.toList());
			List<FlowNode> toFlowNodes;
			if (Objects.nonNull(flowNodeRelVO.getFlowInstId())) {
				List<RunNode> runNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowNodeId, toFlowNodeIds)
						.eq(RunNode::getFlowInstId, flowNodeRelVO.getFlowInstId()));
				toFlowNodes = runNodes.stream().map(m -> {
						FlowNode flowNode = new FlowNode();
						BeanUtil.copyProperties(m, flowNode);
						return flowNode;
					}).collect(Collectors.toList());
			} else toFlowNodes = flowNodeService.listByIds(toFlowNodeIds);
			// 判断节点类型
			boolean start = toFlowNodes.stream().anyMatch(any -> NodeTypeEnum.START.getType().equals(any.getNodeType()));
			boolean serial = toFlowNodes.stream().anyMatch(any -> NodeTypeEnum.SERIAL.getType().equals(any.getNodeType()));
			boolean parallel = toFlowNodes.stream().anyMatch(any -> NodeTypeEnum.PARALLEL.getType().equals(any.getNodeType()));
			boolean end = toFlowNodes.stream().anyMatch(any -> NodeTypeEnum.END.getType().equals(any.getNodeType()));
			// 注意：下一步节点不能同时存在开始节点或串行节点或并行节点或结束节点，必须都存在条件
			if (end && parallel) this.validateNodeTypes(toFlowNodes, toFlowNodeRelVos, "下一节点同时存在结束或并行节点时");
			if (parallel && serial) this.validateNodeTypes(toFlowNodes, toFlowNodeRelVos, "下一节点同时存在并行或串行节点时");
			if (end && serial) this.validateNodeTypes(Collections.emptyList(), toFlowNodeRelVos, "下一节点同时存在结束或串行节点时");
			if (end && start) this.validateNodeTypes(Collections.emptyList(), toFlowNodeRelVos, "下一节点同时存在结束或开始节点时");
			if (start && parallel) this.validateNodeTypes(toFlowNodes, toFlowNodeRelVos, "下一节点同时存在并行或开始节点时");
			if (start && serial) this.validateNodeTypes(Collections.emptyList(), toFlowNodeRelVos, "下一节点同时存在开始或串行节点时");
			// 判断下一节点不能同时存在多个串行节点
			List<FlowNode> flowNodes = toFlowNodes.stream().filter(f -> NodeTypeEnum.SERIAL.getType().equals(f.getNodeType())).collect(Collectors.toList());
			if (serial && flowNodes.size() > 1) this.validateNodeTypes(Collections.emptyList(), toFlowNodeRelVos, "下一节点同时存在多个串行节点时");
			// 下一节点不能是虚拟节点、开始节点
			String toNodeType;
			if (Objects.nonNull(flowNodeRelVO.getFlowInstId())) {
				RunNode toNode = runNodeService.getOne(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowNodeId, flowNodeRelVO.getToFlowNodeId())
						.eq(RunNode::getFlowInstId, flowNodeRelVO.getFlowInstId()));
				toNodeType = toNode.getNodeType();
			} else {
				FlowNode toNode = flowNodeService.getById(flowNodeRelVO.getToFlowNodeId());
				toNodeType = toNode.getNodeType();
			}
			if (StrUtil.equals(toNodeType, NodeTypeEnum.VIRTUAL.getType()) || StrUtil.equals(toNodeType, NodeTypeEnum.START.getType())) {
				throw new ValidationException(ValidationExceptions.TO_FROM_FLOW_NODE);
			}
			flowNodeRelVO.setToNodeType(toNodeType);
		}
		// 保存或修改
		FlowNodeRel flowNodeRel = new FlowNodeRel();
		BeanUtil.copyProperties(flowNodeRelVO, flowNodeRel);
		return super.saveOrUpdate(flowNodeRel);
	}

	/**
	 * 检验节点或任务配置条件
	 * @param flowNodeRelVO 当前节点关系
	 */
	private void validateFlowCondition(FlowNodeRelVO flowNodeRelVO){
		// 校验节点与任务 条件类型是否正确
		Long toNodeJobId = flowNodeRelVO.getToNodeJobId();
		if (Objects.nonNull(toNodeJobId)) {
			if (flowNodeRelVO.getType().equals(CommonNbrPool.STR_0)) throw new ValidationException("任务条件类型必须为任务条件");
		} else {
			if (flowNodeRelVO.getType().equals(CommonNbrPool.STR_1)) throw new ValidationException("节点条件类型必须为节点条件");
		}
		// 来源节点不能是虚拟节点、结束节点

		if (CommonNbrPool.STR_0.equals(flowNodeRelVO.getType())) {
			// 下一节点不能是虚拟节点、开始节点

		} else {
			// 设置任务定义ID
			if (Objects.nonNull(toNodeJobId)) {
			}
		}
	}

	/**
	 * 校验下一节点同时存在多种节点时，判断是否有条件
	 * @param toFlowNodeRelVos 到达节点关系集合
	 * @param message 提示语
	 */
	private void validateNodeTypes(List<FlowNode> toFlowNodes, List<FlowNodeRelVO> toFlowNodeRelVos, String message) {
		toFlowNodeRelVos.forEach(each -> {
			if (StrUtil.isBlank(each.getVarKeyVal())) throw new ValidationException(message + "，必须都存在条件");
			// TODO 判断多条件组条件是否相同
		});
	}

	@Override
	public void handleRunJobFlowNodeRel(RunJob each) {
		// TODO 目前回显不单独显示
		/*Long flowInstId = each.getFlowInstId();
		if (CommonNbrPool.STR_2.equals(each.getFromType())) {
			FlowNodeRel flowNodeRel = new FlowNodeRel();
			flowNodeRel.setDefFlowId(each.getDefFlowId());
			flowNodeRel.setFlowKey(each.getFlowKey());
			// 判断是否配置数据独立
			flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
			flowNodeRel.setFlowInstId(flowInstId);
			flowNodeRel.setFromFlowNodeId(each.getFlowNodeId());
			RunNode runNode = runNodeService.getById(each.getRunNodeId());
			flowNodeRel.setFromNodeType(runNode.getNodeType());
			flowNodeRel.setType(CommonNbrPool.STR_1);
			flowNodeRel.setToNodeJobId(each.getNodeJobId());
			flowNodeRel.setCreateUser(SecurityUtils.getUser().getId());
			flowNodeRel.setCreateTime(LocalDateTime.now());
			this.save(flowNodeRel);
		}*/
	}

	@Override
	public List<FlowNodeRel> listNodeJobRels(List<RunJob> runJobs) {
		Long defFlowId = runJobs.get(0).getDefFlowId();
		Long flowInstId = runJobs.get(0).getFlowInstId();
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		List<Long> nodeJobIds = runJobs.stream().map(RunJob::getNodeJobId).collect(Collectors.toList());
		return this.list(Wrappers.<FlowNodeRel>lambdaQuery().eq(FlowNodeRel::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowNodeRel::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowNodeRel::getFlowInstId)
				.in(FlowNodeRel::getToNodeJobId, nodeJobIds)
				.eq(FlowNodeRel::getType, CommonNbrPool.STR_1));
	}

	@Override
	public List<FlowNodeRel> listFlowNodeRels(RunNodeVO runNodeVO) {
		return this.listByDefFlowIdFlowInstId(runNodeVO.getDefFlowId(), runNodeVO.getFlowInstId());
	}

	@Override
	public List<FlowNodeRel> listByDefFlowIdFlowInstId(Long defFlowId, Long flowInstId) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		return this.list(Wrappers.<FlowNodeRel>lambdaQuery().eq(FlowNodeRel::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowNodeRel::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowNodeRel::getFlowInstId)
				.isNotNull(FlowNodeRel::getToFlowNodeId)
				.eq(FlowNodeRel::getType, CommonNbrPool.STR_0));
	}

	@Override
	public List<FlowNodeRel> listFlowNodeRels(RunNode runNode) {
		return this.listByDefFlowIdFlowInstId(runNode.getDefFlowId(), runNode.getFlowInstId());
	}

	@Override
	public List<FlowNodeRel> listFlowNodeRels(DistPersonVO distPersonVO) {
		return this.listByDefFlowIdFlowInstId(distPersonVO.getDefFlowId(), distPersonVO.getFlowInstId());
	}

	@Override
	public List<FlowNodeRel> listAllFlowNodeRels(Long defFlowId, Long flowInstId) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		return this.list(Wrappers.<FlowNodeRel>lambdaQuery().eq(FlowNodeRel::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowNodeRel::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowNodeRel::getFlowInstId));
	}

	@Override
	public void removeByDefFlowId(Long defFlowId, Long flowInstId, List<Long> linkIds) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		this.remove(Wrappers.<FlowNodeRel>lambdaQuery().notIn(CollUtil.isNotEmpty(linkIds), FlowNodeRel::getId, linkIds)
				.eq(FlowNodeRel::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowNodeRel::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowNodeRel::getFlowInstId));
	}

}
