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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 自动创建表管理
 *
 * @author luolin
 * @date 2022-09-23 21:56:11
 */
@Data
@TenantTable
@TableName("order_create_table")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "自动创建表管理")
public class CreateTable extends Model<CreateTable> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 表名称
	 */
	@Schema(description = "表名称")
	private String tableName;

	/**
	 * 表注释
	 */
	@Schema(description = "表注释")
	private String comments;

	/**
	 * 数据库类型
	 */
	@Schema(description = "数据库类型")
	private String databaseType;

	/**
	 * 主键策略
	 */
	@Schema(description = "主键策略")
	private String pkPolicy;

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
	 * 字段信息
	 */
	@Schema(description = "字段信息")
	private String columnInfo;

}
