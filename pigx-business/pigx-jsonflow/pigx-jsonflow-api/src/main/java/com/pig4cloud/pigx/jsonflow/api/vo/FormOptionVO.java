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

import com.pig4cloud.pigx.jsonflow.api.entity.FormOption;
import com.pig4cloud.pigx.jsonflow.api.entity.TabsOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 表单/页面操作表
 *
 * @author luolin
 * @date 2024-03-06 22:42:55
 */
@Data
@Schema(description = "表单/页面操作表")
public class FormOptionVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@Schema(description = "")
	private Long id;
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
	 * 业务KEY
	 */
	@Schema(description = "业务KEY")
	private String flowKey;
	/**
	 * 属性唯一ID
	 */
	@Schema(description = "属性唯一ID")
	private String propId;
	/**
	 * 属性名称
	 */
	@Schema(description = "属性名称")
	private String label;
	/**
	 * 属性名
	 */
	@Schema(description = "属性名")
	private String prop;
	/**
	 * 子表单属性名
	 */
	@Schema(description = "子表单属性名")
	private String subForm;
	/**
	 * 属性类型
	 */
	@Schema(description = "属性类型")
	private String propType;
	/**
	 * 打印信息
	 */
	@Schema(description = "打印信息")
	private String printInfo;
	/**
	 * 数据类型：0字段定义 1权限配置 2打印设计
	 */
	@Schema(description = "数据类型：0字段定义 1权限配置 2打印设计")
	private String type;

	/**
	 * 权限类型：-1隐藏 0只读 1可编辑
	 */
	@Schema(description = "权限类型：-1隐藏 0只读 1可编辑")
	private String permType;

	/**
	 * 页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面
	 */
	@Schema(description = "页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面")
	private String formType;

	/**
	 * 表单ID
	 */
	@Schema(description="表单ID")
	private Long formId;

	/**
	 * 表单名称
	 */
	@Schema(description="表单名称")
	private String formName;

	/**
	 * 页面路径（动态组件）
	 */
	@Schema(description = "页面路径（动态组件）")
	private String path;

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

	/**
	 * 页面tabsId
	 */
	@Schema(description = "页面tabsId")
	private Long formTabsId;

	//-------------------查询信息-------------------

	/**
	 * 字段信息
	 */
	@Schema(description="字段信息")
	private List<FormOption> columns;

	/**
	 * 是否只查询当前配置
	 */
	@Schema(description="是否只查询当前配置")
	private String isOnlyCurr;

	/**
	 * PC待办页面信息
	 */
	@Schema(description="PC待办页面信息")
	private TabsOption tabsOption;

}
