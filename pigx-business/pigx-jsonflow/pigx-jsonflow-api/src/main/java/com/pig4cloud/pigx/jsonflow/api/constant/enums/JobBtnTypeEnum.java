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
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import jakarta.validation.ValidationException;
import java.util.List;

/**
 * 审批按钮
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum JobBtnTypeEnum {
    /**
     * 发起
     */
    INITIATE("-1", "发起"),

    /**
     * 同意
     */
    APPROVE("0", "同意"),

	/**
	 * 退回首节点
	 */
	BACK_FIRST("1", "退回首节点"),

    /**
     * 任意驳回
     */
    REJECT("2", "任意驳回"),

    /**
     * 下一办理人
     */
    NEXT_HANDLE("3", "下一办理人"),

    /**
     * 作废
     */
    INVALID("4", "作废"),

	/**
	 * 前加签
	 */
	BEFORE_SIGNATURE("5", "前加签"),

	/**
	 * 后加签
	 */
	AFTER_SIGNATURE("6", "后加签"),

	/**
	 * 加并签
	 */
	SIGN_TOGETHER("7", "加并签"),

	/**
	 * 任意跳转
	 */
	ANY_JUMP("8", "任意跳转"),

    /**
     * 自动审批
     */
    AUTO_AUDIT("9", "自动审批"),

	/**
	 * 退回上一步
	 */
	BACK_PRE("10", "退回上一步"),

	/**
	 * 抄送
	 */
	CARBON_COPY("11", "抄送"),

	/**
	 * 传阅
	 */
	PASS_READ("12", "传阅"),

	/**
	 * 前加节点
	 */
	BEFORE_NODE("13", "前加节点"),

	/**
	 * 后加节点
	 */
	AFTER_NODE("14", "后加节点"),

	/**
	 * 加并节点
	 */
	SIGN_NODE("15", "加并节点"),

	/**
	 * 删除节点
	 */
	DEL_NODE("16", "删除节点");

	/**
	 * 类型
	 */
	private final String type;
	/**
	 * 描述
	 */
	private final String desc;

	public static List<String> listAllBtns() {
		return CollUtil.newArrayList(JobBtnTypeEnum.APPROVE.getType(),
				JobBtnTypeEnum.ANY_JUMP.getType(), JobBtnTypeEnum.REJECT.getType(),
				JobBtnTypeEnum.BEFORE_NODE.getType(), JobBtnTypeEnum.AFTER_NODE.getType(), JobBtnTypeEnum.SIGN_NODE.getType(),
				JobBtnTypeEnum.BEFORE_SIGNATURE.getType(), JobBtnTypeEnum.AFTER_SIGNATURE.getType(), JobBtnTypeEnum.SIGN_TOGETHER.getType(),
				JobBtnTypeEnum.BACK_FIRST.getType(), JobBtnTypeEnum.BACK_PRE.getType(),
				JobBtnTypeEnum.CARBON_COPY.getType(), JobBtnTypeEnum.PASS_READ.getType(),
				JobBtnTypeEnum.NEXT_HANDLE.getType());
	}

	/**
	 * 返回枚举常量对象
	 * @param type 类型
	 */
	@SneakyThrows
	public static JobBtnTypeEnum getEnumByType(String type){
		if (StrUtil.isEmpty(type)) throw new ValidationException("该审批按钮类型不存在");
		for (JobBtnTypeEnum each: values()) {
			if(each.getType().equals(type)) return  each;
			if(each.getDesc().equals(type)) return  each;
		}
		throw new ValidationException("该审批按钮类型不存在");
	}

}
