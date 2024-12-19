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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交接流程
 *
 * @author luolin
 * @date 2020-04-26 10:35:55
 */
@Data
@TenantTable
@TableName("order_handover_flow")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "交接流程")
public class HandoverFlow extends Model<HandoverFlow> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.INPUT)
	@Schema(description = "ID")
	private Long id;

	/**
	 * 编号
	 */
	@Schema(description = "编号")
	private String code;

	/**
	 * 交接人ID
	 */
	@Schema(description = "交接人ID")
	private Long handoverUser;

	/**
	 * 交接人部门
	 */
	@Schema(description = "交接人部门")
	private Long handoverUserDept;

	/**
	 * 交接原因（-1日常交接 0：晋升，1：转岗，2：离职 3：平调）
	 */
	@Schema(description = "交接原因（-1日常交接 0：晋升，1：转岗，2：离职 3：平调）")
	private String handoverReason;

	/**
	 * 接收部门
	 */
	@Schema(description = "接收部门")
	private Long receiveDept;

	/**
	 * 接收人ID
	 */
	@Schema(description = "接收人ID")
	private Long receiveUser;

	/**
	 * 交接类型：-1 任务交接 0 公共交接
	 */
	@Schema(description = "交接类型：-1 任务交接 0 公共交接")
	private String type;

	/**
	 * 流程名称
	 */
	@Schema(description = "流程名称")
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
	 * 删除标识
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 状态 -3撤回 -2: 未交接 -1 交接中 0：已交接 1：作废 2终止
	 */
	@Schema(description = "状态 -3撤回 -2: 未交接 -1 交接中 0：已交接 1：作废 2终止")
	private String status;

	/**
	 * 完成时间
	 */
	@Schema(description = "完成时间")
	private LocalDateTime finishTime;

}
