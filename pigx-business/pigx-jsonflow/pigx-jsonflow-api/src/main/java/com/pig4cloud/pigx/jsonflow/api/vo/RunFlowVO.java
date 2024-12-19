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
 * 流程管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程管理")
public class RunFlowVO extends DefFlowVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 父流程实例ID
	 */
	@Schema(description = "父流程实例ID")
	private Long parFlowInstId;

	/**
	 * 流程定义ID
	 */
	@NotNull(message = "流程定义ID不能为空")
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 流程发起人ID
	 */
	@NotNull(message = "流程发起人ID不能为空")
	@Schema(description = "流程发起人ID")
	private Long initiatorId;

	/**
	 * 工单ID
	 */
	@NotNull(message = "工单ID不能为空")
	@Schema(description = "工单ID")
	private Long orderId;

	/**
	 * 业务KEY
	 */
	@NotBlank(message = "业务KEY不能为空")
	@Schema(description = "业务KEY")
	private String flowKey;

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
	 * 流程状态 -2：撤回 -1：发起 0：运行中（正常） 1：完结 2：作废
	 */
	@Schema(description = "流程状态 -2：撤回 -1：发起 0：运行中（正常） 1：完结 2：作废")
	private String status;

	/**
	 * 作废原因
	 */
	@Schema(description = "作废原因")
	private String invalidReason;

	/**
	 * 工单CODE，便于业务人员查看
	 */
	@NotBlank(message = "工单CODE不能为空")
	@Schema(description = "工单CODE，便于业务人员查看")
	private String code;

	/**
	 * 分组名称
	 */
	@Schema(description = "分组名称")
	private String groupName;

	/**
	 * 版本
	 */
	@Schema(description = "版本")
	private Integer version;

	/**
	 * 表单ID
	 */
	@Schema(description = "表单ID")
	private Long formId;

	/**
	 * 查询工单信息接口
	 */
	@Schema(description = "查询工单信息接口")
	private String queryOrder;

	/**
	 * 更新工单信息接口
	 */
	@Schema(description = "更新工单信息接口")
	private String updateOrder;

	/**
	 * 查询工单信息接口http请求类型
	 */
	@Schema(description = "查询工单信息接口http请求类型")
	private String queryMethod;

	/**
	 * 更新工单信息接口http请求类型
	 */
	@Schema(description = "更新工单信息接口http请求类型")
	private String updateMethod;

	/**
	 * 流程设计来源 0 一键快捷设计 1 定制化开发设计
	 */
	@Schema(description = "流程设计来源 0 一键快捷设计 1 定制化开发设计")
	private String fromType;

	/**
	 * 自动布局方向
	 */
	@Schema(description = "自动布局方向")
	private String autoLayout;

	/**
	 * 流程名称
	 */
	@Schema(description = "流程名称")
	private String flowName;


	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 是否允许任务连线到其他节点 0否 1是
	 */
	@Schema(description = "是否允许任务连线到其他节点 0否 1是")
	private String allowJobLink;

	/**
	 * 是否允许节点与任务分离显示 0否 1是
	 */
	@Schema(description = "是否允许节点与任务分离显示 0否 1是")
	private String isJobSeparated;

	/**
	 * 设计模式：1 简单模式 0 专业模式
	 */
	@Schema(description = "设计模式：1 简单模式 0 专业模式")
	private String isSimpleMode;

	/**
	 * 连线样式
	 */
	@Schema(description = "连线样式")
	private String connector = "rounded";

	/**
	 * 连线路由
	 */
	@Schema(description = "连线路由")
	private String router = "normal";

	/**
	 * 配置数据独立 0否 1是
	 */
	@Schema(description = "配置数据独立 0否 1是")
	private String isIndependent;

	//----------------------额外参数----------------------

	/**
	 * 流程用时
	 */
	@Schema(description = "流程用时")
	private String useTime;

	//----------------------流程实例图参数----------------------
	/**
	 * 流程实例ID
	 */
	@Schema(description = "流程实例ID")
	private Long flowInstId;
	
}
