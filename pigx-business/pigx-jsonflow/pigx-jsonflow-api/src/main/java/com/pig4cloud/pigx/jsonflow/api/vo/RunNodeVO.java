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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运行节点管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "运行节点管理")
public class RunNodeVO extends RunJobVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 流程ID
	 */
	@NotNull(message = "流程ID不能为空")
	@Schema(description = "流程ID")
	private Long flowInstId;
	
	/**
	 * 节点子流程实例ID
	 */
	@Schema(description = "节点子流程实例ID")
	private Long subFlowInstId;
	/**
	 * 开启子流程的任务ID
	 */
	@Schema(description = "开启子流程的任务ID")
	private Long subRunJobId;
	/**
	 * 节点ID
	 */
	@NotNull(message = "节点ID不能为空")
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 是否挂起（0否 1是）
	 */
	@Schema(description = "是否挂起（0否 1是）")
	private String suspension;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private LocalDateTime endTime;

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

	/**
	 * 更新人
	 */
	@Schema(description = "更新人")
	private Long updateUser;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 业务KEY
	 */
	@NotBlank(message = "业务KEY不能为空")
	@Schema(description = "业务KEY")
	private String flowKey;

	/**
	 * 任务时限
	 */
	@Schema(description = "任务时限")
	private Integer timeout;

	/**
	 * 节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟
	 */
	@NotBlank(message = "节点类型不能为空")
	@Schema(description = "节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟")
	private String nodeType;

	/**
	 * 流程定义ID
	 */
	@NotNull(message = "流程定义ID不能为空")
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 节点子流程定义ID
	 */
	@Schema(description = "节点子流程定义ID")
	private Long subDefFlowId;
	
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 节点状态 -1：未开始 0：运行中 1：结束 2：驳回中 3：跳过 9：被驳回
	 */
	@NotBlank(message = "节点状态不能为空")
	@Schema(description = "节点状态 -1：未开始 0：运行中 1：结束  2：驳回中 3：跳过 9：被驳回")
	private String status;

	/**
	 * 多节点审批方式
	 */
	@Schema(description = "多节点审批方式")
	private String nodeApproveMethod;

	/**
	 * 多节点组ID
	 */
	@Schema(description = "多节点组ID")
	private Long nodeGroupId;

	/**
	 * 驳回类型 0 依次返回 1 直接返回
	 */
	@Schema(description = "驳回类型 0 依次返回  1 直接返回")
	private String rejectType;

	/**
	 * 不满足自身条件是否继续流转下一节点 0 否 1 是
	 */
	@Schema(description = "不满足自身条件是否继续流转下一节点 0 否  1 是")
	private String isContinue;

	/**
	 * 是否自动流转下一节点 0否 1是
	 */
	@Schema(description = "是否自动流转下一节点 0否 1是")
	private String isAutoNext;

	/**
	 * 是否自动审批 0否 1是
	 */
	@Schema(description = "是否自动审批 0否 1是")
	private String isAutoAudit;

	/**
	 * 多人审批方式(0会签、1或签、2依次审批）
	 */
	@Schema(description = "多人审批方式(0会签、1或签、2依次审批）")
	private String approveMethod;

	/**
	 * 开启的节点ID
	 */
	@Schema(description = "开启的节点ID")
	private String[] toRunNodeIds;

	/**
	 * 来源的节点ID
	 */
	@Schema(description = "来源的节点ID")
	private String[] fromRunNodeIds;

	/**
	 * 下一步节点操作状态 0：未操作 1：已操作
	 */
	@Schema(description="下一步节点操作状态 0：未操作 1：已操作")
	private String nextStatus;

	/**
	 * 当前节点操作状态 0：未操作 1：已操作
	 */
	@Schema(description="当前节点操作状态 0：未操作 1：已操作")
	private String currStatus;
	
	/**
	 * 审批按钮权限
	 */
	@Schema(description = "审批按钮权限")
	private String[] jobBtns;

	/**
	 * 抄送用户ID
	 */
	@Schema(description = "抄送用户ID")
	private String[] carbonCopy;

	/**
	 * 相同审批人自动通过 0：否 1：是
	 */
	@Schema(description = "相同审批人自动通过 0：否 1：是")
	private String isPassSame;

	/**
	 * 子流程状态
	 */
	@Schema(description="子流程状态")
	private String subFlowStatus;

	/**
	 * PC待办页面路径（多个逗号隔开）
	 */
	@Schema(description = "PC待办页面路径（多个逗号隔开）")
	private String[] pcTodoUrl;
	/**
	 * PC完成页面路径（多个逗号隔开）
	 */
	@Schema(description = "PC完成页面路径（多个逗号隔开）")
	private String[] pcFinishUrl;

	/**
	 * 节点名称
	 */
	@Schema(description = "节点名称")
	private String nodeName;

	/**
	 * 是否为网关 0：否 1：是
	 */
	@Schema(description = "是否为网关 0：否 1：是")
	private String isGateway;

	/**
	 * 节点描述
	 */
	@Schema(description = "节点描述")
	private String description;

	/**
	 * 节点位置大小
	 */
	@Schema(description = "节点位置大小")
	private String positionSize;

	//----------------------流程实例图参数----------------------
	/**
	 * 运行节点ID
	 */
	@Schema(description ="运行节点ID")
	private Long runNodeId;

}
