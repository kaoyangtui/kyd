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
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程节点定义关系
 *
 * @author luolin
 * @date 2021-03-10 16:48:00
 */
@Data
@TenantTable
@TableName("jf_flow_node_rel")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程节点定义关系")
public class FlowNodeRel extends Model<FlowNodeRel> {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "")
	private Long id;

	/**
	 *
	 */
	@Schema(description = "")
	private Long defFlowId;

	/**
	 * 工单流程KEY
	 */
	@Schema(description = "工单流程KEY")
	private String flowKey;

	/**
	 * 来源节点ID
	 */
	@Schema(description = "来源节点ID")
	private Long fromFlowNodeId;

	/**
	 * 到达节点ID
	 */
	@Schema(description = "到达节点ID")
	private Long toFlowNodeId;

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
	 * 更新人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新人")
	private Long updateUser;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标识
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 来源节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟
	 */
	@Schema(description = "来源节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟")
	private String fromNodeType;

	/**
	 * 到达节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟
	 */
	@Schema(description = "到达节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟")
	private String toNodeType;

	/**
	 * 到达任务ID
	 */
	@Schema(description = "到达任务ID")
	private Long toNodeJobId;

	/**
	 * 变量KEY取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "变量KEY取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )")
	private String varKeyVal;

	/**
	 * 条件模式：0简单模式 1SpEL模式 2专业模式 3Http模式
	 */
	@Schema(description = "条件模式：0简单模式 1SpEL模式 2专业模式 3Http模式")
	private String valType;

	/**
	 * 连线名称
	 */
	@Schema(description = "连线名称")
	private String label;

	/**
	 * 连线类型 0 节点到节点 1 节点到任务
	 */
	@Schema(description = "连线类型0 节点到节点 1 节点到任务")
	private String type;

	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;

	/**
	 * 流程实例ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "流程实例ID")
	private Long flowInstId;

	/**
	 * 连线顶点
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "连线顶点")
	private String vertices;

	// -------------------------额外参数-------------------------

	/**
	 * 是否被访问过 0/null:否 1:是
	 */
	@TableField(exist = false)
	@Schema(description = "是否被访问过 0/null:否 1:是")
	private String isVisited;

}
