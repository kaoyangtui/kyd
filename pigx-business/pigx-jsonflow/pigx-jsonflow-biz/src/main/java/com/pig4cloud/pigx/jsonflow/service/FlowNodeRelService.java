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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeRelVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;

import java.util.List;

/**
 * 流程节点定义关系
 *
 * @author luolin
 * @date 2021-03-10 16:48:00
 */
public interface FlowNodeRelService extends IService<FlowNodeRel> {

	/**
	 * 保存或修改条件配置
	 *
	 * @param flowNodeRelVO 流程节点关系
	 * @param runJobVOS    节点任务关系
	 */
	Boolean saveOrUpdate(FlowNodeRelVO flowNodeRelVO, List<FlowNodeRelVO> linkList, List<RunJobVO> runJobVOS);

	/**
	 * 保存节点与任务关系
	 * @param each 运行任务
	 */
	void handleRunJobFlowNodeRel(RunJob each);

	/**
	 * 查询节点与任务的关系
	 * @param runJobs 运行任务集合
	 * @return List
	 */
	List<FlowNodeRel> listNodeJobRels(List<RunJob> runJobs);

	/**
	 * 查询节点与节点的关系
	 * @param runNodeVO 运行节点
	 * @return List
	 */
	List<FlowNodeRel> listFlowNodeRels(RunNodeVO runNodeVO);

	/**
	 * 查询节点与节点的关系
	 * @param defFlowId 流程定义ID
	 * @param flowInstId 流程实例ID
	 * @return List
	 */
	List<FlowNodeRel> listByDefFlowIdFlowInstId(Long defFlowId, Long flowInstId);

	/**
	 * 查询节点与节点的关系
	 * @param runNode 运行节点
	 * @return List
	 */
	List<FlowNodeRel> listFlowNodeRels(RunNode runNode);

	/**
	 * 查询节点与节点的关系
	 * @param distPersonVO 分配参与者
	 * @return List
	 */
	List<FlowNodeRel> listFlowNodeRels(DistPersonVO distPersonVO);

	List<FlowNodeRel> listAllFlowNodeRels(Long defFlowId, Long flowInstId);

	void removeByDefFlowId(Long defFlowId, Long flowInstId, List<Long> linkIds);

}
