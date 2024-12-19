package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 多人/多角色加签类型
 * @author luiolin
 * @date 2023/05/28
 */
@Getter
@AllArgsConstructor
public enum JobSignatureTypeEnum {

	/**
	 * 前加签
	 */
	BEFORE_SIGNATURE("1", "前加签"),

	/**
	 * 后加签
	 */
	AFTER_SIGNATURE("2", "后加签"),

	/**
	 * 加并签
	 */
	SIGN_TOGETHER("3", "加并签");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String desc;

}
