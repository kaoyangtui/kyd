package com.pig4cloud.pigx.jsonflow.api.util;
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

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库工具类
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@SuppressWarnings("unused")
@Slf4j
public class DataBaseUtil {

	/**
	 * Java对象属性转换为数据库字段
	 *
	 * @param property Java属性名
	 * @return 数据库字段
	 */
	public static String propertyToColumn(String property) {
		if (null == property) {
			return StrUtil.EMPTY;
		}
		char[] chars = property.toCharArray();
		StringBuilder field = new StringBuilder();
		for (char c : chars) {
			if (c >= 'A' && c <= 'Z') {
				field.append(StrUtil.UNDERLINE).append(String.valueOf(c).toLowerCase());
			} else {
				field.append(c);
			}
		}
		return field.toString();
	}

	/**
	 * 将数据库字段转换为Java属性
	 *
	 * @param column 字段名
	 * @return Java属性名
	 */
	public static String columnToProperty(String column) {
		if (null == column) {
			return StrUtil.EMPTY;
		}
		char[] chars = column.toCharArray();
		StringBuilder property = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == CharPool.UNDERLINE) {
				int j = i + 1;
				if (j < chars.length) {
					property.append(String.valueOf(chars[j]).toUpperCase());
					i++;
				}
			} else {
				property.append(c);
			}
		}
		return property.toString();
	}

}
