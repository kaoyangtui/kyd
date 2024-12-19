package com.pig4cloud.pigx.jsonflow.engine;

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

import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;

import java.util.List;

/**
 * 节点公共处理器
 *
 * @author luolin
 * @date 2021/3/10 17:48
 */
public interface NodeJobHandler {

	/**
	 * 设置下一节点参与者
	 *
	 * @param runJobVO 运行任务
	 * @param runJobs  运行任务集合
	 */
	void setNextUsers(RunJobVO runJobVO, List<RunJob> runJobs);

	/**
	 * 通知下一节点参与者
	 *
	 * @param runJobVO 运行任务
	 * @param runJobs  运行任务集合
	 */
	void notify(RunJobVO runJobVO, List<RunJob> runJobs);

	/**
	 * 通知一个参与者，多个任务时，逗号分隔
	 *
	 * @param runJobVO     运行任务
	 * @param runJobs      运行任务集合
	 */
	void nodeNotifySingleUser(RunJobVO runJobVO, List<RunJob> runJobs);

	/**
	 * 处理简单模式条件组条件
	 * @param each 条件规则
	 * @param flowKey 流程KEY
	 * @param flowInstId 流程实例ID
	 * @param varKeyVal KEY取值来源
	 * @param errMsg 错误信息
	 * @return boolean
	 */
	boolean handleCondGroupCondition(FlowRule each, String flowKey, Long flowInstId, String varKeyVal, String errMsg);

}
