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
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 表单/页面操作表
 *
 * @author luolin
 * @date 2024-03-06 22:42:55
 */
@Data
@TenantTable
@TableName("jf_form_option")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "表单/页面操作表")
public class FormOption extends Model<FormOption> {
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
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "流程定义ID")
	private Long defFlowId;
	/**
	 * 节点定义ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "节点定义ID")
	private Long flowNodeId;
	/**
	 * 业务KEY
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "业务KEY")
	private String flowKey;
	/**
	 * 属性唯一ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "属性唯一ID")
	private String propId;
	/**
	 * 属性名称
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "属性名称")
	private String label;
	/**
	 * 属性名
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "属性名")
	private String prop;
	/**
	 * 子表单属性名
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "子表单属性名")
	private String subForm;
	/**
	 * 属性类型
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "属性类型")
	private String propType;
	/**
	 * 打印信息
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
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
	@TableField(updateStrategy = FieldStrategy.IGNORED)
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
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description="表单ID")
	private Long formId;

	/**
	 * 表单名称
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description="表单名称")
	private String formName;

	/**
	 * 页面路径（动态组件）
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "页面路径（动态组件）")
	private String path;

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
	 * 流程实例ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "流程实例ID")
	private Long flowInstId;

	/**
	 * 页面tabsId
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "页面tabsId")
	private Long formTabsId;
	
}
