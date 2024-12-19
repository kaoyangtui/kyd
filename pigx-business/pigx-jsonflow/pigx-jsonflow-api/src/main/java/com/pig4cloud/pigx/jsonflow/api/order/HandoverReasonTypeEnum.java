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
 * 交接原因（-1日常交接 0：晋升，1：转岗，2：离职 3：平调）
 *
 * @author luolin
 * @date 2020/2/17
 */
@Getter
@AllArgsConstructor
public enum HandoverReasonTypeEnum {

	/**
	 * 日常交接
	 */
	DAILY_HANDOVER("-1", "日常交接"),
	/**
	 * 晋升
	 */
	PROMOTION("0", "晋升"),
	/**
	 * 转岗
	 */
	CHANGE_POST("1", "转岗"),
	/**
	 * 离职
	 */
	LEAVE_OFFICE("2", "离职"),
	/**
	 * 平调
	 */
	FLAT_TONE("3", "平调");

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 描述
	 */
	private String description;

}
