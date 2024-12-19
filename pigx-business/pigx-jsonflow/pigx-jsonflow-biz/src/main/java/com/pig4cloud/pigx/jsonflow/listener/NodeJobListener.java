package com.pig4cloud.pigx.jsonflow.listener;

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

import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;

import java.util.List;

/**
 * 节点监听事件
 *
 * @author luolin
 * @date 2021/3/10 17:48
 */
public interface NodeJobListener {

	/**
	 * 当开启任务时
	 *
	 * @param runJobVO 运行任务
	 */
	default void startJob(RunJobVO runJobVO) {};

	/**
	 * 当完成任务时
	 *
	 * @param runJobVO 运行任务
	 */
	default void completeJob(RunJobVO runJobVO) {};

	/**
	 * 当开启下一步任务时
	 *
	 * @param runJobVO 运行任务
	 * @param nextRunJobs 下一步运行任务集合
	 */
	default void startNextJob(RunJobVO runJobVO, List<RunJob> nextRunJobs) {};

	/**
	 * 当开启节点时
	 *
	 * @param runNodeVO 运行节点
	 */
	default void startNode(RunNodeVO runNodeVO) {};

	/**
	 * 当完成节点时
	 *
	 * @param runNodeVO 运行节点
	 */
	default void completeNode(RunNodeVO runNodeVO) {};

	/**
	 * 当开启下一步节点时
	 *
	 * @param runNodeVO 运行节点
	 * @param nextRunNodes 下一步运行节点集合
	 */
	default void startNextNode(RunNodeVO runNodeVO, List<RunNode> nextRunNodes) {};

	/**
	 * 当任务驳回时
	 *
	 * @param runJobVO 运行任务
	 */
	default void reject(RunJobVO runJobVO) {};

	/**
	 * 当任务被驳回时
	 *
	 * @param runJobVO 运行任务
	 */
	default void rejected(RunJobVO runJobVO) {};

	/**
	 * 当任务跳转时
	 *
	 * @param runJobVO 运行任务
	 */
	default void anyJump(RunJobVO runJobVO) {};

	/**
	 * 当任务被跳转时
	 *
	 * @param runJobVO 运行任务
	 */
	default void anyJumped(RunJobVO runJobVO) {};

	/**
	 * 当需分配参与者时
	 *
	 * @param runJobVO 运行任务
	 */
	default void distPerson(RunJobVO runJobVO) {};

}
