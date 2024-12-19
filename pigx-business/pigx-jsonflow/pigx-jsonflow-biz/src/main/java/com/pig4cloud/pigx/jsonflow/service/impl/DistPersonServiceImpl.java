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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.ValidationExceptions;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.DistValTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.engine.JsonFlowEngineService;
import com.pig4cloud.pigx.jsonflow.mapper.DistPersonMapper;
import com.pig4cloud.pigx.jsonflow.service.DistPersonService;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import com.pig4cloud.pigx.jsonflow.util.CommonHttpUrlInvokeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分配参与者
 *
 * @author luolin
 * @date 2021-02-23 14:12:22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DistPersonServiceImpl extends ServiceImpl<DistPersonMapper, DistPerson> implements DistPersonService {

	@Lazy
	@Autowired
	private JsonFlowEngineService jsonFlowEngineService;

	@Override
	public boolean recordDistPersons(Map<String, Object> params) {
		// 动态分配
		List<DistPerson> distPersons = this.buildDistPersons(params);
		if (CollUtil.isEmpty(distPersons)) return true;
		// userKey标识节点任务
		this.validateDistPerson(distPersons);
		distPersons.forEach(baseMapper::insert);
		// baseMapper.insertBatchSomeColumn(distPersons);
		return true;
	}

	/**
	 * 获取分配参与者
	 *
	 * @param distPersons  分配参与者集合
	 */
	private void validateDistPerson(List<DistPerson> distPersons) {
		DistPerson distPerson = distPersons.get(0);
		if (StrUtil.isEmpty(distPerson.getJobType())) throw new ValidationException(ValidationExceptions.NO_JOB_TYPE);
		if (Objects.isNull(distPerson.getFlowInstId())) throw new ValidationException(ValidationExceptions.NO_FLOWINSTID);
		if (StrUtil.isEmpty(distPerson.getFlowKey())) throw new ValidationException(ValidationExceptions.NO_FLOW_KEY);
		if (StrUtil.isEmpty(distPerson.getCode())) throw new ValidationException(ValidationExceptions.NO_FLOW_CODE);
		if (StrUtil.isEmpty(distPerson.getUserKey())) throw new ValidationException(ValidationExceptions.NO_USER_KEY);
		boolean noUserRole = distPersons.stream().anyMatch(any -> Objects.isNull(any.getRoleId()));
		if (noUserRole) throw new ValidationException(ValidationExceptions.NO_ROLE_ID);
	}

	private void validateValType(DistPersonVO distPersonVO) {
		if (!DistValTypeEnum.DIST.getType().equals(distPersonVO.getValType())) {
			throw new ValidationException(ValidationExceptions.DIST_VAL_TYPE);
		}
	}

	@Override
	public boolean updateDistPersons(Map<String, Object> params) {
		List<DistPerson> distPersons = this.buildDistPersons(params);
		if (CollUtil.isEmpty(distPersons)) return true;
		DistPerson distPerson = distPersons.get(0);
		this.validateDistPerson(distPersons);
		List<String> keys = distPersons.stream().map(DistPerson::getUserKey).collect(Collectors.toList());
		this.removeDistPersons(distPerson.getFlowInstId(), keys);
		distPersons.forEach(baseMapper::insert);
		// baseMapper.insertBatchSomeColumn(distPersons);
		return true;
	}

	private void removeDistPersons(Long flowInstId, List<String> keys) {
		this.remove(Wrappers.<DistPerson>lambdaQuery().eq(DistPerson::getFlowInstId, flowInstId)
				// 不能删除别人分配的人员
				.eq(DistPerson::getCreateUser, SecurityUtils.getUser().getId())
				.in(DistPerson::getUserKey, keys));
	}

	/**
	 * 构建流程Map分配参与者
	 *
	 * @param params      参数
	 */
	private List<DistPerson> buildDistPersons(Map<String, Object> params) {
		Object flowUsers = params.get(FlowCommonConstants.FLOW_USERS);
		List<DistPerson> distPersons = new ArrayList<>();
		if (ObjectUtil.isEmpty(flowUsers)) return distPersons;
		String userParams = CommonHttpUrlInvokeUtils.isTypeJSON(flowUsers);
		if (ObjectUtil.isEmpty(userParams)) return distPersons;
		if(JSONUtil.isTypeJSONArray(userParams)) {
			JSONArray json = JSONUtil.parseArray(userParams);
			distPersons = BeanUtil.copyToList(json, DistPerson.class);
		} else {
			JSONObject json = JSONUtil.parseObj(userParams);
			DistPerson distPerson = BeanUtil.toBean(json, DistPerson.class);
			distPersons.add(distPerson);
		}
		distPersons.forEach(distPerson ->
			this.extractedDistPerson(distPerson, MapUtil.getLong(params, FlowEntityInfoConstants.FLOW_INST_ID),
					MapUtil.getStr(params, FlowEntityInfoConstants.FLOW_KEY),
					MapUtil.getStr(params, FlowEntityInfoConstants.CODE),
					MapUtil.getLong(params, FlowEntityInfoConstants.CREATE_USER))
		);
		return distPersons;
	}

	/**
	 * 扩展公共字段的值
	 *
	 * @param distPerson  分配参与者
	 * @param flowInstId  流程实例ID
	 * @param flowKey 业务KEY
	 * @param code        业务CODE
	 * @param createUser  创建人
	 */
	private void extractedDistPerson(DistPerson distPerson, Long flowInstId, String flowKey, String code, Long createUser) {
		distPerson.setFlowInstId(flowInstId);
		distPerson.setFlowKey(flowKey);
		distPerson.setCode(code);
		distPerson.setCreateUser(createUser);
		distPerson.setCreateTime(LocalDateTime.now());
	}

	// @GlobalTransactional
	@Override
	public boolean saveByFlowInstId(DistPersonVO distPersonVO) {
		this.validateValType(distPersonVO);
		List<DistPerson> distPersons = distPersonVO.getRoleUserId();
		// 扩展流程实例信息
		this.extractRunFlowInfo(distPersonVO);
		distPersons = this.buildCommonDistPersons(distPersonVO, distPersons);
		if (CollUtil.isEmpty(distPersons)) return true;
		this.validateDistPerson(distPersons);
		// 被分配后立即运行执行任务加减签
		if (FlowCommonConstants.YES.equals(distPersonVO.getIsNowRun())) {
			// 根据参与者动态计算全量任务
			jsonFlowEngineService.calculateAllRunJobs(distPersonVO, distPersons);
		}
		this.removeDistPersons(distPersonVO.getFlowInstId(), CollUtil.newArrayList(distPersonVO.getUserKey()));
		distPersons.forEach(baseMapper::insert);
		// baseMapper.insertBatchSomeColumn(distPersons);
		return true;
	}

	private void extractRunFlowInfo(DistPersonVO params) {
		// 判断流程是否结束
		RunFlow runFlow = RunFlowContextHolder.initRunFlow(params.getFlowKey(), params.getFlowInstId(), null);
		if (!FlowStatusEnum.RUN.getStatus().equals(runFlow.getStatus())) {
			params.setIsFlowFinish(FlowCommonConstants.YES);
		}
		// 在监听时可能无Code参数
		params.setDefFlowId(runFlow.getDefFlowId());
		params.setCode(runFlow.getCode());
	}

	/**
	 * 构建流程中公共分配参与者参数
	 *
	 * @param distPersonVO 分配参与者
	 * @param distPersons  分配参与者
	 */
	private List<DistPerson> buildCommonDistPersons(DistPersonVO distPersonVO, List<DistPerson> distPersons) {
		Long userId = SecurityUtils.getUser().getId();
		// 不能删除别人分配的人员
		distPersons = distPersons.stream().filter(
				f -> Objects.isNull(f.getCreateUser()) || f.getCreateUser().equals(userId)
		).peek(distPerson -> {
			distPerson.setUserKey(distPersonVO.getUserKey());
			extractedDistPerson(distPerson, distPersonVO.getFlowInstId(), distPersonVO.getFlowKey(), distPersonVO.getCode(), userId);
		}).collect(Collectors.toList());
		return distPersons;
	}

	// @GlobalTransactional
	@Override
	public boolean saveOrUpdate(DistPersonVO distPersonVO) {
		DistPerson distPerson = new DistPerson();
		BeanUtil.copyProperties(distPersonVO, distPerson);
		Long userId = SecurityUtils.getUser().getId();
		this.validationException(distPersonVO, distPerson, userId);
		// 被分配后立即运行执行任务加减签
		if (FlowCommonConstants.YES.equals(distPersonVO.getIsNowRun())) {
			// TODO 与删除一致，在运行到节点时自动进行动态加减签
		}
		// 新增为自己
		if (Objects.isNull(distPersonVO.getCreateUser())) {
			distPerson.setCreateUser(userId);
		}
		return super.saveOrUpdate(distPerson);
	}

	/**
	 * 前置校验必填项
	 *
	 * @param distPersonVO 分配参与者
	 */
	private void validationException(DistPersonVO distPersonVO, DistPerson distPerson, Long userId) {
		// 可以不选择节点或任务
		// this.validateValType(distPersonVO);
		this.validateDistPerson(CollUtil.newArrayList(distPerson));

		boolean b = Objects.nonNull(distPersonVO.getCreateUser()) && !distPersonVO.getCreateUser().equals(userId);
		if (b) throw new ValidationException(ValidationExceptions.NO_PERMIT_UPDATE);
	}

	@Override
	public DistPersonVO getByFlowInstId(DistPersonVO distPersonVO) {
		DistPersonVO res = new DistPersonVO();
		Long flowInstId = distPersonVO.getFlowInstId();
		String userKey = distPersonVO.getUserKey();
		if (StrUtil.isEmpty(userKey)) throw new ValidationException(ValidationExceptions.NO_USER_KEY);
		List<DistPerson> list = this.list(Wrappers.<DistPerson>lambdaQuery().eq(DistPerson::getFlowInstId, flowInstId)
				.eq(DistPerson::getUserKey, userKey));
		if (CollUtil.isEmpty(list)) return res;
		// 封装数据
		res.getRoleUserId().addAll(list);
		return res;
	}

	// @GlobalTransactional
	@Override
	public boolean incrementCalculate(DistPersonVO distPersonVO) {
		this.validateValType(distPersonVO);
		// 扩展流程实例信息
		this.extractRunFlowInfo(distPersonVO);
		List<DistPerson> distPersons = distPersonVO.getRoleUserId();
		distPersons = this.buildCommonDistPersons(distPersonVO, distPersons);
		if (CollUtil.isEmpty(distPersons)) return true;
		this.validateDistPerson(distPersons);
		// 被分配后立即运行执行任务加减签
		if (FlowCommonConstants.YES.equals(distPersonVO.getIsNowRun())) {
			// 根据参与者动态计算增量任务
			jsonFlowEngineService.incrementCalculateRunJobs(distPersonVO, distPersons);
		}
		List<String> keys = distPersons.stream().map(DistPerson::getUserKey).collect(Collectors.toList());
		this.removeDistPersons(distPersonVO.getFlowInstId(), keys);
		distPersons.forEach(baseMapper::insert);
		// baseMapper.insertBatchSomeColumn(distPersons);
		return true;
	}

	/**
	 * 目前都修改，不管是否有未来任务
	 *
	 * @param handoverUser 交接人
	 * @param receiveUser  接收人
	 * @param flowInstIds 流程实例ID集合
	 */
	@Override
	public boolean handleJobHandover(Long handoverUser, Long receiveUser, List<Long> flowInstIds) {
		return this.lambdaUpdate().set(DistPerson::getRoleId, receiveUser)
				// 离职则交接所有
				.in(CollUtil.isNotEmpty(flowInstIds), DistPerson::getFlowInstId, flowInstIds)
				.eq(DistPerson::getRoleId, handoverUser.toString())
				// 只更新用户任务
				.eq(DistPerson::getJobType, JobUserTypeEnum.USER.getType())
				.update();
	}

}
