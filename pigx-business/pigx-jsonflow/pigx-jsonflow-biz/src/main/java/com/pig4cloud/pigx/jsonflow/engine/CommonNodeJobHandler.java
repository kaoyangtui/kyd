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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.KeyValFromEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.config.FlowCommonProperties;
import com.pig4cloud.pigx.jsonflow.service.DistPersonService;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import com.pig4cloud.pigx.jsonflow.util.ClassMethodInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.CommonHttpUrlInvokeUtils;
import com.pig4cloud.pigx.jsonflow.util.SimpMsgTempUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 公共任务监听处理类
 *
 * @author luolin
 * @date 2020/2/7
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommonNodeJobHandler implements NodeJobHandler {

	@Autowired
	private IAuditorService auditorService;
	@Autowired
	private DistPersonService distPersonService;
	@Lazy
	@Autowired
	private JsonFlowEngineService jsonFlowEngineService;
	@Autowired
	private FlowVariableService flowVariableService;
	@Autowired
	private FlowCommonProperties flowCommonProperties;
	@Autowired
	private FlowRuleService flowRuleService;

	/**
	 * 设置下一节点参与者
	 *
	 * @param runJobVO 运行任务
	 * @param runJobs 运行任务集合
	 */
	@Override
	public void setNextUsers(RunJobVO runJobVO, List<RunJob> runJobs) {
		// 前端指定下一步参与者
		DistPerson nextUserRole = runJobVO.getNextUserRole();
		if (Objects.nonNull(nextUserRole)) {
			runJobs.forEach(f -> {
				f.setRoleId(nextUserRole.getRoleId());
				f.setJobType(nextUserRole.getJobType());
			});
			return;
		}
		List<RunJob> calculateRunJobs = CollUtil.newArrayList();
		List<Long> calRunNodeJobIds = CollUtil.newArrayList();
		List<DistPerson> distPersons = distPersonService.list(Wrappers.<DistPerson>lambdaQuery().eq(DistPerson::getFlowInstId, runJobVO.getFlowInstId()));
		runJobs.forEach(each -> {
			Long roleId = each.getRoleId();
			String userKey = each.getUserKey();
			String userKeyVal = each.getUserKeyVal();
			String valType = each.getValType();
			// 判断是否已计算
			if (calRunNodeJobIds.contains(each.getId())) return;
			List<RunJob> currRunNodeJobs = runJobs.stream().filter(f -> f.getRunNodeId().equals(each.getRunNodeId())
					&& f.getValType().equals(each.getValType())).collect(Collectors.toList());
			List<RunJob> resRunJobs = new ArrayList<>();
			if (StrUtil.isBlank(valType) || DistValTypeEnum.ORDINARY.getType().equals(valType)) {
				// TODO 可自行分配的任务类型
			} else if (DistValTypeEnum.DIST.getType().equals(valType)) {
				List<DistPerson> userKeyDistPersons = distPersons.stream().filter(f -> userKey.equals(f.getUserKey())).collect(Collectors.toList());
				if (CollUtil.isNotEmpty(userKeyDistPersons)) {
					resRunJobs = this.setNextAllRunJobsByDistPerson(calRunNodeJobIds, each, currRunNodeJobs, userKeyDistPersons);
				} else {
					if (Objects.isNull(roleId)) this.validationException("下一步开始的任务不存在分配的参与者");
				}
			} else if (DistValTypeEnum.SIMPLE.getType().equals(valType)) {
				resRunJobs = this.handleCondGroupsCondition(each, currRunNodeJobs, calRunNodeJobIds);
			} else if (DistValTypeEnum.SPEL_FIXED.getType().equals(valType)) {
				resRunJobs = this.handleFixedCondition(each, currRunNodeJobs, calRunNodeJobIds);
			} else if (DistValTypeEnum.COMPLEX.getType().equals(valType)) {
				boolean b = userKeyVal.contains(KeyValFromEnum.LEFT_METHOD.getFrom()) && userKeyVal.contains(KeyValFromEnum.RIGHT_METHOD.getFrom());
				if (b) {
					resRunJobs = this.invokeMethodByParamTypes(currRunNodeJobs, each, calRunNodeJobIds);
				} else {
					// 参与者变量值规则：参与者类型前缀（为空默认为用户ID）_参与者（必填）_任务名称（可选）_任务排序值（可选），如ROLE_2
					resRunJobs = this.handleUserKeyValFrom(currRunNodeJobs, each, calRunNodeJobIds);
				}
			} else if (DistValTypeEnum.HTTP.getType().equals(valType)) {
				resRunJobs = this.handleHttpInvoke(currRunNodeJobs, each, calRunNodeJobIds);
			}
			if (CollUtil.isNotEmpty(resRunJobs)) calculateRunJobs.addAll(resRunJobs);
		});
		if (CollUtil.isNotEmpty(calculateRunJobs)) {
			// 处理再次重复计算
			runJobs.removeIf(f -> calculateRunJobs.stream().anyMatch(any -> any.getId().equals(f.getId())));
			runJobs.addAll(calculateRunJobs);
		}
		// 节点任务必须存在参与者
		List<RunJob> isNull = runJobs.stream().filter(f -> !f.getStatus().equals(NodeJobStatusEnum.SKIP.getStatus())
				&& Objects.isNull(f.getRoleId())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(isNull)) this.validationException("下一步开始的任务不存在分配的参与者");
	}

	private List<RunJob> setNextAllRunJobsByDistPerson(List<Long> calRunNodeJobIds, RunJob each, List<RunJob> currRunNodeJobs, List<DistPerson> distPersons) {
		boolean allMatch = distPersons.stream().allMatch(all -> currRunNodeJobs.stream().anyMatch(any -> all.getRoleId().equals(any.getRoleId())
				&& all.getJobType().equals(any.getJobType())));
		List<RunJob> resRunJobs = currRunNodeJobs;
		if (!allMatch) {
			// 根据已分配参与者信息计算任务数
			resRunJobs = jsonFlowEngineService.setNextAllRunJobsByDistPerson(each, distPersons);
		}
		resRunJobs.forEach(f -> calRunNodeJobIds.add(f.getId()));
		return resRunJobs;
	}

	private List<RunJob> handleHttpInvoke(List<RunJob> currRunNodeJobs, RunJob each, List<Long> calRunNodeJobIds) {
		// 处理Http调用请求
		String httpUrl = each.getUserKeyVal();
		if (StrUtil.isBlank(httpUrl)) return Collections.emptyList();
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(each.getDefFlowId());
		flowRuleVO.setFlowKey(each.getFlowKey());
		flowRuleVO.setFlowInstId(each.getFlowInstId());
		flowRuleVO.setFlowNodeId(each.getFlowNodeId());
		flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
		flowRuleVO.setValType(DistValTypeEnum.HTTP.getType());
		flowRuleVO.setHttpUrl(httpUrl);
		flowRuleVO.setHttpMethod(each.getHttpMethod());
		Object obj = CommonHttpUrlInvokeUtils.handleHttpInvoke(flowRuleVO);
		return this.validateListFrom(currRunNodeJobs, each, obj, calRunNodeJobIds);
	}

	private List<RunJob> handleFixedCondition(RunJob each, List<RunJob> currRunNodeJobs, List<Long> calRunNodeJobIds) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(each.getDefFlowId());
		flowRuleVO.setFlowNodeId(each.getFlowNodeId());
		flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
		flowRuleVO.setValType(DistValTypeEnum.SPEL_FIXED.getType());
		List<FlowRule> flowRules = flowRuleService.listFlowRules(flowRuleVO);
		// 条件规则为空则不处理
		if (CollUtil.isEmpty(flowRules)) return Collections.emptyList();
		List<DistPerson> distPersons = new ArrayList<>();
		flowRules.forEach(flowRule -> {
			DistPerson distPerson = new DistPerson();
			distPerson.setRoleId(flowRule.getRoleId());
			distPerson.setJobType(flowRule.getJobType());
			distPersons.add(distPerson);
		});
		return this.setNextAllRunJobsByDistPerson(calRunNodeJobIds, each, currRunNodeJobs, distPersons);
	}

	private List<RunJob> handleCondGroupsCondition(RunJob each, List<RunJob> currRunNodeJobs, List<Long> calRunNodeJobIds) {
		FlowRuleVO flowRuleVO = new FlowRuleVO();
		flowRuleVO.setDefFlowId(each.getDefFlowId());
		flowRuleVO.setFlowNodeId(each.getFlowNodeId());
		flowRuleVO.setType(FlowParamRuleEnum.PERSON.getType());
		flowRuleVO.setValType(DistValTypeEnum.SIMPLE.getType());
		List<FlowRule> flowRules = flowRuleService.listFlowRules(flowRuleVO);
		// 条件规则为空则不处理
		if (CollUtil.isEmpty(flowRules)) return Collections.emptyList();
		List<DistPerson> distPersons = new ArrayList<>();
		// 处理不同组条件
		Iterator<Map.Entry<Long, List<FlowRule>>> iterator = flowVariableService.getEntryIterator(flowRules);
		String groupsType = flowRules.get(0).getGroupsType();
		if (CommonNbrPool.STR_1.equals(groupsType)) {
			while (iterator.hasNext()) {
				boolean groups = this.isGroup(each, iterator);
				if (groups) {
					List<FlowRule> conGroup = iterator.next().getValue();
					DistPerson distPerson = new DistPerson();
					distPerson.setRoleId(conGroup.get(0).getRoleId());
					distPerson.setJobType(conGroup.get(0).getJobType());
					distPersons.add(distPerson);
				}
			}
		}
		// 条件规则都不满足则默认为空
		if (CollUtil.isEmpty(distPersons)) {
			return this.validateCurrRunNodeJobs(currRunNodeJobs, calRunNodeJobIds);
		}
		return this.setNextAllRunJobsByDistPerson(calRunNodeJobIds, each, currRunNodeJobs, distPersons);
	}

	private boolean isGroup(RunJob each, Iterator<Map.Entry<Long, List<FlowRule>>> iterator) {
		boolean group = true;
		List<FlowRule> conGroup = iterator.next().getValue();
		String groupType = conGroup.get(0).getGroupType();
		if (CommonNbrPool.STR_1.equals(groupType)) {
			group = false;
		}
		// 处理组内各个条件
		for (FlowRule condition : conGroup) {
			boolean b = this.handleCondGroupCondition(condition, each.getFlowKey(), each.getFlowInstId(), condition.getVarKeyVal(), "动态参与者");
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

	@Override
	public boolean handleCondGroupCondition(FlowRule each, String flowKey, Long flowInstId, String varKeyVal, String errMsg){
		Object obj = null;
		if (KeyValFromEnum.getVarOrderUser().stream().anyMatch(varKeyVal::contains)) {
			obj = flowVariableService.handleInvokeKeyValFrom(varKeyVal, flowKey, flowInstId, errMsg);
		}
		return flowVariableService.doConditionCompareTo(each, obj);
	}

	/**
	 * 处理KEY取值来源
	 * @param each 运行任务
	 */
	private List<RunJob> handleUserKeyValFrom(List<RunJob> currRunNodeJobs, RunJob each, List<Long> calRunNodeJobIds) {
		Object obj = flowVariableService.handleInvokeKeyValFrom(each.getUserKeyVal(), each.getFlowKey(), each.getFlowInstId(), "动态参与者");
		return this.validateListFrom(currRunNodeJobs, each, obj, calRunNodeJobIds);
	}

	private List<RunJob> validateListFrom(List<RunJob> currRunNodeJobs, RunJob each, Object obj, List<Long> calRunNodeJobIds) {
		if (ObjectUtil.isEmpty(obj)) {
			return this.validateCurrRunNodeJobs(currRunNodeJobs, calRunNodeJobIds);
		}
		// 判断是否为数组
		if(obj instanceof List) {
			return this.validateListValue(currRunNodeJobs, each, (List<?>) obj, calRunNodeJobIds);
		} else {
			// 单个也需动态计算，去掉多余任务
			return this.validateListValue(currRunNodeJobs, each, CollUtil.newArrayList(obj), calRunNodeJobIds);
		}
	}

	private List<RunJob> validateCurrRunNodeJobs(List<RunJob> currRunNodeJobs, List<Long> calRunNodeJobIds) {
		this.validationException("下一步开始的任务未分配参与者");
		// 为空则将现有任务置空
		currRunNodeJobs.forEach(currRunNodeJob -> {
			calRunNodeJobIds.add(currRunNodeJob.getId());
			currRunNodeJob.setRoleId(null);
			currRunNodeJob.setJobType(JobUserTypeEnum.NONE.getType());
		});
		return currRunNodeJobs;
	}

	/**
	 * 处理取值来源参数
	 * @param currRunNodeJobs 当前运行任务集合
	 * @param each 当前运行任务
	 * @return List
	 */
	private List<RunJob> invokeMethodByParamTypes(List<RunJob> currRunNodeJobs, RunJob each, List<Long> calRunNodeJobIds) {
		Object res = ClassMethodInvokeUtils.invokeMethodByParamTypes(each.getUserKeyVal(), each.getFlowKey(), each.getFlowInstId(), "动态参与者");
		return this.validateListFrom(currRunNodeJobs, each, res, calRunNodeJobIds);
	}

	private List<RunJob> validateListValue(List<RunJob> currRunNodeJobs, RunJob each, List<?> ids, List<Long> calRunNodeJobIds) {
		List<DistPerson> distPersons = new ArrayList<>();
		for (Object obj : ids) {
			DistPerson distPerson = new DistPerson();
			String jobType = JobUserTypeEnum.USER.getType();
			if (obj instanceof String) {
				String objStr = obj.toString();
				String id, jobName = null, sort = "", prefix = "";
				String[] values;
				if (StrUtil.contains(objStr, JobUserTypeEnum.USER_PREFIX)) {
					prefix = JobUserTypeEnum.USER_PREFIX;
					jobType = JobUserTypeEnum.USER.getType();
				} else if (StrUtil.contains(objStr, JobUserTypeEnum.ROLE_PREFIX)) {
					prefix = JobUserTypeEnum.ROLE_PREFIX;
					jobType = JobUserTypeEnum.ROLE.getType();
				} else if (StrUtil.contains(objStr, JobUserTypeEnum.POST_PREFIX)) {
					prefix = JobUserTypeEnum.POST_PREFIX;
					jobType = JobUserTypeEnum.POST.getType();
				} else if (StrUtil.contains(objStr, JobUserTypeEnum.DEPT_PREFIX)) {
					prefix = JobUserTypeEnum.DEPT_PREFIX;
					jobType = JobUserTypeEnum.DEPT.getType();
				}
				objStr = objStr.replace(prefix, StrUtil.EMPTY);
				values = objStr.split(StrUtil.UNDERLINE);
				id = values[0];
				if (values.length > 1) jobName = values[1];
				if (values.length > 2) sort = values[2];
				distPerson.setRoleId(Long.valueOf(id));
				distPerson.setJobType(jobType);
				if (StrUtil.isNotBlank(jobName)) distPerson.setJobName(jobName);
				if (StrUtil.isNotBlank(sort)) distPerson.setSort(Integer.valueOf(sort));
			} else if (obj instanceof Long){
				distPerson.setRoleId((Long) obj);
				distPerson.setJobType(jobType);
			} else {
				distPerson = BeanUtil.toBean(obj, DistPerson.class);
			}
			distPersons.add(distPerson);
		}
		return this.setNextAllRunJobsByDistPerson(calRunNodeJobIds, each, currRunNodeJobs, distPersons);
	}

	@Override
	public void notify(RunJobVO runJobVO, List<RunJob> runJobs) {
		// 前端指定下一步参与者
		DistPerson nextUserRole = runJobVO.getNextUserRole();
		if (Objects.isNull(nextUserRole)) {
			this.handleNotifyUserRole(runJobVO, runJobs);
		} else {
			this.nodeNotifySingleUser(runJobVO, runJobs);
		}
	}

	private void validationException(String errMsg){
		if (FlowCommonConstants.IS_ENABLED.equals(flowCommonProperties.getIsAllowNoRoleUser())) {
			return;
		}
		throw new ValidationException(errMsg);
	}

	private List<RunJob> validationException(List<RunJob> runJobs){
		if (FlowCommonConstants.IS_ENABLED.equals(flowCommonProperties.getIsAllowNoRoleUser())) {
			return runJobs.stream().filter(f -> Objects.nonNull(f.getRoleId())).collect(Collectors.toList());
		}
		if (runJobs.stream().anyMatch(f -> Objects.isNull(f.getRoleId()))) {
			throw new ValidationException("下一步任务参与者角色不能为空");
		}
		return runJobs;
	}

	/**
	 * 通知参与者用户
	 */
	private void handleNotifyUserRole(RunJobVO runJobVO, List<RunJob> runJobs) {
		runJobs = this.validationException(runJobs);
		if (CollUtil.isEmpty(runJobs)) return;
		List<RunJob> roleRunJobs = runJobs.stream().filter(f -> !JobUserTypeEnum.USER.getType().equals(f.getJobType()) && Objects.nonNull(f.getRoleId())).collect(Collectors.toList());
		// 根据参与者角色ID查询用户
		List<UserVO> roleUsers = CollUtil.newArrayList();
		List<UserVO> postUsers = CollUtil.newArrayList();
		List<UserVO> deptUsers = CollUtil.newArrayList();
		if (CollUtil.isNotEmpty(roleRunJobs)) {
			Map<String, List<RunJob>> roleRunJobsMap = roleRunJobs.stream().collect(Collectors.groupingBy(RunJob::getJobType));
			roleRunJobsMap.forEach((key, val) -> {
				List<Long> roleIds = val.stream().map(RunJob::getRoleId).collect(Collectors.toList());
				List<UserVO> userVOS = auditorService.listUsersByRoleIds(roleIds, key);
				if (JobUserTypeEnum.ROLE.getType().equals(key)) {
					roleUsers.addAll(userVOS);
				} else if (JobUserTypeEnum.POST.getType().equals(key)) {
					postUsers.addAll(userVOS);
				} else if (JobUserTypeEnum.DEPT.getType().equals(key)) {
					deptUsers.addAll(userVOS);
				}
			});
		}
		List<SysUser> users = new ArrayList<>();
		List<RunJob> userRunJobs = runJobs.stream().filter(f -> JobUserTypeEnum.USER.getType().equals(f.getJobType()) && Objects.nonNull(f.getRoleId())).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(userRunJobs)) {
			List<Long> userIds = userRunJobs.stream().map(RunJob::getRoleId).collect(Collectors.toList());
			users = auditorService.listUsersByUserIds(userIds);
		}
		this.doNotifyUserRole(runJobVO, userRunJobs, users, roleRunJobs, roleUsers, postUsers, deptUsers);
	}

	private void doNotifyUserRole(RunJobVO runJobVO, List<RunJob> userRunJobs, List<SysUser> users, List<RunJob> roleRunJobs, List<UserVO> roleUsers, List<UserVO> postUsers, List<UserVO> deptUsers) {
		// 根据参与者用户分组
		Map<Long, List<RunJob>> userMap = userRunJobs.stream().collect(Collectors.groupingBy(RunJob::getRoleId));
		userMap.forEach((key, val) -> {
			List<String> jobNames = val.stream().map(RunJob::getJobName).collect(Collectors.toList());
			String nodeName = CollUtil.join(jobNames, StrUtil.COMMA);
			SysUser sysUser = users.stream().filter(f -> f.getUserId().equals(key)).findAny()
					.orElseThrow(() -> new ValidationException("下一步审批用户获取失败"));
			this.nodeNotifyUsers(runJobVO, CollUtil.newArrayList(sysUser), nodeName, true);
		});
		// 根据参与者角色分组
		Map<String, List<RunJob>> roleMap = roleRunJobs.stream().collect(Collectors.groupingBy(g -> g.getJobType() + StrUtil.UNDERLINE + g.getRoleId()));
		roleMap.forEach((key, val) -> {
			List<String> jobNames = val.stream().map(RunJob::getJobName).collect(Collectors.toList());
			String nodeName = CollUtil.join(jobNames, StrUtil.COMMA);
			String[] split = key.split(StrUtil.UNDERLINE);
			String jobType = split[0];
			Long roleId = Long.valueOf(split[1]);
			List<SysUser> ddUsers = null;
			if (JobUserTypeEnum.ROLE.getType().equals(jobType)) {
				ddUsers = roleUsers.stream().filter(f -> f.getRoleList().stream().anyMatch(any -> any.getRoleId().equals(roleId))).map(m -> {
					SysUser sysUser = new SysUser();
					BeanUtil.copyProperties(m, sysUser);
					return sysUser;
				}).collect(Collectors.toList());
			} else if (JobUserTypeEnum.POST.getType().equals(jobType)) {
				ddUsers = postUsers.stream().filter(f -> f.getPostList().stream().anyMatch(any -> any.getPostId().equals(roleId))).map(m -> {
					SysUser sysUser = new SysUser();
					BeanUtil.copyProperties(m, sysUser);
					return sysUser;
				}).collect(Collectors.toList());
			} else if (JobUserTypeEnum.DEPT.getType().equals(jobType)) {
				ddUsers = deptUsers.stream().filter(f -> f.getDeptId().equals(roleId)).map(m -> {
					SysUser sysUser = new SysUser();
					BeanUtil.copyProperties(m, sysUser);
					return sysUser;
				}).collect(Collectors.toList());
			}
			if (CollUtil.isEmpty(ddUsers)) throw new ValidationException("当前任务不存在指定的参与者用户，请核实");
			this.nodeNotifyUsers(runJobVO, ddUsers, nodeName, false);
		});
	}

	/**
	 * 通知一个参与者，多个任务时，逗号分隔
	 *
	 * @param runJobVO 运行任务
	 * @param runJobs  运行任务集合
	 */
	public void nodeNotifySingleUser(RunJobVO runJobVO, List<RunJob> runJobs) {
		List<String> jobNames = runJobs.stream().map(RunJob::getJobName).collect(Collectors.toList());
		String nodeName = CollUtil.join(jobNames, StrUtil.COMMA);
		// 前端指定单参与者角色
		DistPerson nextUserRole = runJobVO.getNextUserRole();
		if (Objects.nonNull(nextUserRole)) {
			String jobType = nextUserRole.getJobType();
			boolean isUser = JobUserTypeEnum.USER.getType().equals(jobType);
			List<SysUser> ddUsers = auditorService.listUsersByRoleId(nextUserRole.getRoleId(), jobType);
			this.nodeNotifyUsers(runJobVO, ddUsers, nodeName, isUser);
		}
	}

	/**
	 * 通知参与者用户
	 *
	 * @param runJobVO 运行任务
	 * @param ddUsers 钉钉用户
	 * @param nodeName 节点名称
	 * @param isUser 是否为用户任务
	 */
	private void nodeNotifyUsers(RunJobVO runJobVO, List<SysUser> ddUsers, String nodeName, boolean isUser) {
		if (FlowCommonConstants.YES.equals(runJobVO.getRunNodeVO().getIsPassSame())) {
			ddUsers.removeIf(f -> SecurityUtils.getUser().getId().equals(f.getUserId()));
		}
		if (CollUtil.isEmpty(ddUsers)) return;
		Set<Long> remindUsers = ddUsers.stream().map(SysUser::getUserId).collect(Collectors.toSet());
		// ws 钉钉通知参与者用户
		SimpMsgTempUtils.nodeNotifyUsers(runJobVO, ddUsers, nodeName, isUser);
		log.info("当前任务 {}, 由参与者用户 {} 处理", nodeName, CollUtil.join(remindUsers, StrUtil.COMMA));
	}

}
