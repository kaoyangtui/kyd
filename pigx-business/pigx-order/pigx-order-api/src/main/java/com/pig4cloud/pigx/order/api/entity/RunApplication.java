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
package com.pig4cloud.pigx.order.api.entity;

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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 我的工单
 *
 * @author luolin
 * @date 2022-09-17 00:58:49
 */
@Data
@TenantTable
@TableName("order_run_application")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "我的工单")
public class RunApplication extends Model<RunApplication> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.INPUT)
	@Schema(description = "主键id")
	private Long id;

	/**
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 工单流程KEY
	 */
	@Schema(description = "工单流程KEY")
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
	 * 状态 -3撤回 -2暂存 -1运行中 0完成 1作废 2终止
	 */
	@Schema(description = "状态 -3撤回 -2暂存 -1运行中 0完成 1作废 2终止")
	private String status;

	/**
	 * 删除标
	 */
	@TableLogic
	@Schema(description = "删除标")
	private String delFlag;

	/**
	 * 表单备注
	 */
	@Schema(description = "表单备注")
	private String remark;

	/**
	 * 关联表名称
	 */
	@Schema(description = "关联表名称")
	private String tableName;

	/**
	 * 表单信息
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "表单信息")
	private String formInfo;
	
	/**
	 * 表单名称
	 */
	@Schema(description = "表单名称")
	private String formName;

	/**
	 * 分组名称
	 */
	@Schema(description = "分组名称")
	private String groupName;

	/**
	 * 图标
	 */
	@Schema(description = "图标")
	private String icon;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 编号
	 */
	@Schema(description = "编号")
	private String code;

	/**
	 * 流程ID
	 */
	@Schema(description = "流程ID")
	private Long flowInstId;

	/**
	 * 完成时间
	 */
	@Schema(description = "完成时间")
	private LocalDateTime finishTime;

	/**
	 * 表单数据
	 */
	@Schema(description = "表单数据")
	private String formData;

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
	 * 页面样式
	 */
	@Schema(description="页面样式")
	private String style;

	/**
	 * 页面路径（动态组件）
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "页面路径（动态组件）")
	private String path;

	/**
	 * 页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面
	 */
	@Schema(description = "页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面")
	private String type;

	/**
	 * 关联子表名称
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "关联子表名称")
	private String[] subTableName;

	/**
	 * 子表关联主表列名
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "子表关联主表列名")
	private String subMainField;

	/**
	 * 主表关联子表属性
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "主表关联子表属性")
	private String mainSubProp;
	
}
