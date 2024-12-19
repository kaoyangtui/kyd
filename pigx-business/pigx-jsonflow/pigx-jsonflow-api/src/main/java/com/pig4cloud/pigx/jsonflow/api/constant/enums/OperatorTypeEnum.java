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

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

/**
 * 条件运算符类型
 * 运算符：0等于 1不等于 2大于 3大于等于 4小于 5小于等于 6包含 7不包含
 * @author luolin
 * @date 2018/9/30
 */
@Getter
@AllArgsConstructor
public enum OperatorTypeEnum {

    /**
     * 等于
     */
    EQUAL("0", "等于"),
	/**
	 * 不等于
	 */
	NOT_EQUAL("1", "不等于"),

    /**
     * 大于
     */
    MORE_THAN("2", "大于"),
	/**
	 * 大于等于
	 */
	MORE_EQUAL_THAN("3", "大于等于"),

	/**
	 * 小于
	 */
	LESS_THAN("4", "小于"),
	/**
	 * 小于等于
	 */
	LESS_EQUAL_THAN("5", "小于等于"),

	/**
	 * 包含
	 */
	CONTAINS("6", "包含"),
	/**
	 * 不包含
	 */
	NOT_CONTAINS("7", "不包含");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

	@SneakyThrows
	public static List<String> getEqualNotEqualContainsNotContains(){
		return CollUtil.newArrayList(OperatorTypeEnum.EQUAL.getType(), OperatorTypeEnum.NOT_EQUAL.getType(), OperatorTypeEnum.CONTAINS.getType(), OperatorTypeEnum.NOT_CONTAINS.getType());
	}

}
