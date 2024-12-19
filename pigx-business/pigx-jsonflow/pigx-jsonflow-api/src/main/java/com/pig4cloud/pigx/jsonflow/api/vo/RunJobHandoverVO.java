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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务交接VO
 *
 * @author luolin
 * @date 2020-02-03 16:43:33
 */
@Data
@Schema(description = "任务交接VO")
@JsonIgnoreProperties(value = "handler")
public class RunJobHandoverVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 运行任务ID
	 */
	@NotNull(message = "运行任务ID不能为空")
	@Schema(description = "运行任务ID")
	private Long runJobId;

	/**
	 * 工单编号
	 */
	@Schema(description="工单编号")
	private String code;

	/**
	 * 工单id
	 */
	@NotNull(message = "工单ID不能为空")
	@Schema(description = "工单id")
	private Long orderId;

	/**
	 * 流程ID
	 */
	@NotNull(message = "流程ID不能为空")
	@Schema(description = "流程ID")
	private Long flowInstId;

	/**
	 * 业务KEY
	 */
	@NotBlank(message = "业务KEY不能为空")
	@Schema(description = "业务KEY")
	private String flowKey;

	/**
	 * 流程发起人ID
	 */
	@NotNull(message = "流程发起人ID不能为空")
	@Schema(description = "流程发起人ID")
	private Long initiatorId;

	/**
	 * 节点任务定义ID
	 */
	@NotNull(message = "节点任务定义ID不能为空")
	@Schema(description = "节点任务定义ID")
	private Long nodeJobId;

	/**
	 * 节点定义ID
	 */
	@NotNull(message = "节点定义ID不能为空")
	@Schema(description = "节点定义ID")
	private Long runNodeId;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private LocalDateTime startTime;

	// ---------------------------------工作交接状态，默认0------------------------------
	/**
	 * 工作交接状态(状态 -2: 未交接 -1 交接中 0：已交接 1：流程作废）
	 */
	@Schema(description = "工作交接状态(状态 -2: 未交接 -1 交接中 0：已交接 1：流程作废）")
	private String status;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	private Long createUser;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

}
