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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowClazz;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowClazzVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;

import java.util.LinkedList;
import java.util.List;

/**
 * 节点监听事件设置
 *
 * @author luolin
 * @date 2021-03-19 11:43:00
 */
public interface FlowClazzService extends IService<FlowClazz> {

	/**
	 * 新增或修改任务
	 *
	 * @param flowClazzVO 流程处理类
	 */
	Boolean saveOrUpdate(FlowClazzVO flowClazzVO);

	/**
	 * 处理节点监听事件
	 * 多次触发则需保证业务侧幂等
	 *
	 * @param runJobVO 运行任务
	 * @param methods  方法名称集合
	 */
	boolean handleFlowJobClazz(RunJobVO runJobVO, LinkedList<String> methods, boolean isCurrRunNode);

	/**
	 * 处理节点监听事件
	 * 多次触发则需保证业务侧幂等
	 *
	 * @param runJobVO 运行任务
	 * @param methods  方法名称集合
	 */
	boolean handleFlowNodeClazz(RunJobVO runJobVO, LinkedList<String> methods, boolean isCurrRunNode);

	/**
	 * 处理多任务监听事件，同节点只触发一次
	 * 多次触发则需保证业务侧幂等
	 *
	 * @param runJobs 运行任务集合
	 * @param methods  方法名称集合
	 */
	boolean handleNextJobClazzes(List<RunJob> runJobs, LinkedList<String> methods);

	/**
	 * 处理多节点监听事件
	 * 多次触发则需保证业务侧幂等
	 *
	 * @param runNodes 运行节点集合
	 * @param methods  方法名称集合
	 */
	boolean handleNextNodeClazzes(List<RunNode> runNodes, LinkedList<String> methods);

	/**
	 * 处理开启下一步节点任务监听事件
	 * 多次触发则需保证业务侧幂等
	 *
	 * @param runJobVO 运行任务
	 * @param nextRunJobs 下一步运行任务集合
	 * @param methods  方法名称集合
	 */
	boolean handleNextJobClazz(RunJobVO runJobVO, List<RunJob> nextRunJobs, LinkedList<String> methods);

	/**
	 * 处理开启下一步节点监听事件
	 * 多次触发则需保证业务侧幂等
	 *
	 * @param runJobVO 运行任务
	 * @param nextRunNodes 下一步运行节点集合
	 * @param methods  方法名称集合
	 */
	boolean handleNextNodeClazz(RunJobVO runJobVO, List<RunNode> nextRunNodes, LinkedList<String> methods);

	List<FlowClazz> listFlowClazzes(Long defFlowId, Long flowInstId);

	List<FlowClazz> listFlowClazzes(FlowClazz flowClazz);

	void removeByDefFlowId(Long defFlowId, Long flowInstId);

}
