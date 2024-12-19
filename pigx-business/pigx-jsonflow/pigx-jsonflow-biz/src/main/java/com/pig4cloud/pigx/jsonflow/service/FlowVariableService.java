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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowVariable;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 流程参数管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:28
 */
public interface FlowVariableService extends IService<FlowVariable> {

	/**
	 * 流程发起记录变量及值
	 *
	 * @param params 参数
	 */
	boolean recordFlowVars(Map<String, Object> params);

	/**
	 * 更新流程参数
	 *
	 * @param params 参数
	 */
	boolean updateFlowVariables(Map<String, Object> params);

	/**
	 * 处理拥有节点条件的节点
	 *
	 * @param nextRunNodes   下一运行节点集合
	 * @param flowNodeRels 流程条件配置
	 */
	List<RunNode> handleFlowNodeVariable(List<RunNode> nextRunNodes, List<FlowNodeRel> flowNodeRels);

	/**
	 * 获取流程条件
	 *
	 * @param flowInstId 下一节点
	 */
	List<FlowVariable> listFlowVariables(Long flowInstId);

	Iterator<Map.Entry<Long, List<FlowRule>>> getEntryIterator(List<FlowRule> flowRules);

	/**
	 * 处理条件运算符比较
	 * @param each 条件规则
	 * @param obj 返回值
	 */
	boolean doConditionCompareTo(FlowRule each, Object obj);

	/**
	 * 处理任务自身的条件
	 *
	 * @param runJobs 运行任务集合
	 */
	List<RunJob> handleFlowNodeJobVariable(List<RunJob> runJobs);

	/**
	 * 处理拥有节点条件的串行与并行节点关系
	 *
	 * @param flowNodeRels 流程条件配置
	 */
	List<FlowNodeRel> handleFlowNodeRelSerialParallelVariable(List<FlowNodeRel> flowNodeRels, Long flowInstId);

	/**
	 * 处理KEY取值来源
	 * @param keyVal KEY取值来源
	 * @param flowKey 流程KEY
	 * @param flowInstId 流程实例ID
	 * @param errMsg 错误信息
	 * @return Object
	 */
	Object handleInvokeKeyValFrom(String keyVal, String flowKey, Long flowInstId, String errMsg);

	/**
	 * 提取传入的外部表单字段值
	 * @param keyVal KEY取值来源
	 * @param flowKey 流程KEY
	 * @param flowInstId 流程实例ID
	 * @param errMsg 错误信息
	 * @param resMap 外部表单信息
	 * @return Object
	 */
	Object getValByOrderInfo(String keyVal, String flowKey, Long flowInstId, String errMsg, Map<String, Object> resMap);

	/**
	 * 获取表单属性KEY
	 * @param keyVal KEY取值来源
	 * @return String
	 */
	String getOrderPropKey(String keyVal);

	/**
	 * 处理取值来源参数
	 * @param varKeyVal 取值来源
	 */
	boolean invokeMethodByParamTypes(String varKeyVal, Long flowInstId, String flowKey);

	/**
	 * 获取 SpEL 表达式的值
	 *
	 * @param flowInstId 流程实例ID
	 * @param flowKey 流程KEY
	 * @param varKeyVal  条件表达式
	 */
	boolean evalSpELVarValue(Long flowInstId, String flowKey, String varKeyVal);

}
