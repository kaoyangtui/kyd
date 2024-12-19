package com.pig4cloud.pigx.jsonflow.api.vo;

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

import com.pig4cloud.pigx.jsonflow.api.entity.TabsOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 已办任务记录
 *
 * @author luolin
 * @date 2021-03-03 09:26:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务记录")
public class ToDoneJobVO extends RunJobVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 任务限时
	 */
	@Schema(description = "任务限时")
	private String timeLimit;

	// -------------------查询信息-------------------
	/**
	 * 查询时间
	 */
	@Schema(description = "查询时间")
	private String[] queryTime;

	/**
	 * 申请人
	 */
	@Schema(description = "申请人")
	private Long initiatorId;

	// -------------------节点信息-------------------

	/**
	 * 审批页面
	 */
	private List<TabsOption> elTabs;

	//-------------------工单信息-------------------

	/**
	 * 工单信息
	 */
	private Map<String, Object> order;
	/**
	 * 工单CODE
	 */
	private String code;

	//-------------------流程信息-------------------

	@Schema(description = "父流程实例ID")
	private Long parFlowInstId;

	/**
	 * 作废原因
	 */
	@Schema(description = "作废原因")
	private String invalidReason;

	/**
	 * 状态 -2暂存 -1运行中 0完成 1作废 2撤回
	 */
	@Schema(description = "状态 -2暂存 -1运行中 0完成 1作废 2撤回")
	private String flowStatus;

	/**
	 * 用时
	 */
	@Schema(description = "用时")
	private String useTime;

	/**
	 * 查询任务类型 0 已完成任务 1 公共任务
	 */
	@Schema(description = "是否查询公共任务")
	private String queryJobType;

}
