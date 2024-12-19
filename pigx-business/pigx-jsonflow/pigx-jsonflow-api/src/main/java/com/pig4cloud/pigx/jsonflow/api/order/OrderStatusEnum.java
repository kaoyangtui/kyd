package com.pig4cloud.pigx.jsonflow.api.order;

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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工单状态
 *
 * @author luolin
 * @date 2020/2/17
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

	/**
	 * 撤回
	 */
	RECALL("-3", "撤回"),
	/**
	 * 暂存
	 */
	TEMP("-2", "暂存"),
	/**
	 * 运行中
	 */
	RUN("-1", "运行中"),

	/**
	 * 完成
	 */
	NORMAL("0", "完成"),
	/**
	 * 流程作废
	 */
	INVALID("1", "作废"),
	/**
	 * 流程终止
	 */
	TERMINATE("2", "终止");

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 描述
	 */
	private String description;

}
