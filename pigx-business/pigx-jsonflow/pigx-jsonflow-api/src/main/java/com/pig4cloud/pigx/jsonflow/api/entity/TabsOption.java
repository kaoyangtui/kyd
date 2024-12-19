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
 * 流程中tabs页面
 *
 * @author luolin
 * @date 2021-02-23 14:12:33
 */
@Data
@TenantTable
@TableName("jf_tabs_option")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程中tabs页面")
public class TabsOption extends Model<TabsOption> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 图标
	 */
	@Schema(description = "图标")
	private String icon;

	/**
	 * 页面标题
	 */
	@Schema(description = "页面标题")
	private String label;

	/**
	 * 页面路径（动态组件）
	 */
	@Schema(description = "页面路径（动态组件）")
	private String path;

	/**
	 * 业务KEY或其他页面KEY
	 */
	@Schema(description = "业务KEY或其他页面KEY")
	private String flowKey;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 默认展示页面（0 否 1是）
	 */
	@Schema(description = "默认展示页面（0 否 1是）")
	private String isActive;

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
	 * 是否为查看页面 0否 1是
	 */
	@Schema(description = "是否为查看页面 0否 1是")
	private String isView;

	/**
	 * 表单信息
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "表单信息")
	private String formInfo;

	/**
	 * 是否自动审批 0否 1是
	 */
	@Schema(description = "是否自动审批 0否 1是")
	private String isAutoAudit;

	/**
	 * 页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面
	 */
	@Schema(description = "页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面")
	private String type;

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
	 * 状态 -1 暂存 0 作废 1 发布
	 */
	@Schema(description = "状态 -1 暂存 0 作废 1 发布")
	private String status;

	/**
	 * 删除标识
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 页面样式
	 */
	@Schema(description="页面样式")
	private String style;

}
