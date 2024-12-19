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
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;
import lombok.experimental.UtilityClass;

import jakarta.validation.ValidationException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luolin
 * @date 2020/2/15 16:06
 */
@UtilityClass
public class ToDoneJobVOBuilder {

	/**
	 * 构造流程与任务信息
	 *
	 * @param jobList 运行任务
	 * @param runFlows 运行流程
	 */
	public List<ToDoneJobVO> handleAllFlowKey(List<RunJob> jobList, List<RunFlow> runFlows, List<RunNode> runNodes) {
		return jobList.stream().map(job -> {
			ToDoneJobVO res = new ToDoneJobVO();
			ToDoneJobVOBuilder.toToDoneJobVO(res, job, runNodes);
			// 流程信息
			ToDoneJobVOBuilder.flowToToDoneJobVO(res, job, runFlows);
			return res;
		}).collect(Collectors.toList());
	}

	/**
	 * 构造 ToDoneJobVO
	 *
	 * @param res 返回数据
	 * @param job 任务
	 */
	private void toToDoneJobVO(ToDoneJobVO res, RunJob job, List<RunNode> runNodes) {
		BeanUtil.copyProperties(job, res);
		ToDoneJobVOBuilder.extractedTimeLimit(res, job);
		// 计算用时
		if (NodeJobStatusEnum.RUN.getStatus().equals(job.getStatus())) res.setUseTime(FlowCommonConstants.AUDITING);
		else if (NodeJobStatusEnum.COMPLETE.getStatus().equals(job.getStatus())) {
			Optional<RunNode> any = runNodes.stream().filter(f -> f.getId().equals(job.getRunNodeId())).findAny();
			RunNode runNode = any.orElseThrow(() -> new ValidationException("任务关联的节点不存在"));
			// 当前节点的任务都能看到子流程
			res.setSubFlowInstId(runNode.getSubFlowInstId());
			if (!FlowCommonConstants.YES.equals(runNode.getIsAutoAudit())) {
				LocalDateTime startTime = job.getStartTime();
				LocalDateTime endTime = job.getEndTime();
				if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
					Duration duration = Duration.between(startTime, endTime);
					String between = DateUtil.formatBetween(duration.getSeconds() * 1000, BetweenFormatter.Level.SECOND);
					res.setUseTime(between);
				}
			} else {
				res.setUseTime(FlowCommonConstants.AUTO_AUDIT);
			}
		}
	}

	/**
	 * 计算任务时限
	 * @param res 待办任务
	 * @param job 运行任务
	 */
	private void extractedTimeLimit(ToDoneJobVO res, RunJob job) {
		// 计算任务时限
		if (Objects.nonNull(job.getTimeout()) && job.getTimeout() > 0) {
			String between = DateUtil.formatBetween(job.getTimeout() * 60 * 1000, BetweenFormatter.Level.SECOND);
			res.setTimeLimit(between);
		} else {
			res.setTimeLimit("无限时");
		}
	}

	/**
	 * 构造 ToDoneJobVO
	 *
	 * @param res 返回数据
	 * @param job 任务
	 * @param runFlows 运行流程
	 */
	private void flowToToDoneJobVO(ToDoneJobVO res, RunJob job, List<RunFlow> runFlows) {
		runFlows.stream().filter(f -> f.getId().equals(job.getFlowInstId()))
			.findAny().ifPresent(f -> {
				res.setParFlowInstId(f.getParFlowInstId());
				res.setInitiatorId(f.getInitiatorId());
				res.setOrderId(f.getOrderId());
				res.setInvalidReason(f.getInvalidReason());
				res.setFlowStatus(f.getStatus());
			});
	}

	/**
	 * 构造 ToDoneJobVO
	 *
	 * @param res 返回数据
	 * @param runFlow 运行流程
	 */
	public void flowToToDoneJobVO(ToDoneJobVO res, RunFlow runFlow) {
		res.setCode(runFlow.getCode());
		res.setParFlowInstId(runFlow.getParFlowInstId());
		res.setInitiatorId(runFlow.getInitiatorId());
		res.setOrderId(runFlow.getOrderId());
	}

}
