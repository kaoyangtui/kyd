package com.pig4cloud.pigx.order.api.entity;

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
 * 请假工单
 *
 * @author luolin
 * @date 2020-08-25 10:55:19
 */
@Data
@TenantTable
@TableName("order_ask_leave")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "请假工单")
public class AskLeave extends Model<AskLeave> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键pk
	 */
	@TableId(type = IdType.INPUT)
	@Schema(description = "主键pk")
	private Long id;

	/**
	 * 编号
	 */
	@Schema(description = "编号")
	private String code;

	/**
	 * 工单流程KEY
	 */
	@Schema(description = "工单流程KEY")
	private String flowKey;

	/**
	 * 流程ID
	 */
	@Schema(description = "流程ID")
	private Long flowInstId;

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
	 * 状态 -3撤回 -2暂存 -1运行中 0完成 1作废 2终止
	 */
	@Schema(description = "状态 -3撤回 -2暂存 -1运行中 0完成 1作废 2终止")
	private String status;

	/**
	 * 删除标
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标")
	private String delFlag;

	/**
	 * 请假事由
	 */
	@Schema(description = "请假事由")
	private String remark;

	/**
	 * 请假类型
	 */
	@Schema(description = "请假类型")
	private String type;

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
	 * 请假天数
	 */
	@Schema(description = "请假天数")
	private String days;

	/**
	 * 抄送人
	 */
	@Schema(description = "抄送人")
	private String[] carbonCopyPerson;

	/**
	 * 上传文档（可多个），url路径
	 */
	@Schema(description = "上传文档（可多个），url路径")
	private String imgUrls;

	/**
	 * 完成时间
	 */
	@Schema(description = "完成时间")
	private LocalDateTime finishTime;

}
