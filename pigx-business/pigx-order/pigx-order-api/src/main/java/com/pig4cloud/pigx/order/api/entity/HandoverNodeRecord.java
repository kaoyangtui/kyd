package com.pig4cloud.pigx.order.api.entity;

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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 交接任务记录
 *
 * @author luolin
 * @date 2020-04-26 10:45:55
 */
@Data
@TenantTable
@TableName("order_handover_node_record")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "交接任务记录")
public class HandoverNodeRecord extends Model<HandoverNodeRecord> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "ID")
	private Long id;

	/**
	 * 运行任务ID
	 */
	@Schema(description = "运行任务ID")
	private Long runJobId;

	/**
	 * 交接流程表ID
	 */
	@Schema(description = "交接流程表ID")
	private Long orderId;

	/**
	 * 待办事项
	 */
	@Schema(description = "待办事项")
	private String todoList;

	/**
	 * 交接状态 -3撤回 -2: 未交接 -1 交接中 0：已交接 1：作废 2终止
	 */
	@Schema(description = "交接状态 -3撤回 -2: 未交接 -1 交接中 0：已交接 1：作废 2终止")
	private String status;

	/**
	 * 流程驳回，任务是否被驳回状态（0：否 1：是）
	 */
	@Schema(description = "流程驳回，任务是否被驳回状态（0：否 1：是）")
	private String retStatus;

	/**
	 * 流程ID
	 */
	@Schema(description = "流程ID")
	private Long flowInstId;

	/**
	 * 流程名称
	 */
	@Schema(description = "流程名称")
	private String flowKey;

	/**
	 * 流程发起人ID
	 */
	@Schema(description = "流程发起人ID")
	private Long initiatorId;

	/**
	 * 任务开始时间
	 */
	@Schema(description = "任务开始时间")
	private LocalDateTime startTime;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private Long createUser;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改人")
	private Long updateUser;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 任务定义ID
	 */
	@Schema(description = "任务定义ID")
	private Long nodeJobId;

	/**
	 * 节点定义ID
	 */
	@Schema(description = "节点定义ID")
	private Long runNodeId;

}
