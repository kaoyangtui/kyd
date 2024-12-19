package com.pig4cloud.pigx.order.service.impl;
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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.order.OrderStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.order.api.constant.FlowVariableConstants;
import com.pig4cloud.pigx.order.api.constant.OrderCommonConstants;
import com.pig4cloud.pigx.order.api.entity.AskLeave;
import com.pig4cloud.pigx.order.api.vo.AskLeaveVO;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import com.pig4cloud.pigx.order.mapper.AskLeaveMapper;
import com.pig4cloud.pigx.order.service.AskLeaveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 请假工单
 *
 * @author luolin
 * @date 2020-08-25 10:55:19
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AskLeaveServiceImpl extends OrderCommonServiceImpl<AskLeaveMapper, AskLeave> implements AskLeaveService {

	@Override
	public IPage<AskLeave> getPage(Page<AskLeave> page, AskLeave askLeave, String[] queryTime) {
		QueryWrapper<AskLeave> query = Wrappers.query(askLeave);
		QueryWrapperUtils.userQueryTime(query, AskLeave::getCreateTime, queryTime
				, AskLeave::getCreateUser, SecurityUtils.getUser().getId());

		return this.page(page, query);
	}

	@Override
	// @GlobalTransactional
	public <Vo> Vo startSubFlow(Vo order) {
		AskLeaveVO askLeaveVO = (AskLeaveVO) order;
		Map<String, Object> flowVars = this.doSaveOrUpdate(askLeaveVO);
		askLeaveVO.setFlowVarUser(flowVars);
		return (Vo) askLeaveVO;
	}

	@Override
	// @GlobalTransactional
	public <Vo> Boolean backParFlow(Vo order) {
		AskLeaveVO askLeaveVO = (AskLeaveVO) order;
		askLeaveVO.setUpdateTime(LocalDateTime.now());
		askLeaveVO.setUpdateUser(SecurityUtils.getUser().getId());

		AskLeave askLeave = new AskLeave();
		BeanUtil.copyProperties(askLeaveVO, askLeave);
		return this.update(askLeave, Wrappers.<AskLeave>lambdaUpdate().eq(AskLeave::getFlowInstId, askLeave.getFlowInstId()));
	}

	@Override
	public <Vo> Boolean restartSubFlow(Vo order) {
		AskLeaveVO askLeaveVO = (AskLeaveVO) order;
		askLeaveVO.setStatus(OrderStatusEnum.RUN.getStatus());
		this.backParFlow(order);
		return true;
	}

	@Override
	// @GlobalTransactional
	public Boolean saveOrUpdate(AskLeaveVO askLeaveVO) {
		Map<String, Object> params = this.doSaveOrUpdate(askLeaveVO);
		// 启动流程公共接口
		this.startFlow(params, askLeaveVO, null);
		return Boolean.TRUE;
	}

	private Map<String, Object> doSaveOrUpdate(AskLeaveVO askLeaveVO) {
		askLeaveVO.setCreateTime(LocalDateTime.now());
		askLeaveVO.setCreateUser(SecurityUtils.getUser().getId());
		// 对象转化
		AskLeave askLeave = new AskLeave();
		BeanUtil.copyProperties(askLeaveVO, askLeave);
		// 设置流程参数
		Map<String, Object> params = MapUtil.newHashMap();
		this.buildFlowVariables(params, askLeaveVO);
		// 发起流程并保存工单
		super.saveOrUpdateOrder(params, askLeave);
		super.updateOrderVOInfo(askLeave, askLeaveVO);
		return params;
	}

	/**
	 * 构建流程参数
	 *
	 * @param params 参数
	 * @param askLeaveVO 请假工单
	 */
	private void buildFlowVariables(Map<String, Object> params, AskLeaveVO askLeaveVO) {
		// 设置请假天数条件，目前无用（流程条件配置的#days）
		params.put(FlowVariableConstants.LEAVE_DAYS, askLeaveVO.getDays());
		// 测试分配多个参与者（目前无用仅测试），如单个用户与多个角色（岗位 部门类似）
		List<DistPerson> distPersons = this.buildDistPerson();
		// 注：流程参数params除了FLOW_USERS外，其他都是流程条件参数
		// 注：此处的FLOW_USERS的值支持传入单个或多个DistPerson
		params.put(FlowCommonConstants.FLOW_USERS, JSONUtil.toJsonStr(distPersons));
		// 构建流程公共参数
		super.buildFlowVariables(params, askLeaveVO);
	}

	// TODO 如果流程设计配置HTTP等方式分配参与者，返回值与此处类似
	private List<DistPerson> buildDistPerson() {
		List<DistPerson> distPersons = new ArrayList<>();
		DistPerson distPersonUser = new DistPerson();
		distPersonUser.setRoleId(SecurityUtils.getUser().getId());
		distPersonUser.setJobType(JobUserTypeEnum.USER.getType());
		distPersonUser.setUserKey(FlowVariableConstants.TEST_ROLE_USER);
		// 可单独指定任务的名称，非必填（默认为流程设计的名称）
		distPersonUser.setJobName("测试分配单个用户");
		// 如果是顺序签任务，可以设置任务排序值
		distPersonUser.setSort(0);
		distPersons.add(distPersonUser);
		List<Long> roleIds = SecurityUtils.getRoleIds();
		for (int i = 0; i < roleIds.size(); i++) {
			DistPerson distPerson = new DistPerson();
			distPerson.setRoleId(roleIds.get(i));
			distPerson.setJobType(JobUserTypeEnum.ROLE.getType());
			// 注：可以是相同的userKey，即相同的userKey存在用户、角色、岗位、部门等参与者
			distPerson.setUserKey(FlowVariableConstants.TEST_ROLE_USER);
			distPerson.setJobName("测试分配多个角色");
			distPersonUser.setSort(i + 1);
			distPersons.add(distPerson);
		}
		return distPersons;
	}

	@Override
	// @GlobalTransactional
	public Boolean tempStore(AskLeaveVO askLeaveVO) {
		askLeaveVO.setCreateTime(LocalDateTime.now());
		askLeaveVO.setCreateUser(SecurityUtils.getUser().getId());
		AskLeave askLeave = new AskLeave();
		BeanUtil.copyProperties(askLeaveVO, askLeave);
		return super.tempStore(askLeave);
	}

	@Override
	// @GlobalTransactional
	public Boolean updateOrder(AskLeaveVO askLeaveVO) {
		askLeaveVO.setUpdateTime(LocalDateTime.now());
		askLeaveVO.setUpdateUser(SecurityUtils.getUser().getId());

		AskLeave askLeave = new AskLeave();
		BeanUtil.copyProperties(askLeaveVO, askLeave);
		boolean save = this.updateById(askLeave);
		// TODO 自己决定更新时是否更新流程参数
		String runJobId = askLeaveVO.getRunJobId();
		if (StrUtil.isEmpty(runJobId)) return true;
		Map<String, Object> params = MapUtil.of(OrderCommonConstants.RUN_JOB_ID, runJobId);
		this.buildFlowVariables(params, askLeaveVO);
		super.updateFlowVariables(params, askLeaveVO);
		return save;
	}

	@Override
	public Boolean removeOrder(Long id) {
		return this.removeById(id);
	}

}
