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


import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.entity.RunReject;
import com.pig4cloud.pigx.jsonflow.api.vo.DefFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;

import java.util.List;
import java.util.Set;

/**
 * JsonFlow工作流引擎接口
 *
 * @author luolin
 * @date 2020/2/7
 */
public interface JsonFlowEngineService {

	/**
	 * 代码生成流程例子
	 */
	Boolean testCodeGen();

	/**
	 * 代码更新流程例子
	 */
	Boolean testCodeUpdate();

	/**
	 * 获取流程图信息
	 * @param defFlowId 流程ID
	 * @return DefFlowVO
	 */
	DefFlowVO getNodesByDefFlowId(Long defFlowId);

	/**
	 * 获取流程图信息
	 * @param flowInstId 流程实例ID
	 * @return RunFlowVO
	 */
    RunFlowVO getNodesByFlowInstId(Long flowInstId, String isEdit);

    /**
	 * 保存和更新流程设计
	 * @param defFlowVO 定义流程
	 */
	Boolean saveOrUpdateDefFlow(DefFlowVO defFlowVO);

    /**
	 * 保存和更新流程实例设计
	 * @param runFlowVO 流程实例
	 */
	Boolean saveOrUpdateRunFlow(RunFlowVO runFlowVO);;

	/**
	 * 当前流程实例配置数据独立
	 * @param runFlowVO 流程实例
	 */
	void handleIndependent(RunFlowVO runFlowVO);

	/**
	 * 发起时记录角色和办理人
	 *
	 * @param runNodes 运行节点集合
	 */
	RunJobVO recordRunJobs(List<RunNode> runNodes);

	/**
	 * 处理加签类型
	 *
	 * @param runJobVO 运行任务
	 */
	boolean handleSignatureType(RunJobVO runJobVO);

	/**
	 * 处理节点加签类型
	 *
	 * @param runJobVO 运行任务
	 */
	boolean handleNodeSignatureType(RunJobVO runJobVO);

	/**
	 * 任务审批
	 *
	 * @param runJobVO 运行任务
	 */
	boolean completeRunJob(RunJobVO runJobVO);

	/**
	 * 下一开始并行节点的任务并设置及通知办理人
	 *
	 * @param runJobVO     运行任务
	 * @param nextRunNodes 下一运行节点集合
	 * @param runRejects   驳回运行节点/任务
	 */
	boolean matchRunJobs(RunJobVO runJobVO, List<RunNode> nextRunNodes, List<RunReject> runRejects, boolean isExecStart, boolean isSaveRel);

	/**
	 * 通知待办任务，执行开始监听逻辑
	 * @param runJobVO 运行任务
	 * @param runJobs 下一步开启任务
	 * @param isExecStart 是否执行开始监听逻辑
	 */
	void nextNotifyClazzes(RunJobVO runJobVO, List<RunJob> runJobs, boolean isExecStart, boolean isSaveRel);

	/**
	 * 处理跳转逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean anyJump(RunJobVO runJobVO);

	/**
	 * 处理退回上一步
	 *
	 * @param runJobVO 运行任务
	 */
	boolean backPreJob(RunJobVO runJobVO);

	/**
	 * 处理退回首节点
	 *
	 * @param runJobVO 运行任务
	 */
	boolean backFirstJob(RunJobVO runJobVO);

	/**
	 * 处理驳回逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean reject(RunJobVO runJobVO);

	/**
	 * 记录整个流程的节点信息
	 *
	 * @param runFlowVO 运行流程
	 */
	RunJobVO recordRunNodes(RunFlowVO runFlowVO);

	/**
	 * 审批节点
	 *
	 * @param runJobVO 运行任务
	 */
	boolean completeRunNode(RunJobVO runJobVO);

	/**
	 * 获取当前节点的下一顺序节点（串行/并行）
	 *
	 * @param allRunNodes  运行节点集合
	 * @param flowNodeRels 流程节点关系集合
	 * @param flowNodeId   流程节点ID
	 */
	List<RunNode> handleNextRunNodes(List<RunNode> allRunNodes, List<FlowNodeRel> flowNodeRels, Long flowNodeId);

	/**
	 * 开启节点关联子流程
	 * @param runJobVO 运行任务
	 * @param runNodeVO 运行节点
	 * @return Long
	 */
	Long startSubFlow(RunJobVO runJobVO, RunNodeVO runNodeVO);

	/**
	 * 重入或重启子流程，更新关联数据
	 * @param runJobVO 运行任务
	 * @param runNodeVO 运行节点
	 * @return Long
	 */
	void restartSubFlow(RunJobVO runJobVO, RunNodeVO runNodeVO);

	/**
	 * 获取作为来源的运行节点
	 *
	 * @param runNodes     运行节点集合
	 * @param res          返回数据
	 * @param fromRunNodes 来源运行节点集合
	 */
	List<RunNode> filterNextRunNodes(List<RunNode> runNodes, Set<RunNode> res, List<FlowNodeRel> fromRunNodes, List<FlowNodeRel> flowNodeRels);

	/**
	 * 通过流程id查询小于当前运行节点的节点
	 *
	 * @param flowInstId 流程实例ID
	 */
	List<RunNode> listPreByFlowNodeId(Long flowInstId, Long runNodeId);

	/**
	 * 通过流程id查询任意运行节点
	 *
	 * @param flowInstId 流程实例ID
	 */
	List<RunNode> listAnyJumpByFlowNodeId(Long flowInstId, Long runNodeId);

	/**
	 * 根据参与者动态计算全量任务
	 * 动态计算任务及参与者（可能存在无用的已跳过任务）
	 *
	 * @param params 分配参与者
	 * @param distPersons  分配参与者
	 */
	void calculateAllRunJobs(DistPersonVO params, List<DistPerson> distPersons);
	// 此处只计算当前节点下的任务
	List<RunJob> setNextAllRunJobsByDistPerson(RunJob runJob, List<DistPerson> distPersons);

	/**
	 * 根据参与者动态计算增量任务
	 *
	 * @param params 分配参与者
	 * @param distPersons  分配参与者
	 */
	void incrementCalculateRunJobs(DistPersonVO params, List<DistPerson> distPersons);

	/**
	 * 开启当前userKey节点任务
	 *
	 * @param distPersonVO 分配参与者
	 */
	boolean startRunJobByUserKey(DistPersonVO distPersonVO);

	/**
	 * 删除流程
	 * @param defFlowId 流程ID
	 */
    Boolean removeById(Long defFlowId);

}
