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
 * 节点监听事件设置
 *
 * @author luolin
 * @date 2021-03-19 11:43:00
 */
@Data
@TenantTable
@TableName("jf_flow_clazz")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "节点监听事件设置")
public class FlowClazz extends Model<FlowClazz> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 节点ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 业务KEY
	 */
	@Schema(description = "业务KEY")
	private String flowKey;

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
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 监听类
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "监听类")
	private String clazz;
	/**
	 * http请求地址
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "http请求地址")
	private String httpUrl;
	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;
	
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 方法名称，多个逗号分隔，且有顺序
	 */
	@Schema(description = "方法名称，多个逗号分隔，且有顺序")
	private String[] methods;

	/**
	 * 删除标识
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 流程实例ID
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "流程实例ID")
	private Long flowInstId;

	// ----------------------------节点监听条件----------------------------

	/**
	 * 校验值
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "校验值")
	private String varVal;
	/**
	 * 运算符
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "运算符")
	private String operator;
	/**
	 * 变量KEY取值来源
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "变量KEY取值来源")
	private String varKeyVal;

	/**
	 * 条件模式
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@Schema(description = "条件模式")
	private String valType;

	/**
	 * 事件类型 0 节点事件 1 全局事件
	 */
	@Schema(description = "事件类型 0 节点事件 1 全局事件")
	private String type;

}
