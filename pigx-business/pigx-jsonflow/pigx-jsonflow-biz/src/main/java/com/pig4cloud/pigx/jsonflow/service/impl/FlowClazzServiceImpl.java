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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowClazzMethodEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowClazz;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowClazzVO;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.engine.NodeJobHandler;
import com.pig4cloud.pigx.jsonflow.mapper.FlowClazzMapper;
import com.pig4cloud.pigx.jsonflow.service.FlowClazzService;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import com.pig4cloud.pigx.jsonflow.util.CommonHttpUrlInvokeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程节点事件
 *
 * @author luolin
 * @date 2021-03-19 11:43:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowClazzServiceImpl extends ServiceImpl<FlowClazzMapper, FlowClazz> implements FlowClazzService {

	private final FlowVariableService flowVariableService;
	private final NodeJobHandler nodeJobHandler;

	@Override
	public Boolean saveOrUpdate(FlowClazzVO flowClazzVO) {
		FlowClazz flowClazz = new FlowClazz();
		BeanUtil.copyProperties(flowClazzVO, flowClazz);
		this.saveOrUpdate(flowClazz);
		flowClazzVO.setId(flowClazz.getId());
		return Boolean.TRUE;
	}

	@Override
	public boolean handleFlowJobClazz(RunJobVO runJobVO, LinkedList<String> methods, boolean isCurrRunNode) {
		Long flowNodeId = Objects.nonNull(runJobVO.getRunNodeVO()) && !isCurrRunNode ? runJobVO.getRunNodeVO().getFlowNodeId() : runJobVO.getFlowNodeId();
		List<FlowClazz> flowClazzes = this.buildFlowClazzes(runJobVO, methods, flowNodeId);
		this.doHandleFlowClazz(runJobVO, null, null, methods, flowClazzes, flowNodeId);
		return true;
	}

	@Override
	public boolean handleFlowNodeClazz(RunJobVO runJobVO, LinkedList<String> methods, boolean isCurrRunNode) {
		this.handleFlowJobClazz(runJobVO, methods, isCurrRunNode);
		return true;
	}

	private List<FlowClazz> buildFlowClazzes(RunJobVO runJobVO, LinkedList<String> methods, Long flowNodeId) {
		if (CollUtil.isEmpty(methods)) return null;
		List<FlowClazz> flowClazzes = this.listFlowClazzes(runJobVO.getDefFlowId(), runJobVO.getFlowInstId());
		if (CollUtil.isEmpty(flowClazzes)) return null;
		flowClazzes = flowClazzes.stream().filter(flowClazz -> methods.stream().anyMatch(method -> ArrayUtil.contains(flowClazz.getMethods(), method))
						&& ((Objects.nonNull(flowNodeId) && flowNodeId.equals(flowClazz.getFlowNodeId()) && CommonNbrPool.STR_0.equals(flowClazz.getType()))
						|| CommonNbrPool.STR_1.equals(flowClazz.getType())))
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(flowClazzes)) return null;
		// 处理节点监听条件过滤
		flowClazzes = this.handleSpecialFlowClazz(runJobVO.getFlowInstId(), runJobVO.getFlowKey(), flowClazzes);
		if (CollUtil.isEmpty(flowClazzes)) return null;
		return flowClazzes;
	}

	private void doHandleFlowClazz(RunJobVO runJobVO, List<RunNode> nextRunNodes, List<RunJob> nextRunJobs, LinkedList<String> methods, List<FlowClazz> flowClazzes, Long flowNodeId) {
		if (CollUtil.isEmpty(flowClazzes)) return;
		methods.forEach(method -> flowClazzes.forEach(flowClazz -> {
			// 处理监听类事件
			if (StrUtil.isNotBlank(flowClazz.getClazz())) {
				Object bean = SpringContextHolder.getBean(flowClazz.getClazz());
				Class<?> clazz = bean.getClass();
				if (!ArrayUtil.contains(flowClazz.getMethods(), method)) return;
				try {
					// 节点事件传递RunNodeVO参数
					if (FlowClazzMethodEnum.getRunNodeClazzes().contains(method)) {
						if (CollUtil.isNotEmpty(nextRunNodes)) {
							Method invokeMethod = clazz.getDeclaredMethod(method, RunNodeVO.class, List.class);
							invokeMethod.invoke(bean, runJobVO.getRunNodeVO(), nextRunNodes);
						} else {
							Method invokeMethod = clazz.getDeclaredMethod(method, RunNodeVO.class);
							invokeMethod.invoke(bean, runJobVO.getRunNodeVO());
						}
					} else {
						if (CollUtil.isNotEmpty(nextRunJobs)) {
							Method invokeMethod = clazz.getDeclaredMethod(method, RunJobVO.class, List.class);
							invokeMethod.invoke(bean, runJobVO, nextRunJobs);
						} else {
							Method invokeMethod = clazz.getDeclaredMethod(method, RunJobVO.class);
							invokeMethod.invoke(bean, runJobVO);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("流程节点事件 调用异常" + e.getMessage());
				}
			}
			// 处理Http调用请求
			try {
				String httpUrl = flowClazz.getHttpUrl();
				if (StrUtil.isBlank(httpUrl)) return;
				FlowRuleVO flowRuleVO = this.buildFlowRuleVO(runJobVO, flowClazz, flowNodeId);
				flowRuleVO.setHttpUrl(httpUrl);
				flowRuleVO.setHttpMethod(flowClazz.getHttpMethod());
				CommonHttpUrlInvokeUtils.handleHttpInvoke(flowRuleVO);
			} catch (Exception e) {
				throw new RuntimeException("流程节点事件 Http调用异常" + e.getMessage());
			}
		}));
	}

	private FlowRuleVO buildFlowRuleVO(RunJobVO runJobVO, FlowClazz flowClazz, Long flowNodeId) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(runJobVO.getDefFlowId());
		flowRuleVO.setFlowKey(runJobVO.getFlowKey());
		flowRuleVO.setFlowInstId(runJobVO.getFlowInstId());
		// 判断是否为节点事件
		if (CommonNbrPool.STR_0.equals(flowClazz.getType())) {
			flowRuleVO.setFlowNodeId(flowNodeId);
		}
		flowRuleVO.setFlowClazzId(flowClazz.getId());
		flowRuleVO.setType(FlowParamRuleEnum.LISTENER.getType());
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		return flowRuleVO;
	}

	@Override
	public boolean handleNextJobClazzes(List<RunJob> runJobs, LinkedList<String> methods) {
		// 同节点只触发一次
		Map<Long, List<RunJob>> mapList = runJobs.stream().collect(Collectors.groupingBy(RunJob::getFlowNodeId));
		mapList.forEach((key, each) -> {
			RunJobVO runJobVO = new RunJobVO();
			BeanUtil.copyProperties(each.get(0), runJobVO);
			this.handleFlowJobClazz(runJobVO, methods, false);
		});
		return true;
	}

	@Override
	public boolean handleNextNodeClazzes(List<RunNode> runNodes, LinkedList<String> methods) {
		runNodes.forEach(each -> {
			RunJobVO runJobVO = new RunJobVO();
			BeanUtil.copyProperties(each, runJobVO);
			RunNodeVO runNodeVO = new RunNodeVO();
			BeanUtil.copyProperties(each, runNodeVO);
			runJobVO.setRunNodeVO(runNodeVO);
			this.handleFlowNodeClazz(runJobVO, methods, false);
		});
		return true;
	}

	/**
	 * 处理节点监听条件过滤：查找符合条件的监听
	 *
	 * @param flowInstId 流程实例ID
	 * @param flowClazzes 流程监听事件
	 */
	private List<FlowClazz> handleSpecialFlowClazz(Long flowInstId, String flowKey, List<FlowClazz> flowClazzes) {
		return flowClazzes.stream().filter(each -> {
			String varKeyVal = each.getVarKeyVal();
			// 取值来源为空则默认为满足条件
			if (StrUtil.isEmpty(varKeyVal)) return true;
			if (DistValTypeEnum.SIMPLE.getType().equals(each.getValType())) {
				FlowRule flowRule = new FlowRule();
				flowRule.setVarVal(each.getVarVal());
				flowRule.setOperator(each.getOperator());
				return nodeJobHandler.handleCondGroupCondition(flowRule, flowKey, flowInstId, varKeyVal,  "监听事件条件值");
			}
			else if (DistValTypeEnum.SPEL_FIXED.getType().equals(each.getValType())) {
				return flowVariableService.evalSpELVarValue(flowInstId, flowKey, varKeyVal);
			}
			else if (DistValTypeEnum.COMPLEX.getType().equals(each.getValType())) {
				return flowVariableService.invokeMethodByParamTypes(varKeyVal, flowInstId, flowKey);
			}
			throw new ValidationException("监听事件条件值设置错误");
		}).collect(Collectors.toList());
	}

	@Override
	public boolean handleNextJobClazz(RunJobVO runJobVO, List<RunJob> nextRunJobs, LinkedList<String> methods) {
		Long flowNodeId = runJobVO.getFlowNodeId();
		List<FlowClazz> flowClazzes = this.buildFlowClazzes(runJobVO, methods, flowNodeId);
		this.doHandleFlowClazz(runJobVO, null, nextRunJobs, methods, flowClazzes, flowNodeId);
		return true;
	}

	@Override
	public boolean handleNextNodeClazz(RunJobVO runJobVO, List<RunNode> nextRunNodes, LinkedList<String> methods) {
		Long flowNodeId = runJobVO.getFlowNodeId();
		List<FlowClazz> flowClazzes = this.buildFlowClazzes(runJobVO, methods, flowNodeId);
		this.doHandleFlowClazz(runJobVO, nextRunNodes, null, methods, flowClazzes, flowNodeId);
		return true;
	}

	@Override
	public List<FlowClazz> listFlowClazzes(Long defFlowId, Long flowInstId) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		return this.list(Wrappers.<FlowClazz>lambdaQuery().eq(FlowClazz::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowClazz::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowClazz::getFlowInstId)
				.orderByAsc(FlowClazz::getSort));
	}

	@Override
	public List<FlowClazz> listFlowClazzes(FlowClazz flowClazz) {
		Long flowInstId = RunFlowContextHolder.validateIndependent(flowClazz.getFlowInstId());
		flowClazz.setFlowInstId(null);
		return this.list(Wrappers.lambdaQuery(flowClazz).eq(Objects.nonNull(flowInstId), FlowClazz::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowClazz::getFlowInstId));
	}

	@Override
	public void removeByDefFlowId(Long defFlowId, Long flowInstId) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		this.remove(Wrappers.<FlowClazz>lambdaQuery().eq(FlowClazz::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FlowClazz::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FlowClazz::getFlowInstId));
	}

}
