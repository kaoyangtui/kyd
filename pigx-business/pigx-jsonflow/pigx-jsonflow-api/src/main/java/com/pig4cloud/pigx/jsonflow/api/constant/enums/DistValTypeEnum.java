package com.pig4cloud.pigx.jsonflow.api.constant.enums;
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
 * 条件类型/参与者类型
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum DistValTypeEnum {

	/**
	 * 普通模式
	 */
	ORDINARY("-2", "普通模式"),
	/**
	 * 分配模式
	 */
	DIST("-1", "分配模式"),
	/**
	 * 简单模式
	 */
	SIMPLE("0", "简单模式"),
	/**
	 * 流程条件-SpEL模式、分配参与者-固定模式
	 */
	SPEL_FIXED("1", "SpEL模式/固定模式"),
	/**
	 * 专业模式
	 */
	COMPLEX("2", "专业模式"),
	/**
	 * Http模式
	 */
	HTTP("3", "Http模式");

	/**
	 * 类型
	 */
	private final String type;
	/**
	 * 描述
	 */
	private final String description;

}
