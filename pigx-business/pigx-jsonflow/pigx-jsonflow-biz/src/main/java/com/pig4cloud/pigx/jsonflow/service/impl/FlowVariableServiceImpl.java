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

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.ValidationExceptions;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.KeyValFromEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.OperatorTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowVariable;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.mapper.FlowVariableMapper;
import com.pig4cloud.pigx.jsonflow.service.DistPersonService;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeRelService;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import com.pig4cloud.pigx.jsonflow.support.FlowVarContextHolder;
import com.pig4cloud.pigx.jsonflow.support.OrderInfoContextHolder;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import com.pig4cloud.pigx.jsonflow.util.AuthorizationHeaderUtils;
import com.pig4cloud.pigx.jsonflow.util.ClassMethodInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.CommonHttpUrlInvokeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程参数管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowVariableServiceImpl extends ServiceImpl<FlowVariableMapper, FlowVariable> implements FlowVariableService {

	@Autowired
	private DistPersonService distPersonService;
	@Lazy
	@Autowired
	private RunJobService runJobService;
	@Autowired
	private RunNodeService runNodeService;
	@Autowired
	private FlowNodeRelService flowNodeRelService;
	@Autowired
	private FlowRuleService flowRuleService;

	@Override
	public boolean recordFlowVars(Map<String, Object> params) {
		List<FlowVariable> flowVars = validateFlowVariables(params);
		if (flowVars == null) return true;
		flowVars.forEach(baseMapper::insert);
		// baseMapper.insertBatchSomeColumn(flowVars);
		return true;
	}

	private List<FlowVariable> validateFlowVariables(Map<String, Object> params) {
		List<FlowVariable> flowVars = this.buildFlowVariables(params);
		if (CollUtil.isEmpty(flowVars)) return CollUtil.newArrayList();
		FlowVariable flowVariable = flowVars.get(0);
		if (Objects.isNull(flowVariable.getFlowInstId())) throw new ValidationException(ValidationExceptions.NO_FLOWINSTID);
		if (StrUtil.isEmpty(flowVariable.getFlowKey())) throw new ValidationException(ValidationExceptions.NO_FLOW_KEY);
		if (StrUtil.isEmpty(flowVariable.getCode())) throw new ValidationException(ValidationExceptions.NO_FLOW_CODE);
		return flowVars;
	}

	@Override
	public boolean updateFlowVariables(Map<String, Object> params) {
		distPersonService.updateDistPersons(params);
		List<FlowVariable> flowVars = validateFlowVariables(params);
		// 带参数防止删除之前的数据
		List<String> keys = flowVars.stream().map(FlowVariable::getVarKey).collect(Collectors.toList());
		this.remove(Wrappers.<FlowVariable>lambdaQuery().eq(FlowVariable::getFlowInstId, flowVars.get(0).getFlowInstId())
				.in(FlowVariable::getVarKey, keys));
		flowVars.forEach(baseMapper::insert);
		// baseMapper.insertBatchSomeColumn(flowVars);
		return true;
	}

	/**
	 * 构建流程参数
	 *
	 * @param params 参数
	 */
	private List<FlowVariable> buildFlowVariables(Map<String, Object> params) {
		List<FlowVariable> flowVars = new ArrayList<>();
		params.forEach((key, value) -> {
			if (StrUtil.contains(key, FlowCommonConstants.FLOW_USERS)) return;
			if (ObjectUtil.isEmpty(value)) throw new ValidationException("流程条件值不能为空");
			FlowVariable flowVar = new FlowVariable();
			flowVar.setVarKey(key);
			flowVar.setVarVal(value.toString());
			flowVar.setFlowInstId(MapUtil.getLong(params, FlowEntityInfoConstants.FLOW_INST_ID));
			flowVar.setFlowKey(MapUtil.getStr(params, FlowEntityInfoConstants.FLOW_KEY));
			flowVar.setCode(MapUtil.getStr(params, FlowEntityInfoConstants.CODE));
			flowVar.setCreateUser(MapUtil.getLong(params, FlowEntityInfoConstants.CREATE_USER));
			flowVar.setCreateTime(LocalDateTime.now());
			flowVars.add(flowVar);
		});
		return flowVars;
	}

	/**
	 * 处理节点流转条件
	 *
	 * @param nextRunNodes 下一运行节点集合
	 * @param flowNodeRels 流程条件配置
	 */
	@Override
	public List<RunNode> handleFlowNodeVariable(List<RunNode> nextRunNodes, List<FlowNodeRel> flowNodeRels) {
		if (CollUtil.isEmpty(flowNodeRels)) return nextRunNodes;
		Long flowInstId = nextRunNodes.get(0).getFlowInstId();
		// 不符合条件的节点默认不开始
		List<RunNode> noMatchConRunNodes = nextRunNodes.stream().filter(runNode -> {
			List<FlowNodeRel> conditions = flowNodeRels.stream().filter(f -> f.getToFlowNodeId().equals(runNode.getFlowNodeId())).collect(Collectors.toList());
			return doHandleFlowVariable(conditions, flowInstId);
		}).collect(Collectors.toList());
		// 去除不符合条件的节点
		if (CollUtil.isNotEmpty(noMatchConRunNodes)) {
			// 将状态设置为跳过
			this.handleSkipNodes(noMatchConRunNodes, NodeJobStatusEnum.SKIP.getStatus());
			nextRunNodes = nextRunNodes.stream().filter(f -> noMatchConRunNodes.stream().noneMatch(any -> f.getId().equals(any.getId()))).collect(Collectors.toList());
		}
		return nextRunNodes;
	}

	/**
	 * 处理节点符合流转条件的关系
	 *
	 * @param flowNodeRels 流程条件配置
	 */
	private List<FlowNodeRel> handleFlowNodeRelVariable(List<FlowNodeRel> flowNodeRels, Long flowInstId) {
		if (CollUtil.isEmpty(flowNodeRels)) return flowNodeRels;
		return flowNodeRels.stream().filter(f -> !this.handleNoMatchCond(f, flowInstId)).collect(Collectors.toList());
	}

	/**
	 * 处理条件判断
	 *
	 * @param conditions 配置条件
	 * @return
	 */
	private boolean doHandleFlowVariable(List<FlowNodeRel> conditions, Long flowInstId) {
		if (CollUtil.isEmpty(conditions)) return false;
		return conditions.stream().allMatch(any -> this.handleNoMatchCond(any, flowInstId));
	}

	@Override
	public List<FlowVariable> listFlowVariables(Long flowInstId) {
		List<FlowVariable> variables = this.list(Wrappers.<FlowVariable>lambdaQuery().eq(FlowVariable::getFlowInstId, flowInstId));
		if (CollUtil.isEmpty(variables)) throw new ValidationException("当前流程条件不能为空");
		return variables;
	}

	/**
	 * 更新不符合条件节点状态
	 *
	 * @param runNodes 运行节点集合
	 * @param status   状态
	 */
	private void handleSkipNodes(List<RunNode> runNodes, String status) {
		// 更新节点
		List<Long> nextIds = runNodes.stream().map(RunNode::getId).collect(Collectors.toList());
		runNodeService.lambdaUpdate().set(RunNode::getStatus, status)
				.eq(RunNode::getStatus, NodeJobStatusEnum.NO_START.getStatus())
				.in(RunNode::getId, nextIds)
				.update();
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().in(RunJob::getRunNodeId, nextIds));
		// 不存在任务的节点则不更新
		if (CollUtil.isEmpty(runJobs)) return;
		this.handleSkipJobs(runJobs, status);
	}

	/**
	 * 处理流程条件
	 *
	 * @param each        流程条件
	 */
	private boolean handleNoMatchCond(FlowNodeRel each, Long flowInstId) {
		String varKeyVal = each.getVarKeyVal();
		// 取值来源为空则默认为满足条件
		if (StrUtil.isEmpty(varKeyVal)) return false;
		if (DistValTypeEnum.SIMPLE.getType().equals(each.getValType())) {
			return !this.handleCondGroupsCondition(each, flowInstId);
		}
		else if (DistValTypeEnum.SPEL_FIXED.getType().equals(each.getValType())) {
			return !this.evalSpELVarValue(flowInstId, each.getFlowKey(), varKeyVal);
		}
		else if (DistValTypeEnum.COMPLEX.getType().equals(each.getValType())) {
			return !this.invokeMethodByParamTypes(varKeyVal, flowInstId, each.getFlowKey());
		}
		else if (DistValTypeEnum.HTTP.getType().equals(each.getValType())) {
			return !this.handleHttpInvoke(each, flowInstId);
		}
		throw new ValidationException("流程条件值设置错误");
	}

	private boolean handleHttpInvoke(FlowNodeRel each, Long flowInstId) {
		// 处理Http调用请求
		String httpUrl = each.getVarKeyVal();
		if (StrUtil.isBlank(httpUrl)) return true;
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(each.getDefFlowId());
		flowRuleVO.setFlowKey(each.getFlowKey());
		flowRuleVO.setFlowInstId(flowInstId);
		flowRuleVO.setFlowNodeRelId(each.getId());
		flowRuleVO.setType(FlowParamRuleEnum.LINK.getType());
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		flowRuleVO.setHttpUrl(httpUrl);
		flowRuleVO.setHttpMethod(each.getHttpMethod());
		Object obj = CommonHttpUrlInvokeUtils.handleHttpInvoke(flowRuleVO);
		String fromVal = Objects.toString(obj, null);
		return FlowCommonConstants.YES.equals(fromVal);
	}

	private boolean handleCondGroupsCondition(FlowNodeRel each, Long flowInstId) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(each.getDefFlowId());
		flowRuleVO.setFlowNodeRelId(each.getId());
		flowRuleVO.setType(FlowParamRuleEnum.LINK.getType());
		flowRuleVO.setValType(DistValTypeEnum.SIMPLE.getType());
		List<FlowRule> flowRules = flowRuleService.listFlowRules(flowRuleVO);
		// 条件规则为空则默认为满足条件
		if (CollUtil.isEmpty(flowRules)) return true;
		// 处理不同组条件
		Iterator<Map.Entry<Long, List<FlowRule>>> iterator = this.getEntryIterator(flowRules);
		boolean groups = true;
		String groupsType = flowRules.get(0).getGroupsType();
		if (CommonNbrPool.STR_0.equals(groupsType)) {
			while (iterator.hasNext()) {
				groups = this.isGroup(each.getFlowKey(), flowInstId, iterator);
				if (!groups) break;
			}
		} else if (CommonNbrPool.STR_1.equals(groupsType)) {
			groups = false;
			while (iterator.hasNext()) {
				groups = this.isGroup(each.getFlowKey(), flowInstId, iterator);
				if (groups) break;
			}
		}
		return groups;
	}

	@Override
	public Iterator<Map.Entry<Long, List<FlowRule>>> getEntryIterator(List<FlowRule> flowRules) {
		Map<Long, List<FlowRule>> conGroupMap = flowRules.stream().collect(Collectors.groupingBy(FlowRule::getGroupId));
		Set<Map.Entry<Long, List<FlowRule>>> entries = conGroupMap.entrySet();
		return entries.iterator();
	}

	private boolean isGroup(String flowKey, Long flowInstId, Iterator<Map.Entry<Long, List<FlowRule>>> iterator) {
		boolean group = true;
		List<FlowRule> conGroup = iterator.next().getValue();
		String groupType = conGroup.get(0).getGroupType();
		if (CommonNbrPool.STR_1.equals(groupType)) {
			group = false;
		}
		// 处理组内各个条件
		for (FlowRule condition : conGroup) {
			boolean b = this.handleCondGroupCondition(condition, flowKey, flowInstId, condition.getVarKeyVal());
			if (CommonNbrPool.STR_0.equals(groupType)) {
				group = b;
				if (!group) break;
			} else {
				group = group || b;
				if (group) break;
			}
		}
		return group;
	}

	private boolean handleCondGroupCondition(FlowRule each, String flowKey, Long flowInstId, String varKeyVal){
		Object obj = null;
		if (KeyValFromEnum.getVarOrderUser().stream().anyMatch(varKeyVal::contains)) {
			obj = this.handleInvokeKeyValFrom(varKeyVal, flowKey, flowInstId, "流程条件");
		}
		return this.doConditionCompareTo(each, obj);
	}

	@Override
	public boolean doConditionCompareTo(FlowRule each, Object obj) {
		// 值为空则默认为不满足条件
		if (ObjectUtil.isEmpty(obj)) return false;
		// 判断是否为数组
		if(obj instanceof List) {
			return this.handleListContains(each.getVarVal(), each.getOperator(), obj);
		} else {
			if (OperatorTypeEnum.getEqualNotEqualContainsNotContains().contains(each.getOperator())) {
				return this.handleStringCompareTo(each.getVarVal(), each.getOperator(), obj);
			}
			return this.handleNumberCompareTo(each.getVarVal(), each.getOperator(), obj);
		}
	}

	private boolean handleListContains(String varVal, String operator, Object obj) {
		// 转String比较
		List<String> subKeyVals = ((List<?>) obj).stream().map(String::valueOf).collect(Collectors.toList());
		if (CollUtil.isEmpty(subKeyVals)) {
			// 值为空时，不包含认为满足条件
			return OperatorTypeEnum.NOT_CONTAINS.getType().equals(operator);
		}
		boolean bool = false;
		if (OperatorTypeEnum.CONTAINS.getType().equals(operator))
			bool = subKeyVals.contains(varVal);
		else if (OperatorTypeEnum.NOT_CONTAINS.getType().equals(operator))
			bool = !subKeyVals.contains(varVal);
		return bool;
	}

	private boolean handleStringCompareTo(String varVal, String operator, Object obj) {
		String varValFrom = Objects.toString(obj, null);
		boolean bool = false;
		if (OperatorTypeEnum.EQUAL.getType().equals(operator))
			bool = varValFrom.equals(varVal);
		else if (OperatorTypeEnum.NOT_EQUAL.getType().equals(operator))
			bool = !varValFrom.equals(varVal);
		else if (OperatorTypeEnum.CONTAINS.getType().equals(operator))
			bool = varValFrom.contains(varVal);
		else if (OperatorTypeEnum.NOT_CONTAINS.getType().equals(operator))
			bool = !varValFrom.contains(varVal);
		return bool;
	}

	private boolean handleNumberCompareTo(String varVal, String operator, Object obj) {
		String varValFrom = Objects.toString(obj, null);
		int compareTo = new BigDecimal(varValFrom).compareTo(new BigDecimal(varVal));
		boolean bool = false;
		if (OperatorTypeEnum.EQUAL.getType().equals(operator))
			bool = compareTo == 0;
		else if (OperatorTypeEnum.NOT_EQUAL.getType().equals(operator))
			bool = compareTo != 0;
		else if (OperatorTypeEnum.LESS_THAN.getType().equals(operator))
			bool = compareTo < 0;
		else if (OperatorTypeEnum.MORE_THAN.getType().equals(operator))
			bool = compareTo > 0;
		else if (OperatorTypeEnum.LESS_EQUAL_THAN.getType().equals(operator))
			bool = compareTo <= 0;
		else if (OperatorTypeEnum.MORE_EQUAL_THAN.getType().equals(operator))
			bool = compareTo >= 0;
		return bool;
	}

	@Override
	public Object handleInvokeKeyValFrom(String keyVal, String flowKey, Long flowInstId, String errMsg) {
		if (keyVal.contains(KeyValFromEnum.FLOW.getFrom())) {
			RunFlow runFlow = RunFlowContextHolder.initRunFlow(flowKey, flowInstId, null);
			DynaBean bean = DynaBean.create(runFlow);
			String key = keyVal.replaceAll(KeyValFromEnum.FLOW.getFrom(), StrUtil.EMPTY);
			if(bean.containsProp(key)) {
				return bean.get(key);
			} else throw new ValidationException("当前流程实例信息不存在"+ errMsg +"取值来源【" + key + "】变量");
		} else if (keyVal.contains(KeyValFromEnum.VAR.getFrom())) {
			String key = keyVal.replaceAll(KeyValFromEnum.VAR.getFrom(), StrUtil.EMPTY);
			List<FlowVariable> variables = FlowVarContextHolder.initFlowVar(flowKey, flowInstId, null);
			Optional<FlowVariable> flowVariable = variables.stream().filter(f -> key.equals(f.getVarKey())).findAny();
			FlowVariable exist = flowVariable.orElseThrow(() -> new ValidationException("流程条件不存在"+ errMsg +"取值来源【" + key + "】变量"));
			return exist.getVarVal();
		} else if (keyVal.contains(KeyValFromEnum.FORM.getFrom()) || keyVal.contains(KeyValFromEnum.ORDER.getFrom())) {
			return this.getValByOrderInfo(keyVal, flowKey, flowInstId, errMsg, null);
		} else if (keyVal.contains(KeyValFromEnum.USER.getFrom())) {
			DynaBean bean = DynaBean.create(SecurityUtils.getUser());
			String key = keyVal.replaceAll(KeyValFromEnum.USER.getFrom(), StrUtil.EMPTY);
			key = AuthorizationHeaderUtils.validateAuthorization(key);
			if(bean.containsProp(key)) {
				return bean.get(key);
			} else throw new ValidationException("当前登录用户信息不存在"+ errMsg +"取值来源【" + key + "】变量");
		} else {
			return this.getValByOrderInfo(keyVal, flowKey, flowInstId, errMsg, null);
		}
	}

	@Override
	public Object getValByOrderInfo(String keyVal, String flowKey, Long flowInstId, String errMsg, Map<String, Object> resMap) {
		String subKey = null;
		String propKey = this.getOrderPropKey(keyVal);
		DynaBean orderVo;
		if (Objects.isNull(resMap)) {
			boolean isOrder = keyVal.contains(KeyValFromEnum.ORDER.getFrom());
			orderVo = OrderInfoContextHolder.initOrderFormInfo(flowKey, flowInstId, null, isOrder);
		}
		else orderVo = DynaBean.create(resMap);
		if (propKey.contains(StrUtil.DOT)) {
			String[] keys = propKey.split(KeyValFromEnum.DOT.getFrom());
			propKey = keys[0];
			subKey = keys[1];
		}
		if(orderVo.containsProp(propKey)) {
			if (StrUtil.isNotBlank(subKey)) {
				return this.listSubKeyVals(orderVo.get(propKey), subKey);
			} else {
				return orderVo.get(propKey);
			}
		} else {
			throw new ValidationException("表单信息不存在"+ errMsg +"取值来源【" + propKey + "】字段");
		}
	}

	@Override
	public String getOrderPropKey(String keyVal) {
		String key;
		if (keyVal.contains(KeyValFromEnum.ORDER.getFrom())) {
			key = keyVal.replaceAll(KeyValFromEnum.ORDER.getFrom(), StrUtil.EMPTY);
		} else if (keyVal.contains(KeyValFromEnum.FORM.getFrom())) {
			key = keyVal.replaceAll(KeyValFromEnum.FORM.getFrom(), StrUtil.EMPTY);
		} else {
			key = keyVal.replaceAll(KeyValFromEnum.NUMBER_SIGN.getFrom(), StrUtil.EMPTY);
		}
		return key;
	}

	private Object listSubKeyVals(Object obj, String subKey) {
		// 值为空，则默认null
		if (ObjectUtil.isEmpty(obj)) return null;
		List<Object> subKeyVals = new ArrayList<>();
		JSONArray varValFrom = JSONUtil.parseArray(obj);
		varValFrom.forEach(each -> {
			JSONObject entries = JSONUtil.parseObj(each);
			Object res = entries.get(subKey);
			if (ObjectUtil.isNotEmpty(res)) {
				subKeyVals.add(res);
			}
		});
		return subKeyVals;
	}

	@Override
	public boolean invokeMethodByParamTypes(String varKeyVal, Long flowInstId, String flowKey) {
		Object res;
		boolean b = varKeyVal.contains(KeyValFromEnum.LEFT_METHOD.getFrom()) && varKeyVal.contains(KeyValFromEnum.RIGHT_METHOD.getFrom());
		if (b) {
			res = ClassMethodInvokeUtils.invokeMethodByParamTypes(varKeyVal, flowKey, flowInstId, "流程条件");
		} else {
			res = this.handleInvokeKeyValFrom(varKeyVal, flowKey, flowInstId, "流程条件");
		}
		String fromVal = Objects.toString(res, null);
		return FlowCommonConstants.YES.equals(fromVal);
	}

	public boolean evalSpELVarValue(Long flowInstId, String flowKey, String varKeyVal) {
		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();

		if (varKeyVal.contains(KeyValFromEnum.VAR.getFrom())) {
			varKeyVal = varKeyVal.replaceAll(KeyValFromEnum.VAR.getFrom(), KeyValFromEnum.NUMBER_SIGN.getFrom());
			List<FlowVariable> variables = FlowVarContextHolder.initFlowVar(flowKey, flowInstId, null);
			variables.forEach(each -> {
				String eachVarVal = each.getVarVal();
				if (NumberUtil.isNumber(eachVarVal)) context.setVariable(each.getVarKey(), NumberUtil.parseNumber(eachVarVal));
				else context.setVariable(each.getVarKey(), eachVarVal);
			});
		} else {
			DynaBean bean;
			if (varKeyVal.contains(KeyValFromEnum.FLOW.getFrom())) {
				varKeyVal = varKeyVal.replaceAll(KeyValFromEnum.FLOW.getFrom(), KeyValFromEnum.NUMBER_SIGN.getFrom());
				RunFlow runFlow = RunFlowContextHolder.initRunFlow(flowKey, flowInstId, null);
				bean = DynaBean.create(runFlow);
			} else if (varKeyVal.contains(KeyValFromEnum.USER.getFrom())) {
				varKeyVal = varKeyVal.replaceAll(KeyValFromEnum.USER.getFrom(), KeyValFromEnum.NUMBER_SIGN.getFrom());
				bean = DynaBean.create(SecurityUtils.getUser());
			} else {
				boolean isOrder = varKeyVal.contains(KeyValFromEnum.ORDER.getFrom());
				if (isOrder) {
					varKeyVal = varKeyVal.replaceAll(KeyValFromEnum.ORDER.getFrom(), KeyValFromEnum.NUMBER_SIGN.getFrom());
				} else {
					varKeyVal = varKeyVal.replaceAll(KeyValFromEnum.FORM.getFrom(), KeyValFromEnum.NUMBER_SIGN.getFrom());
				}
				bean = OrderInfoContextHolder.initOrderFormInfo(flowKey, flowInstId, null, isOrder);
			}
			Map<String, Object> order = bean.getBean();
			order.forEach((key ,val) -> {
				if (ObjectUtil.isEmpty(val)) return;
				if (NumberUtil.isNumber(val.toString())) context.setVariable(key, NumberUtil.parseNumber(val.toString()));
				else context.setVariable(key, val.toString());
			});
		}
		Boolean value;
		try {
			Expression expression = spelExpressionParser.parseExpression(varKeyVal);
			value = expression.getValue(context, Boolean.class);
		}
		catch (Exception e) {
			log.error("流程条件 解析SpEL {} 异常: {}", varKeyVal, e);
			throw new ValidationException("流程条件 解析SpEL " + varKeyVal + " 异常", e);
		}

		return Boolean.TRUE.equals(value);
	}

	/**
	 * 处理任务自身条件
	 *
	 * @param runJobs 运行任务集合
	 */
	@Override
	public List<RunJob> handleFlowNodeJobVariable(List<RunJob> runJobs) {
		List<FlowNodeRel> flowNodeRels = flowNodeRelService.listNodeJobRels(runJobs);
		flowNodeRels = flowNodeRels.stream().filter(f -> StrUtil.isNotBlank(f.getVarKeyVal())).collect(Collectors.toList());
		if (CollUtil.isEmpty(flowNodeRels)) return runJobs;
		Long flowInstId = runJobs.get(0).getFlowInstId();
		// 不符合条件的节点默认不开始
		List<FlowNodeRel> finalFlowNodeRels = flowNodeRels;
		List<RunJob> noMatchConJobs = runJobs.stream().filter(runJob -> {
			List<FlowNodeRel> conditions = finalFlowNodeRels.stream().filter(f -> f.getToNodeJobId().equals(runJob.getNodeJobId())).collect(Collectors.toList());
			return doHandleFlowVariable(conditions, flowInstId);
		}).collect(Collectors.toList());
		// 去除不符合条件的节点
		if (CollUtil.isNotEmpty(noMatchConJobs)) {
			// 将状态设置为跳过
			this.handleSkipJobs(noMatchConJobs, NodeJobStatusEnum.SKIP.getStatus());
			runJobs = runJobs.stream().filter(f -> noMatchConJobs.stream().noneMatch(any -> f.getId().equals(any.getId()))).collect(Collectors.toList());
		}
		return runJobs;
	}

	/**
	 * 更新不符合条件任务状态
	 *
	 * @param runJobs 运行任务集合
	 * @param status  状态
	 */
	private void handleSkipJobs(List<RunJob> runJobs, String status) {
		List<Long> nextIds = runJobs.stream().map(RunJob::getId).collect(Collectors.toList());
		runJobService.lambdaUpdate().set(RunJob::getStatus, status)
				.eq(RunJob::getStatus, NodeJobStatusEnum.NO_START.getStatus())
				.in(RunJob::getId, nextIds)
				.update();
	}

	@Override
	public List<FlowNodeRel> handleFlowNodeRelSerialParallelVariable(List<FlowNodeRel> flowNodeRels, Long flowInstId) {
		// 不能判断来源节点类型因可能存在父子关系
		boolean start = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.START.getType().equals(any.getToNodeType()));
		boolean serial = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.SERIAL.getType().equals(any.getToNodeType()));
		boolean parallel = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.PARALLEL.getType().equals(any.getToNodeType()));
		boolean end = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.END.getType().equals(any.getToNodeType()));
		if ((end && parallel) || (parallel && serial) || (end && serial)  || (end && start)|| (start && parallel) || (start && serial)) {
			flowNodeRels = this.handleFlowNodeRelVariable(flowNodeRels, flowInstId);
			if (CollUtil.isEmpty(flowNodeRels)) throw new ValidationException("下一节点同时存在并行或串行节点时，条件筛选后下一节点为空");
			// 再次判断是否还同时存在，是则异常
			start = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.START.getType().equals(any.getToNodeType()));
			serial = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.SERIAL.getType().equals(any.getToNodeType()));
			parallel = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.PARALLEL.getType().equals(any.getToNodeType()));
			end = flowNodeRels.stream().anyMatch(any -> NodeTypeEnum.END.getType().equals(any.getToNodeType()));
			if ((end && parallel) || (parallel && serial) || (end && serial)  || (end && start)|| (start && parallel) || (start && serial)) {
				throw new ValidationException("下一节点同时存在开始节点或串行节点或并行节点或结束节点时，条件筛选后依然同时存在");
			}
			if ((start || serial || end) && flowNodeRels.size() > 1) throw new ValidationException("下一节点同时存在开始节点或串行节点或结束节点，条件筛选后串行节点不唯一");
		}
		if (CollUtil.isEmpty(flowNodeRels)) throw new ValidationException("下一节点同时存在开始节点或串行节点或并行节点或结束节点时，条件筛选后流程不存在可用节点");
		return flowNodeRels;
	}

}
