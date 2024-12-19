package com.pig4cloud.pigx.jsonflow.service.impl;
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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.mapper.FlowRuleMapper;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import com.pig4cloud.pigx.jsonflow.util.CommonHttpUrlInvokeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 条件/人员规则
 *
 * @author luolin
 * @date 2024-03-14 22:55:26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowRuleServiceImpl extends ServiceImpl<FlowRuleMapper, FlowRule> implements FlowRuleService {

	@Lazy
	@Autowired
	private FlowVariableService flowVariableService;

	@Override
	public List<FlowRule> listFlowRules(FlowRuleVO flowRuleVO) {
		Long flowInstId = flowRuleVO.getFlowInstId();
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		return this.list(Wrappers.<FlowRule>lambdaQuery().eq(FlowRule::getDefFlowId, flowRuleVO.getDefFlowId())
				.eq(Objects.nonNull(flowRuleVO.getFlowNodeId()), FlowRule::getFlowNodeId, flowRuleVO.getFlowNodeId())
				.eq(Objects.nonNull(flowRuleVO.getFlowNodeRelId()), FlowRule::getFlowNodeRelId, flowRuleVO.getFlowNodeRelId())
				.eq(Objects.nonNull(flowRuleVO.getFlowClazzId()), FlowRule::getFlowClazzId, flowRuleVO.getFlowClazzId())
				.eq(StrUtil.isNotBlank(flowRuleVO.getType()), FlowRule::getType, flowRuleVO.getType())
				.eq(StrUtil.isNotBlank(flowRuleVO.getValType()), FlowRule::getValType, flowRuleVO.getValType())
				.eq(StrUtil.isNotBlank(flowRuleVO.getParamFrom()), FlowRule::getParamFrom, flowRuleVO.getParamFrom())
				.eq(Objects.nonNull(flowInstId), FlowRule::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowRule::getFlowInstId));
	}

	@Override
	public List<FlowRule> listFlowRuleParams(FlowRuleVO flowRuleVO, Map<String, Object> resMap) {
		List<FlowRule> flowRules = this.listFlowRules(flowRuleVO);
		flowRules.forEach(each -> {
			if (CommonNbrPool.STR_0.equals(flowRuleVO.getParamFrom()) || CommonNbrPool.STR_1.equals(flowRuleVO.getParamFrom())) {
				String orderPropKey = flowVariableService.getOrderPropKey(each.getTargetProp());
				each.setTargetProp(orderPropKey);
				if (CommonNbrPool.STR_0.equals(each.getParamValType()) || CommonNbrPool.STR_1.equals(each.getParamValType())) {
					Object obj = this.getKeyValFrom(each.getVarKeyVal(), flowRuleVO, "父子流程传参 或 Http请求头请求参数");
					String res = CommonHttpUrlInvokeUtils.isTypeJSON(obj);
					each.setVarKeyVal(res);
				}
			} else if (CommonNbrPool.STR_2.equals(flowRuleVO.getParamFrom())) {
				String orderPropKey = flowVariableService.getOrderPropKey(each.getVarKeyVal());
				each.setVarKeyVal(orderPropKey);
				if (CommonNbrPool.STR_0.equals(each.getParamValType())) {
					Object obj;
					if (Objects.isNull(resMap)) {
						obj = this.getKeyValFrom(each.getTargetProp(), flowRuleVO, "父子流程回参 或 Http请求回参");
					} else {
						obj = flowVariableService.getValByOrderInfo(each.getTargetProp(), null, null, "父子流程回参 或 Http请求回参", resMap);
					}
					String res = CommonHttpUrlInvokeUtils.isTypeJSON(obj);
					each.setTargetProp(res);
				}
			}
		});
		return flowRules;
	}

	private Object getKeyValFrom(String each, FlowRuleVO flowRuleVO, String errMsg) {
		return flowVariableService.handleInvokeKeyValFrom(each, flowRuleVO.getFlowKey(), flowRuleVO.getFlowInstId(), errMsg);
	}

	@Override
	public Map<String, Object> buildSubFlowParams(RunNodeVO runNodeVO, String paramFrom) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(runNodeVO.getDefFlowId());
		flowRuleVO.setFlowKey(runNodeVO.getFlowKey());
		flowRuleVO.setFlowInstId(runNodeVO.getFlowInstId());
		flowRuleVO.setFlowNodeId(runNodeVO.getFlowNodeId());
		flowRuleVO.setType(FlowParamRuleEnum.SUB_FLOW.getType());
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		flowRuleVO.setParamFrom(paramFrom);
		List<FlowRule> flowRules = this.listFlowRuleParams(flowRuleVO, null);
		return this.buildFlowRuleParams(flowRuleVO, flowRules, null);
	}

	@Override
	public Map<String, Object> buildFlowRuleParams(FlowRuleVO flowRuleVO, List<FlowRule> flowRules, Map<String, Object> resMap) {
		if (CollUtil.isEmpty(flowRules)) {
			flowRules = this.listFlowRuleParams(flowRuleVO, resMap);
		}
		Map<String, Object> params = new HashMap<>();
		if (CollUtil.isEmpty(flowRules)) return params;
		if (CommonNbrPool.STR_0.equals(flowRuleVO.getParamFrom()) || CommonNbrPool.STR_1.equals(flowRuleVO.getParamFrom())) {
			flowRules.forEach(each -> params.put(each.getTargetProp(), each.getVarKeyVal()));
		} else if (CommonNbrPool.STR_2.equals(flowRuleVO.getParamFrom())) {
			flowRules.forEach(each -> params.put(each.getVarKeyVal(), each.getTargetProp()));
		}
		return params;
	}

	@Override
	public void removeByDefFlowId(Long defFlowId, Long flowInstId) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		this.remove(Wrappers.<FlowRule>lambdaQuery().eq(FlowRule::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowRule::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowRule::getFlowInstId));
	}

}
