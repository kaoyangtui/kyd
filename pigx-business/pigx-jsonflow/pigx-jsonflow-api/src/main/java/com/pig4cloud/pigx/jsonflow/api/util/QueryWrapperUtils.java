
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
package com.pig4cloud.pigx.jsonflow.api.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luolin
 * @date 2020/2/2 16:56
 */
@UtilityClass
public class QueryWrapperUtils {

	/**
	 * 为QueryWrapper构造like查询，支持多列多参数查询
	 * @param columns
	 * @param likes
	 * @param <T>
	 */
	public <T> void queryLike(QueryWrapper<T> query, List<SFunction<T, ?>> columns, List<String> likes) {
		if (ArrayUtil.isNotEmpty(columns) && ArrayUtil.isNotEmpty(likes)) {
			LambdaQueryWrapper<T> lambda = query.lambda();
			for (int i = 0; i < likes.size(); i++) {
				lambda.like(Objects.nonNull(likes.get(i)), columns.get(i), likes.get(i));
			}
		}
	}

	/**
	 * 为QueryWrapper构造queryTime[]参数
	 * @param query
	 * @param column
	 * @param queryTime
	 * @param <T>
	 */
	public <T> LambdaQueryWrapper<T> queryTime(QueryWrapper<T> query, SFunction<T, ?> column, String[] queryTime) {
		LambdaQueryWrapper<T> lambda = query.lambda();
		if (ArrayUtil.isNotEmpty(queryTime)) {
			lambda.ge(column, DateUtil.toLocalDateTime(DateUtil.beginOfDay(DateUtil.parse(queryTime[0])))).le(column,
					DateUtil.toLocalDateTime(DateUtil.endOfDay(DateUtil.parse(queryTime[1]))));
		}
		return lambda;
	}

	/**
	 * 为QueryWrapper构造 创建人+queryTime[] 参数
	 * @param query
	 * @param column
	 * @param queryTime
	 * @param <T>
	 */
	public <T> LambdaQueryWrapper<T> userQueryTime(QueryWrapper<T> query, SFunction<T, ?> column, String[] queryTime,
			SFunction<T, ?> userColumn, Long user) {
		LambdaQueryWrapper<T> lambda = query.lambda();
		// 默认查询创建人
		lambda.eq(userColumn, user);
		if (ArrayUtil.isNotEmpty(queryTime)) {
			lambda.ge(column, DateUtil.toLocalDateTime(DateUtil.beginOfDay(DateUtil.parse(queryTime[0])))).le(column,
					DateUtil.toLocalDateTime(DateUtil.endOfDay(DateUtil.parse(queryTime[1]))));
		}
		return lambda;
	}

	/**
	 * 内存分页处理
	 * @param page
	 * @param records
	 * @return
	 */
	public <T> List<T> paging(Page page, List<T> records) {
		page.setTotal(records.size());
		return records.stream().skip((page.getCurrent() - 1) * page.getSize()).limit(page.getSize())
				.collect(Collectors.toList());
	}

}
