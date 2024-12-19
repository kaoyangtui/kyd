package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;
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
 * 任务类型
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum JobUserTypeEnum {

	/**
	 * 无
	 */
	NONE("-1", "无"),

    /**
     * 人员
     */
    USER("0", "人员"),

    /**
     * 角色
     */
    ROLE("1", "角色"),

	/**
     * 岗位
     */
    POST("2", "岗位"),

    /**
     * 部门
     */
	DEPT("3", "部门");

	/**
	 * 标识用户 TODO 可选
	 */
	public static final String USER_PREFIX = "USER_";

	/**
	 * 标识角色
	 */
	public static final String ROLE_PREFIX = "ROLE_";

	/**
	 * 标识岗位
	 */
	public static final String POST_PREFIX = "POST_";

	/**
	 * 标识部门
	 */
	public static final String DEPT_PREFIX = "DEPT_";

	/**
	 * 类型
	 */
	private final String type;
	/**
	 * 描述
	 */
	private final String description;

	@SneakyThrows
	public static List<String> getUserRolePostDept(){
		return CollUtil.newArrayList(USER_PREFIX, ROLE_PREFIX, POST_PREFIX, DEPT_PREFIX);
	}

}
