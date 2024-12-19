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
 * 消息类型 0 - 个人消息 1 - 群消息
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum MsgTypeEnum {

	/**
	 * 个人消息
	 */
	PERSONAL("0", "个人消息"),

	/**
	 * 群消息
	 */
	GROUP("1", "群消息");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
