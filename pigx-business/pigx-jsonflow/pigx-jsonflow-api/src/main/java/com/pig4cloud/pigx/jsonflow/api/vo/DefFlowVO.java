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

import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowClazz;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义管理
 *
 * @author luolin
 * @date 2021-02-23 13:52:11
 */
@Data
@Schema(description = "流程定义管理")
public class DefFlowVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 流程名称
	 */
	@Schema(description = "流程名称")
	private String flowName;

	/**
	 * 业务KEY
	 */
	@Schema(description = "业务KEY")
	private String flowKey;

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
	 * 状态 -1 暂存 0 作废 1 发布
	 */
	@Schema(description = "状态 -1 暂存 0 作废 1 发布")
	private String status;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 修改人
	 */
	@Schema(description = "修改人")
	private Long updateUser;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

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
	@Schema(description="表单ID")
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

	// ----------------------配置参数----------------------
	/**
	 * 流程定义属性
	 */
	private RunFlowVO attrs;
	
	/**
	 * 查询/更新工单信息接口http请求头
	 */
	@Schema(description = "查询/更新工单信息接口http请求头")
	private List<FlowRule> orderParams = new ArrayList<>();

	/**
	 * 流程节点关系属性
	 */
	private List<FlowNodeRelVO> linkList = new ArrayList<>();

	/**
	 * 流程节点或任务属性
	 */
	private List<RunNodeVO> nodeList = new ArrayList<>();

	/**
	 * 全局监听事件
	 */
	private List<FlowClazzVO> clazzes = new ArrayList<>();

	//----------------------流程实例图参数----------------------
	/**
	 * 流程实例ID
	 */
	@Schema(description = "流程实例ID")
	private Long flowInstId;

}
