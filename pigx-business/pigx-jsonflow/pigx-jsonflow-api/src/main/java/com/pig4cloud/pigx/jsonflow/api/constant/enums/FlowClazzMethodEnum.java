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
 * 流程、节点、任务事件
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum FlowClazzMethodEnum {

	// ---------------- 节点事件 ----------------
    /**
     * 当开启任务时
     */
	START_JOB("startJob", "当开启任务时"),

	/**
	 * 当完成任务时
	 */
	COMPLETE_JOB("completeJob", "当完成任务时"),

	/**
	 * 当开启下一步任务时
	 */
	START_NEXT_JOB("startNextJob", "当开启下一步任务时"),

	/**
	 * 当开启节点时
	 */
	START_NODE("startNode", "当开启节点时"),

	/**
     * 当完成节点时
     */
	COMPLETE_NODE("completeNode", "当完成节点时"),

	/**
	 * 当开启下一步节点时
	 */
	START_NEXT_NODE("startNextNode", "当开启下一步节点时"),

    /**
     * 当任务驳回时
     */
    REJECT("reject", "当任务驳回时"),

    /**
     * 当任务被驳回时
     */
    REJECTED("rejected", "当任务被驳回时"),

    /**
     * 当任务跳转时
     */
	ANY_JUMP("anyJump", "当任务跳转时"),

    /**
     * 当任务被跳转时
     */
	ANY_JUMPED("anyJumped", "当任务被跳转时"),

	/**
     * 当需分配参与者时
     */
	DIST_PERSON("distPerson", "当需分配参与者时"),

	// ---------------- 流程事件 ----------------
	/**
	 * 当流程发起时
	 */
	INITIATE("initiate", "当流程发起时"),

	/**
	 * 当流程完成时
	 */
	FINISH("finish", "当流程完成时"),

	/**
	 * 当流程被撤回时
	 */
	RECALL("recall", "当流程被撤回时"),

	/**
	 * 当流程被终止时
	 */
	TERMINATE("terminate", "当流程被终止时"),

	/**
	 * 当流程被作废时
	 */
	INVALID("invalid", "当流程被作废时");

	/**
	 * 方法名称
	 */
	private final String method;
	/**
	 * 描述
	 */
	private final String description;

	@SneakyThrows
	public static List<String> getRunNodeClazzes(){
		return CollUtil.newArrayList(FlowClazzMethodEnum.START_NODE.getMethod(), FlowClazzMethodEnum.COMPLETE_NODE.getMethod(), FlowClazzMethodEnum.START_NEXT_NODE.getMethod());
	}

}
