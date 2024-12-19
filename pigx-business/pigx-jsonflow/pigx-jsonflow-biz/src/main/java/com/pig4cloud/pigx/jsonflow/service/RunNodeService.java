package com.pig4cloud.pigx.jsonflow.service;

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

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;

import java.util.List;

/**
 * 运行节点管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:14
 */
public interface RunNodeService extends IService<RunNode> {

	/**
	 * 审批节点
	 *
	 * @param runJobVO 运行任务
	 */
	boolean complete(RunJobVO runJobVO);

	/**
	 * 新增或修改
	 *
	 * @param runNodeVO 流程节点设置
	 */
	Boolean saveOrUpdate(RunNodeVO runNodeVO, String fromType);

	/**
	 * 处理驳回逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean reject(RunJobVO runJobVO);

	/**
	 * 跳转逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	List<RunJob> anyJump(RunJobVO runJobVO);

	/**
	 * 更新符合条件节点状态
	 *
	 * @param runNodes 运行节点集合
	 * @param status   状态
	 */
	void handleRunNodeStatus(List<RunNode> runNodes, String status);

	void handleRunNodeStatusByRunNodes(List<RunNode> runNodes, String status);

	/**
	 * 完成节点并更新操作
	 * @param id 运行节点ID
	 * @param currRunNodeId 运行节点ID
	 * @param suspension 是否挂起
	 */
	void completeRunNodeStatus(Long id, Long currRunNodeId, String suspension);

	RunNode buildRunNode(RunJobVO runJobVO, RunNodeVO runNodeVO);

	/**
	 * 催办当前节点
	 * @param runNode 运行节点
	 */
	Boolean remind(RunNode runNode);

	/**
	 * 处理挂起逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean suspension(RunJobVO runJobVO);

	/**
	 * 通过流程id查询运行节点管理
	 *
	 * @param flowInstId 流程实例ID
	 */
	List<RunNode> listByFlowNodeId(Long flowInstId);

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
	 * 开启动态计算运行中任务的节点状态
	 *
	 * @param runJobs 运行任务集合
	 */
	boolean startCalculateRunNodes(List<RunJob> runJobs, boolean updateRunJob);

	/**
	 * 处理加签
	 *
	 * @param runJobVO 运行任务
	 */
	boolean signature(RunJobVO runJobVO);

}
