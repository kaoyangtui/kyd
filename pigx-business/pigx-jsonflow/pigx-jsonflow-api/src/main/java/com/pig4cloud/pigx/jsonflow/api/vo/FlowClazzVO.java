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

import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点监听事件设置
 *
 * @author luolin
 * @date 2021-03-19 11:43:00
 */
@Data
@Schema(description = "节点监听事件设置")
public class FlowClazzVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 节点ID
	 */
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 业务KEY
	 */
	@NotBlank(message = "业务KEY不能为空")
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
	 * 流程定义ID
	 */
	@NotNull(message = "流程定义ID不能为空")
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 监听类
	 */
	@Schema(description = "监听类")
	private String clazz;
	/**
	 * http请求地址
	 */
	@Schema(description = "http请求地址")
	private String httpUrl;
	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;
	/**
	 * 排序值
	 */
	@NotNull(message = "排序值不能为空")
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 方法名称，多个逗号分隔，且有顺序
	 */
	@NotBlank(message = "方法名称不能为空")
	@Schema(description = "方法名称，多个逗号分隔，且有顺序")
	private String[] methods;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 流程实例ID
	 */
	@Schema(description = "流程实例ID")
	private Long flowInstId;

	// ----------------------------节点监听条件----------------------------

	/**
	 * 校验值
	 */
	@Schema(description = "校验值")
	private String varVal;
	/**
	 * 运算符
	 */
	@Schema(description = "运算符")
	private String operator;
	/**
	 * 变量KEY取值来源
	 */
	@Schema(description = "变量KEY取值来源")
	private String varKeyVal;

	/**
	 * 条件模式
	 */
	@Schema(description = "条件模式")
	private String valType;

	/**
	 * 事件类型 0 节点事件 1 全局事件
	 */
	@Schema(description = "事件类型 0 节点事件 1 全局事件")
	private String type;

	//----------------------额外参数----------------------

	/**
	 * Http请求参数
	 */
	@Schema(description = "Http请求参数")
	private List<FlowRule> httpParams = new ArrayList<>();

}
