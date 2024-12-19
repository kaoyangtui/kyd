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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 节点批注管理
 *
 * @author luolin
 * @date 2021-02-25 16:41:52
 */
@Data
@Schema(description = "节点批注管理")
public class CommentVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 流程ID
	 */
	@Schema(description = "流程ID")
	private Long flowInstId;

	/**
	 * 业务KEY
	 */
	@Schema(description = "业务KEY")
	private String flowKey;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 节点ID
	 */
	@Schema(description = "节点ID")
	private Long runNodeId;

	/**
	 * 运行任务ID
	 */
	@Schema(description = "运行任务ID")
	private Long runJobId;

	/**
	 * 审批意见
	 */
	@Schema(description = "审批意见")
	private String remark;

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
	 * 节点定义ID
	 */
	@Schema(description = "节点定义ID")
	private Long flowNodeId;

	/**
	 * 任务定义ID
	 */
	@Schema(description = "任务定义ID")
	private Long nodeJobId;

	// ---------------节点任务信息---------------
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
	 * 用时
	 */
	@Schema(description = "用时")
	private String useTime;

	/**
	 * 节点状态 -1：未开始 0：运行中 1：结束 2：驳回中 3：跳过 9：被驳回
	 */
	@Schema(description = "节点状态 -1：未开始 0：运行中 1：结束  2：驳回中 3：跳过 9：被驳回")
	private String status;

	/**
	 * 参与者角色ID
	 */
	@Schema(description = "参与者角色ID")
	private Long roleId;
	
	/**
	 * 任务类型 -1无 0用户 1角色 2岗位 3部门
	 */
	@Schema(description = "任务类型 -1无 0用户 1角色 2岗位 3部门")
	private String jobType;
	
	/**
	 * 是否挂起（0否 1是）
	 */
	@Schema(description = "是否挂起（0否 1是）")
	private String suspension;

	/**
	 * 加签类型：1前加签、2后加签、3加并签
	 */
	@Schema(description = "加签类型：1前加签、2后加签、3加并签")
	private String signatureType;
	
	/**
	 * 归属类型 0普通任务 1抄送任务 2传阅任务
	 */
	@Schema(description = "归属类型 0普通任务 1抄送任务 2传阅任务")
	private String belongType;
	
	/**
	 * 节点子流程实例ID
	 */
	@Schema(description = "节点子流程实例ID")
	private Long subFlowInstId;

	/**
	 * 子流程状态
	 */
	@Schema(description="子流程状态")
	private String subFlowStatus;

	// ---------------时间线信息---------------
	
	/**
	 * 办理人名称
	 */
	@Schema(description = "办理人名称")
	private String userName;

	/**
	 * 参与者角色名称
	 */
	@Schema(description = "参与者角色名称")
	private String roleName;

	/**
	 * 任务名称
	 */
	@Schema(description = "任务名称")
	private String jobName;

}
