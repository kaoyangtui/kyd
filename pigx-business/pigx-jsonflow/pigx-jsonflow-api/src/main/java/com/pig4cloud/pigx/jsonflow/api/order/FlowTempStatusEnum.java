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
 * 流程工单暂存状态 状态 -1 暂存 0 作废 1 发布
 *
 * @author luolin
 * @date 2020/2/17
 */
@Getter
@AllArgsConstructor
public enum FlowTempStatusEnum {

	/**
	 * 暂存
	 */
	TEMP("-1", "暂存"),
	/**
	 * 作废
	 */
	INVALID("0", "作废"),

	/**
	 * 发布
	 */
	PUBLISH("1", "发布");

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 描述
	 */
	private String desc;

}
