package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 多人/多角色审批方式
 * @author luiolin
 * @date 2023/05/28
 */
@Getter
@AllArgsConstructor
public enum JobApproveMethodEnum {

	/**
	 * 会签
	 */
	ALL_AUDIT("1", "会签"),

	/**
	 * 或签
	 */
	ANY_ONE("2", "或签"),

	/**
	 * 依次审批
	 */
	SEQUENTIAL("3", "依次审批");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
