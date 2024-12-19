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
 * 消息通知管理
 *
 * @author luolin
 * @date 2021-02-26 10:31:43
 */
@Data
@TenantTable
@TableName("jf_ws_notice")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息通知管理")
public class WsNotice extends Model<WsNotice> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "编号")
	private Long id;

	/**
	 * 消息类型 0-个人消息 1-群消息
	 */
	@Schema(description = "消息类型 0-个人消息 1-群消息")
	private String type;

	/**
	 * 消息数据
	 */
	@Schema(description = "消息数据")
	private String data;

	/**
	 * 任务类型（0-个人任务 1-组任务）
	 */
	@Schema(description = "任务类型（0-个人任务 1-组任务）")
	private String jobType;

	/**
	 * 通知状态 -1发起 0流程中 1结束
	 */
	@Schema(description = "通知状态 -1发起 0流程中 1结束")
	private String status;

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
	 * 办理人(用户ID）
	 */
	@Schema(description = "办理人(用户ID）")
	private Long userId;

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

}
