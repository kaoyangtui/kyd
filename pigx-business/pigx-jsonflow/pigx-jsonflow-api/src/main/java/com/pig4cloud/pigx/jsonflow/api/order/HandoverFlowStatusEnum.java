package com.pig4cloud.pigx.jsonflow.api.order;

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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工作交接流程状态
 *
 * @author luolin
 * @date 2020/2/17
 */
@Getter
@AllArgsConstructor
public enum HandoverFlowStatusEnum {

	/**
	 * 撤回
	 */
	RECALL(OrderStatusEnum.RECALL.getStatus(), "撤回"),
	/**
	 * 未交接（不能暂存）
	 */
    NO_HANDOVER(OrderStatusEnum.TEMP.getStatus(), "未交接"),
	/**
	 * 交接中
	 */
    RUN(OrderStatusEnum.RUN.getStatus(), "交接中"),
    /**
     * 已交接/完成
     */
    COMPLETED(OrderStatusEnum.NORMAL.getStatus(), "已交接"),
    /**
     * 流程作废
     */
    INVALID(OrderStatusEnum.INVALID.getStatus(), "作废"),
	/**
	 * 流程终止
	 */
	TERMINATE(OrderStatusEnum.TERMINATE.getStatus(), "终止");

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 描述
	 */
	private String description;

}
