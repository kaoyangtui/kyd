package com.pig4cloud.pigx.jsonflow.api.entity;

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
 * 驳回记录
 *
 * @author luolin
 * @date 2021-03-05 11:56:29
 */
@Data
@TenantTable
@TableName("jf_run_reject")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "驳回记录")
public class RunReject extends Model<RunReject> {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "")
	private Long id;

	/**
	 * 流程ID
	 */
	@Schema(description = "流程ID")
	private Long flowInstId;

	/**
	 * 来源节点ID
	 */
	@Schema(description = "来源节点ID")
	private Long fromRunNodeId;

	/**
	 * 来源任务ID
	 */
	@Schema(description = "来源任务ID")
	private Long fromRunJobId;

	/**
	 * 到达节点ID
	 */
	@Schema(description = "到达节点ID")
	private Long toRunNodeId;

	/**
	 * 工单流程KEY
	 */
	@Schema(description = "工单流程KEY")
	private String flowKey;

	/**
	 * 驳回人ID
	 */
	@Schema(description = "驳回人ID")
	private Long userId;

	/**
	 * 被驳回人ID
	 */
	@Schema(description = "被驳回人ID")
	private Long handleUserId;

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
	 * 节点状态 0：驳回中 1：结束
	 */
	@Schema(description = "节点状态 0：驳回中 1：结束")
	private String status;

	/**
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 来源流程节点ID
	 */
	@Schema(description = "来源流程节点ID")
	private Long fromFlowNodeId;

	/**
	 * 到达流程节点ID
	 */
	@Schema(description = "到达流程节点ID")
	private Long toFlowNodeId;

	/**
	 * 达到任务ID
	 */
	@Schema(description = "达到任务ID")
	private Long toRunJobId;

	/**
	 * 驳回意见
	 */
	@Schema(description = "驳回意见")
	private String remark;

}
