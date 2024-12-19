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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.RunFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;

import java.util.List;
import java.util.Map;

/**
 * 流程管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:11
 */
public interface RunFlowService extends IService<RunFlow> {

	/**
	 * 查询流程实例管理
	 *
	 * @param page      分页对象
	 * @param runFlow   流程实例
	 * @param queryTime 查询时间
	 */
	IPage getPage(Page page, RunFlow runFlow, String[] queryTime);

	/**
	 * 修改流程实例管理
	 * @param runFlowVO 流程管理
	 * @return Boolean
	 */
	Boolean saveOrUpdate(RunFlowVO runFlowVO);

	/**
	 * 启动流程
	 *
	 * @param order  工单数据
	 * @param params 流程条件与分配参与者参数
	 */
	Boolean startFlow(Map<String, Object> order, Map<String, Object> params);

	/**
	 * 完成整个流程
	 *
	 * @param runJobVO 运行任务
	 */
	boolean complete(RunJobVO runJobVO);

	/**
	 * 子流程返回父流程，并更新关联数据
	 * @param currFlowInstId 当前流程实例ID
	 * @param parFlowInstId 父流程实例ID
	 */
    void backParFlow(Long currFlowInstId, String currFlowKey, Long parFlowInstId, String subFlowStatus);

    /**
	 * 提前结束流程
	 *
	 * @param runFlow 提前结束流程
	 */
	boolean earlyComplete(RunFlow runFlow);

	/**
	 * 终止/恢复流程
	 *
	 * @param runFlow 终止/恢复流程
	 */
	boolean terminateFlow(RunFlow runFlow);

	/**
	 * 作废流程
	 *
	 * @param runFlow 运行流程
	 */
	boolean invalidFlow(RunFlow runFlow);

	/**
	 * 获取流程图信息
	 * @param id 流程实例ID
	 * @return RunFlowVO
	 */
    RunFlowVO getNodesById(Long id, String isEdit);

    /**
	 * 工单发起异常后删除流程
	 *
	 * @param runJobVO 运行任务
	 */
	boolean delFlowInfo(RunJobVO runJobVO);

	/**
	 * 根据发起人ID查询
	 *
	 * @param initiatorId 发起人ID
	 * @param status      状态
	 */
	List<RunFlow> listFlowInstIds(Long initiatorId, List<String> status);

	/**
	 * 撤回或重置当前流程
	 * @param runFlow 运行流程
	 */
	Boolean recallReset(RunFlow runFlow);

	/**
	 * 催办当前流程
	 * @param runFlow 运行流程
	 */
	Boolean remind(RunFlow runFlow);

}
