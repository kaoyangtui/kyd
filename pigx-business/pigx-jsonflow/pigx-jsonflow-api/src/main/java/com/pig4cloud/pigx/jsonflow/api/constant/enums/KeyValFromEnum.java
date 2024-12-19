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

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

/**
 * KEY取值来源，支持自定义任意扩展
 *
 * @author luolin
 * @date 2020/2/17
 */
@Getter
@AllArgsConstructor
public enum KeyValFromEnum {

    /**
     * 系统信息 TODO 自行扩展
     */
	SYS("#sys.", "系统信息"),

	/**
     * 流程实例
     */
	FLOW("#flow.", "流程实例"),

	/**
     * 流程条件
     */
    VAR("#var.", "流程条件"),

    /**
     * 工单对象
     */
	ORDER("#order.", "工单对象"),

	/**
	 * 表单数据
	 */
	FORM("#form.", "表单数据"),

    /**
	 * 当前登录用户
	 */
	USER("#user.", "当前登录用户"),

	// ------------- 以下为固定字符串，非KEY取值来源 -------------
    /**
	 * 人员规则
	 */
	PERSON("#person.", "人员规则"),

    /**
	 * 条件规则
	 */
	LINK("#link.", "条件规则"),

	/**
     * 左半函数表达式
     */
    LEFT_METHOD("(", "左半函数表达式"),

	/**
     * 左半函数表达式
     */
    RIGHT_METHOD(")", "左半函数表达式"),

	/**
     * 井号分隔符，含义：参数类型#参数值
     */
	NUMBER_SIGN("#", "井号分隔符"),

	/**
     * 逗号转义分隔符
     */
	DOT("\\.", "逗号转义分隔符");

	/**
	 * KEY取值来源
	 */
	private String from;
	/**
	 * 描述
	 */
	private String des;

	@SneakyThrows
	public static List<String> getVarOrderUser(){
		return CollUtil.newArrayList(KeyValFromEnum.SYS.getFrom(), KeyValFromEnum.FLOW.getFrom(), KeyValFromEnum.VAR.getFrom(), KeyValFromEnum.ORDER.getFrom(), KeyValFromEnum.FORM.getFrom(), KeyValFromEnum.USER.getFrom());
	}

}
