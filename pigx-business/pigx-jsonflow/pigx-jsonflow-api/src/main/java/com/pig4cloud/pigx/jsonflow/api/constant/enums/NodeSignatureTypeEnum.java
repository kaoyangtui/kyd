package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节点加签类型
 * @author luiolin
 * @date 2023/05/28
 */
@Getter
@AllArgsConstructor
public enum NodeSignatureTypeEnum {

	/**
	 * 前加节点
	 */
	BEFORE_NODE("1", "前加节点"),

	/**
	 * 后加节点
	 */
	AFTER_NODE("2", "后加节点"),

	/**
	 * 加并节点
	 */
	SIGN_NODE("3", "加并节点"),

	/**
	 * 删除节点
	 */
	DEL_NODE("4", "删除节点");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String desc;

}
