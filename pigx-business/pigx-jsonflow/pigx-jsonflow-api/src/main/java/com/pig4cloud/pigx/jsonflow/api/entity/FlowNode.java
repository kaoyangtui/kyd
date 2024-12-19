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
import java.util.List;
import java.util.Map;

/**
 * 流程节点设置
 *
 * @author luolin
 * @date 2021-02-23 14:12:03
 */
@Data
@TenantTable
@TableName("jf_flow_node")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程节点设置")
public class FlowNode extends Model<FlowNode> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 节点子流程定义ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "节点子流程定义ID")
	private Long subDefFlowId;

	/**
	 * 业务KEY
	 */
	@Schema(description = "业务KEY")
	private String flowKey;

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
	 * MP待办页面路径（多个逗号隔开）
	 */
	@Schema(description = "MP待办页面路径（多个逗号隔开）")
	private String[] mpTodoUrl;

	/**
	 * MP完成页面路径（多个逗号隔开）
	 */
	@Schema(description = "MP完成页面路径（多个逗号隔开）")
	private String[] mpFinishUrl;

	/**
	 * 删除标识
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 任务时限
	 */
	@Schema(description = "任务时限")
	private Integer timeout;

	/**
	 * 节点名称
	 */
	@Schema(description = "节点名称")
	private String nodeName;

	/**
	 * 节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟
	 */
	@Schema(description = "节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟")
	private String nodeType;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

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
	 * 审批按钮权限
	 */
	@Schema(description = "审批按钮权限")
	private String[] jobBtns;

	/**
	 * 抄送用户ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "抄送用户ID")
	private String[] carbonCopy;

	/**
	 * 相同审批人自动通过 0：否 1：是
	 */
	@Schema(description = "相同审批人自动通过 0：否 1：是")
	private String isPassSame;

	/**
	 * 是否为网关 0：否 1：是
	 */
	@Schema(description = "是否为网关 0：否 1：是")
	private String isGateway;

	/**
	 * 节点描述
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "节点描述")
	private String description;

	/**
	 * 节点位置大小
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "节点位置大小")
	private String positionSize;

}
