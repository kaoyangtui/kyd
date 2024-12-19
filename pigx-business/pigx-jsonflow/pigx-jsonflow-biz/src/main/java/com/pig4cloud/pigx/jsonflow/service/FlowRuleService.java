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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;

import java.util.List;
import java.util.Map;

/**
 * 条件/人员规则
 *
 * @author luolin
 * @date 2024-03-14 22:53:18
 */
public interface FlowRuleService extends IService<FlowRule> {

	/**
	 * 查询条件/人员规则
	 * @param flowRuleVO 条件/人员规则
	 * @return List
	 */
	List<FlowRule> listFlowRules(FlowRuleVO flowRuleVO);

	/**
	 * 查询请求头请求参数返回值
	 * @param flowRuleVO 条件/人员规则
	 * @param resMap 外部表单信息
	 * @return List
	 */
	List<FlowRule> listFlowRuleParams(FlowRuleVO flowRuleVO, Map<String, Object> resMap);

	/**
	 * 构造父子流程传参回参
	 * @param runNodeVO 运行节点
	 * @return Map
	 */
	Map<String, Object> buildSubFlowParams(RunNodeVO runNodeVO, String paramFrom);

	/**
	 * 构造请求头请求参数返回值
	 * @param flowRuleVO 条件/人员规则
	 * @param resMap 外部表单信息
	 * @return Map
	 */
	Map<String, Object> buildFlowRuleParams(FlowRuleVO flowRuleVO, List<FlowRule> flowRules, Map<String, Object> resMap);

	void removeByDefFlowId(Long defFlowId, Long flowInstId);

}
