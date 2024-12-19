package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

/**
 * 节点流转类型 0 顺序流转 1 多条件流转 2 直接返回
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum NodeCircTypeEnum {

	/**
	 * 顺序流转
	 */
	SEQUENTIAL("0", "顺序流转"),

	/**
	 * 直接返回
	 */
	DIRECT_RETURN("1", "直接返回"),

	/**
	 * 多条件流转
	 */
	MULTI_CONDITION("2", "多条件流转");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
