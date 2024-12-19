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

package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程参数规则枚举
 * @author luolin
 * @date 2018/9/30
 */
@Getter
@AllArgsConstructor
public enum FlowParamRuleEnum {

    /**
     * 人员规则
     */
    PERSON("0", "人员规则"),
	/**
	 * 条件规则
	 */
	LINK("1", "条件规则"),
	/**
	 * 父子流程参数
	 */
	SUB_FLOW("2", "父子流程参数"),
	/**
	 * 监听事件
	 */
	LISTENER("3", "监听事件"),
	/**
	 * 查询/更新工单信息接口http请求头
	 */
	ORDER_PARAMS("4", "查询/更新工单信息接口http请求头"),
	/**
	 * 启动子流程时保存子工单接口
	 */
	START_SUB_FLOW("5", "启动子流程时保存子工单接口"),
	/**
	 * 重启子流程时更新子工单接口
	 */
	RESTART_SUB_FLOW("6", "重启子流程时更新子工单接口"),
	/**
	 * 返回父流程时更新父工单接口
	 */
	BACK_PAR_FLOW("7", "返回父流程时更新父工单接口");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
