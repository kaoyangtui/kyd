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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 条件/人员规则
 *
 * @author luolin
 * @date 2024-03-14 22:47:50
 */
@Data
@Schema(description = "条件/人员规则")
public class FlowRuleVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@Schema(description = "")
	private Long id;
	/**
	 * 业务KEY
	 */
	@Schema(description = "业务KEY")
	private String flowKey;
	/**
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;
	/**
	 * 节点定义ID
	 */
	@Schema(description = "节点定义ID")
	private Long flowNodeId;
	/**
	 * 节点连线ID
	 */
	@Schema(description = "节点连线ID")
	private Long flowNodeRelId;
	/**
	 * 监听事件ID
	 */
	@Schema(description = "监听事件ID")
	private Long flowClazzId;
	/**
	 * 任务类型 -1无 0用户 1角色 2岗位 3部门
	 */
	@Schema(description = "任务类型 -1无 0用户 1角色 2岗位 3部门")
	private String jobType;
	/**
	 * 默认参与者角色ID
	 */
	@Schema(description = "默认参与者角色ID")
	private Long roleId;
	/**
	 * 数据类型：0人员规则 1条件规则
	 */
	@Schema(description = "数据类型：0人员规则 1条件规则")
	private String type;
	/**
	 * 模式：0简单模式 1SpEL模式 3专业模式
	 */
	@Schema(description = "模式：0简单模式 1SpEL模式 3专业模式")
	private String valType;
	/**
	 * 同组条件ID
	 */
	@Schema(description = "同组条件ID")
	private Long groupId;
	/**
	 * 条件组关系：0 且 1 或
	 */
	@Schema(description = "条件组关系：0 且 1 或")
	private String groupsType;
	/**
	 * 组内条件关系：0 且 1 或
	 */
	@Schema(description = "组内条件关系：0 且 1 或")
	private String groupType;
	/**
	 * 条件取值来源：工单如#order.anyKey，当前登录用户如#user.userId，流程条件如#var.createUser
	 */
	@Schema(description = "变量KEY取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )，流程条件如#var.createUser")
	private String varKeyVal;
	/**
	 * 运算符：0等于 1不等于 2大于 3大于等于 4小于 5小于等于 6包含 7不包含
	 */
	@Schema(description = "运算符：0等于 1不等于 2大于 3大于等于 4小于 5小于等于 6包含 7不包含")
	private String operator;
	/**
	 * 变量值
	 */
	@Schema(description = "变量值")
	private String varVal;

	/**
	 * 参数来源：0请求头、1传参、2回参
	 */
	@Schema(description = "参数来源：0请求头、1传参、2回参")
	private String paramFrom;
	/**
	 * 参数值类型：0表单字段、1SpEL表达式、2固定值
	 */
	@Schema(description = "参数值类型：0表单字段、1SpEL表达式、2固定值")
	private String paramValType;
	/**
	 * 目标属性
	 */
	@Schema(description = "目标属性")
	private String targetProp;
	/**
	 * 参数类型：0json、1form
	 */
	@Schema(description = "参数类型：0json、1form")
	private String paramType;

	/**
	 * http请求地址
	 */
	@Schema(description = "http请求地址")
	private String httpUrl;
	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;

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
	 * 流程实例ID
	 */
	@Schema(description = "流程实例ID")
	private Long flowInstId;

	// -------------------------配置参数-------------------------
	/**
	 * 条件组内集合
	 */
	@Schema(description = "条件组内集合")
	private List<FlowRule> condGroup = new ArrayList<>();

	// -------------------------额外参数-------------------------

	/**
	 * 关联对象ID
	 */
	@Schema(description = "关联对象ID")
	private Long relObjId;

}
