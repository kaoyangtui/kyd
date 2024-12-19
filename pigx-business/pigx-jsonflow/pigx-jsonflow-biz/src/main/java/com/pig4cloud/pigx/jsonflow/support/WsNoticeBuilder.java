package com.pig4cloud.pigx.jsonflow.support;

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

import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.WsNoticeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.WsNotice;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 对象构造
 *
 * @author luolin
 * @date 2021/3/19 9:01
 */
@UtilityClass
public class WsNoticeBuilder {

	/**
	 * 构建WS消息记录
	 *
	 * @param userId 用户iD
	 * @param data 数据
	 * @param status 状态
	 * @param type 类型
	 * @param jobType 任务类型
	 * @param flowKey 业务KEY
	 */
	private WsNotice wsNotice(Long flowInstId, Long userId, String data, String status, String type, String jobType, String flowKey) {
		WsNotice wsNotice = new WsNotice();
		wsNotice.setFlowInstId(flowInstId);
		wsNotice.setUserId(userId);
		wsNotice.setData(data);
		wsNotice.setStatus(status);
		wsNotice.setType(type);
		wsNotice.setJobType(jobType);
		wsNotice.setFlowKey(flowKey);
		wsNotice.setCreateUser(userId);
		wsNotice.setCreateTime(LocalDateTime.now());
		return wsNotice;
	}

	/**
	 * WS消息默认赋值：个人任务发起
	 *
	 * @param remindUsers 通知人
	 * @param data 数据
	 * @param flowKey 业务KEY
	 */
	public List<WsNotice> defUserWsNotice(Set<Long> remindUsers, String data, Long flowInstId, String flowKey) {
		List<WsNotice> wsNotices = new ArrayList<>();
		remindUsers.forEach(userId -> {
			WsNotice wsNotice = WsNoticeBuilder.wsNotice(flowInstId, userId, data, NodeJobStatusEnum.NO_START.getStatus(), WsNoticeTypeEnum.PERSONAL.getType()
					, JobUserTypeEnum.USER.getType(), flowKey);
			wsNotices.add(wsNotice);
		});
		return wsNotices;
	}

	/**
	 * WS消息默认赋值：个人任务运行中
	 *
	 * @param remindUsers 通知人
	 * @param data 数据
	 * @param flowKey 业务KEY
	 */
	public List<WsNotice> defUserWsNotice2(Set<Long> remindUsers, String data, Long flowInstId, String flowKey) {
		List<WsNotice> wsNotices = new ArrayList<>();
		remindUsers.forEach(userId -> {
			WsNotice wsNotice = WsNoticeBuilder.wsNotice(flowInstId, userId, data, NodeJobStatusEnum.RUN.getStatus(), WsNoticeTypeEnum.PERSONAL.getType()
					, JobUserTypeEnum.USER.getType(), flowKey);
			wsNotices.add(wsNotice);
		});
		return wsNotices;
	}

	/**
	 * WS消息默认赋值：个人任务完成
	 *
	 * @param remindUsers 通知人
	 * @param data 数据
	 * @param flowKey 业务KEY
	 */
	public List<WsNotice> defUserWsNotice3(Set<Long> remindUsers, String data, Long flowInstId, String flowKey) {
		List<WsNotice> wsNotices = new ArrayList<>();
		remindUsers.forEach(userId -> {
			WsNotice wsNotice = WsNoticeBuilder.wsNotice(flowInstId, userId, data, NodeJobStatusEnum.COMPLETE.getStatus(), WsNoticeTypeEnum.PERSONAL.getType()
					, JobUserTypeEnum.USER.getType(), flowKey);
			wsNotices.add(wsNotice);
		});
		return wsNotices;
	}

	/**
	 * WS消息默认赋值：组任务运行中
	 *
	 * @param remindUsers 通知人
	 * @param data 数据
	 * @param flowKey 业务KEY
	 */
	public List<WsNotice> defGroupWsNotice(Set<Long> remindUsers, String data, Long flowInstId, String flowKey) {
		List<WsNotice> wsNotices = new ArrayList<>();
		remindUsers.forEach(userId -> {
			WsNotice wsNotice = WsNoticeBuilder.wsNotice(flowInstId, userId, data, NodeJobStatusEnum.RUN.getStatus(), WsNoticeTypeEnum.GROUP.getType()
					, JobUserTypeEnum.ROLE.getType(), flowKey);
			wsNotices.add(wsNotice);
		});
		return wsNotices;
	}

	/**
	 * WS消息默认赋值：组任务完成
	 *
	 * @param remindUsers 通知人
	 * @param data 数据
	 * @param flowKey 业务KEY
	 */
	public List<WsNotice> defGroupWsNotice2(Set<Long> remindUsers, String data, Long flowInstId, String flowKey) {
		List<WsNotice> wsNotices = new ArrayList<>();
		remindUsers.forEach(userId -> {
			WsNotice wsNotice = WsNoticeBuilder.wsNotice(flowInstId, userId, data, NodeJobStatusEnum.COMPLETE.getStatus(), WsNoticeTypeEnum.GROUP.getType()
					, JobUserTypeEnum.ROLE.getType(), flowKey);
			wsNotices.add(wsNotice);
		});
		return wsNotices;
	}

}
