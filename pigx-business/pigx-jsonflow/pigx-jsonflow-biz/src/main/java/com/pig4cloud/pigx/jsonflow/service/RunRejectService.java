package com.pig4cloud.pigx.jsonflow.service;

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

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunReject;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;

import java.util.List;

/**
 * 驳回记录
 *
 * @author luolin
 * @date 2021-03-05 11:56:29
 */
public interface RunRejectService extends IService<RunReject> {

	/**
	 * 驳回逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	List<RunJob> reject(RunJobVO runJobVO);

	/**
	 * 处理驳回逻辑
	 *
	 * @param runNodeId 运行节点ID
	 * @param runJobIds 运行任务集合
	 * @param isQuery   是否需查询
	 */
	List<RunReject> handleRejectNodes(List<Long> runNodeId, List<Long> runJobIds, boolean isQuery);

	/**
	 * 任务交接时，更新被驳回任务
	 *
	 * @param handoverUser 交接人
	 * @param receiveUser  接收人
	 * @param runJobs      运行任务集合
	 */
	void doHandleJobHandover(Long handoverUser, Long receiveUser, List<RunJob> runJobs);

}
