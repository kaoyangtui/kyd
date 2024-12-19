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

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * KEY取值来源Class类型，支持自定义任意扩展
 *
 * @author luolin
 * @date 2020/2/17
 */
@Getter
@AllArgsConstructor
public enum KeyValParamClassEnum {

    /**
     * String 类型
     */
	STRING("String", String.class),

	/**
	 * Long 类型
	 */
	LONG("Long", Long.class),

	/**
	 * Integer 类型
	 */
	INTEGER("Integer", Integer.class),

    /**
     * Map 类型
     */
	MAP("Map", HashMap.class),

    /**
     * List 类型
     */
	LIST("List", ArrayList.class),

    /**
     * SysUser 类型
     */
	SYS_USER("SysUser", SysUser.class),

	/**
	 * SysRole 类型
	 */
	SYS_ROLE("SysRole", SysRole.class),

    /**
     * SysDept 类型
     */
	SYS_DEPT("SysDept", SysDept.class),

    /**
     * NULL 类型
     */
	NULL("NULL", null);

	/**
	 * KEY取值来源
	 */
	private String from;
	/**
	 * Class类型
	 */
	private Class<?> clazz;

	/**
	 * 返回枚举常量对象
	 * @param from 取值来源
	 */
	@SneakyThrows
	public static KeyValParamClassEnum getEnumByFrom(String from){
		if (StrUtil.isEmpty(from)) throw new ValidationException("KEY取值来源参数类型不存在, 请先自定义");
		for (KeyValParamClassEnum each: values()) {
			if(each.getFrom().equals(from)) return  each;
		}
		throw new ValidationException("KEY取值来源参数类型不存在, 请先自定义");
	}

}
