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
 * 节点任务设置
 *
 * @author luolin
 * @date 2021-03-09 17:30:17
 */
@Data
@TenantTable
@TableName("jf_node_job")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "节点任务设置")
public class NodeJob extends Model<NodeJob> {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "")
	private Long id;

	/**
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 节点ID
	 */
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 工单流程KEY
	 */
	@Schema(description = "工单流程KEY")
	private String flowKey;

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
	 * 任务名称
	 */
	@Schema(description = "任务名称")
	private String jobName;

	/**
	 * 任务时限
	 */
	@Schema(description = "任务时限")
	private Integer timeout;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 动态参与者KEY
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "动态参与者KEY")
	private String userKey;

	/**
	 * 动态参与者取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "动态参与者取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )")
	private String userKeyVal;
	/**
	 * 人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式
	 */
	@Schema(description = "人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式")
	private String valType;

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
	 * 被驳回后是否可跳过 0 否 1 是
	 */
	@Schema(description = "被驳回后是否可跳过 0 否  1 是")
	private String isSkipRejected;

	/**
	 * 删除标识
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 待分配办理人节点ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "待分配办理人节点ID")
	private Long distFlowNodeId;

	/**
	 * 被分配后是否立即运行 0否 1是
	 */
	@Schema(description = "被分配后是否立即运行 0否 1是")
	private String isNowRun;

	/**
	 * 任务来源类型 0默认任务 1不分离任务 2分离任务
	 */
	@Schema(description = "任务来源类型 0默认任务 1不分离任务 2分离任务")
	private String fromType;

	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;

	/**
	 * 节点位置大小
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "节点位置大小")
	private String positionSize;

}
