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
 * 任务状态
 * @author luolin
 * @date 2018/9/30
 */
@Getter
@AllArgsConstructor
public enum NodeJobStatusEnum {

	/**
	 * 流程终止（仅回显）
	 */
	TERMINATE("-3", "已终止"),
	/**
	 * 流程作废（仅回显）
	 */
	INVALID("-2", "已作废"),
    /**
     * 未开始（含条件不满足）
     */
    NO_START("-1", "未开始"),
    /**
     * 运行中
     */
    RUN("0", "审批中"),
    /**
     * 结束
     */
    COMPLETE("1", "结束"),
    /**
     * 驳回中
     */
    REJECT("2", "驳回中"),
    /**
     * 跳过
     */
    SKIP("3", "跳过"),
    /**
     * 被驳回
     */
    REJECTED("9", "被驳回"),

    /** 以下为任务的额外状态 **/
	/**
	 * 激活
	 */
	ACTIVE("0", "激活"),
	/**
	 * 挂起
	 */
	SUSPEND("1", "挂起"),

	/** 以下为驳回状态 **/
	/**
	 * 驳回中
	 */
	REJ_RUN("0", "驳回中"),
	/**
	 * 结束
	 */
	REJ_COMP("1", "结束");

	/**
	 * 状态
	 */
	private final String status;
	/**
	 * 描述
	 */
	private final String description;

	@SneakyThrows
	public static List<String> getRunRejectedStatuses(){
		return CollUtil.newArrayList(NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
	}

	@SneakyThrows
	public static List<String> getRunRejectRejectedStatuses(){
		return CollUtil.newArrayList(NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.REJECT.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
	}

	@SneakyThrows
	public static List<String> getRunRejectRejectedCompleteStatuses(){
		return CollUtil.newArrayList(NodeJobStatusEnum.RUN.getStatus(), NodeJobStatusEnum.COMPLETE.getStatus(), NodeJobStatusEnum.REJECT.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
	}

	@SneakyThrows
	public static List<String> getRejectRejectedCompleteStatuses(){
		return CollUtil.newArrayList(NodeJobStatusEnum.COMPLETE.getStatus(), NodeJobStatusEnum.REJECT.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
	}

	@SneakyThrows
	public static List<String> getRejectRejectedStatuses(){
		return CollUtil.newArrayList(NodeJobStatusEnum.REJECT.getStatus(), NodeJobStatusEnum.REJECTED.getStatus());
	}

}
