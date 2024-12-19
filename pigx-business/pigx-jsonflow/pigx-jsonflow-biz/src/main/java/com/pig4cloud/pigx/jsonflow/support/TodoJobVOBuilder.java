package com.pig4cloud.pigx.jsonflow.support;

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
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.vo.TodoJobVO;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luolin
 * @date 2020/2/15 16:06
 */
@UtilityClass
public class TodoJobVOBuilder {

	/**
	 * 构造流程与任务信息
	 *
	 * @param jobList 运行任务
	 * @param runFlows 运行流程
	 */
	public List<TodoJobVO> handleAllFlowKey(List<RunJob> jobList, List<RunFlow> runFlows) {
		return jobList.stream().map(job -> {
			TodoJobVO res = new TodoJobVO();
			TodoJobVOBuilder.toTodoJobVO(res, job);
			// 流程信息
			TodoJobVOBuilder.flowToTodoJobVO(res, job, runFlows);
			return res;
		}).collect(Collectors.toList());
	}

	/**
	 * 构造 TodoJobVO
	 *
	 * @param res 返回数据
	 * @param job 任务
	 */
	private void toTodoJobVO(TodoJobVO res, RunJob job) {
		BeanUtil.copyProperties(job, res);
		if (Objects.nonNull(job.getTimeout()) && job.getTimeout() > 0) {
			String between = DateUtil.formatBetween(job.getTimeout() * 60 * 1000, BetweenFormatter.Level.SECOND);
			res.setTimeLimit(between);
		} else {
			res.setTimeLimit("无限时");
		}
	}

	/**
	 * 构造 TodoJobVO
	 *
	 * @param res 返回数据
	 * @param job 任务
	 * @param runFlows 运行流程
	 */
	private void flowToTodoJobVO(TodoJobVO res, RunJob job, List<RunFlow> runFlows) {
		runFlows.stream().filter(f -> f.getId().equals(job.getFlowInstId())).findAny().ifPresent(f -> {
			res.setCode(f.getCode());
			res.setParFlowInstId(f.getParFlowInstId());
			res.setInitiatorId(f.getInitiatorId());
			res.setOrderId(f.getOrderId());
		});
	}

	/**
	 * 构造 TodoJobVO
	 *
	 * @param res 返回数据
	 * @param runFlow 运行流程
	 */
	public void flowToTodoJobVO(TodoJobVO res, RunFlow runFlow) {
		res.setCode(runFlow.getCode());
		res.setParFlowInstId(runFlow.getParFlowInstId());
		res.setInitiatorId(runFlow.getInitiatorId());
		res.setOrderId(runFlow.getOrderId());
	}

}
