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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 子流程状态
 * @author luolin
 * @date 2018/9/30
 */
@Getter
@AllArgsConstructor
public enum SubFlowStatusEnum {

	/**
	 * 子流程撤回
	 */
	SUB_FLOW_RECALL(FlowStatusEnum.RECALL.getStatus(), "子流程撤回"),
	/**
	 * 子流程中
	 */
	SUB_FLOW_RUN(FlowStatusEnum.RUN.getStatus(), "子流程中"),
	/**
	 * 子流程完成
	 */
	SUB_FLOW_FINISH(FlowStatusEnum.FINISH.getStatus(), "子流程完成"),
	/**
	 * 子流程作废
	 */
	SUB_FLOW_INVALID(FlowStatusEnum.INVALID.getStatus(), "子流程作废"),
	/**
	 * 子流程终止
	 */
	SUB_FLOW_TERMINATE(FlowStatusEnum.TERMINATE.getStatus(), "子流程终止");

	/**
	 * 状态
	 */
	private final String status;
	/**
	 * 描述
	 */
	private final String description;
}
