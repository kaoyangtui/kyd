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
package com.pig4cloud.pigx.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.order.FlowTempStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.order.api.entity.FlowApplication;
import com.pig4cloud.pigx.order.api.vo.FlowApplicationVO;
import com.pig4cloud.pigx.order.mapper.FlowApplicationMapper;
import com.pig4cloud.pigx.order.service.FlowApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 工单申请
 *
 * @author luolin
 * @date 2022-09-17 00:58:32
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowApplicationServiceImpl extends ServiceImpl<FlowApplicationMapper, FlowApplication> implements FlowApplicationService {

	@Override
	public FlowApplication saveOrUpdate(FlowApplicationVO flowApplicationVO) {
		// 判断版本
		FlowApplication byId = this.getById(flowApplicationVO.getId());
		if (Objects.isNull(byId)) {
			List<FlowApplication> existFlowApplications = this.list(Wrappers.<FlowApplication>lambdaQuery().eq(FlowApplication::getFlowKey, flowApplicationVO.getFlowKey())
					.orderByDesc(FlowApplication::getVersion));
			if (CollUtil.isNotEmpty(existFlowApplications)) {
				int newVersion = existFlowApplications.get(0).getVersion() + 1;
				flowApplicationVO.setVersion(newVersion);
			}
		}
		FlowApplication flowApplication = new FlowApplication();
		BeanUtil.copyProperties(flowApplicationVO, flowApplication);
		flowApplication.setCreateUser(SecurityUtils.getUser().getId());
		super.saveOrUpdate(flowApplication);
		return flowApplication;
	}

	@Override
	public List<FlowApplication> listByPerms(FlowApplication flowApplication) {
		List<FlowApplication> records = this.list(Wrappers.<FlowApplication>lambdaQuery().eq(FlowApplication::getStatus, FlowTempStatusEnum.PUBLISH.getStatus())
				.eq(StrUtil.isNotBlank(flowApplication.getGroupName()), FlowApplication::getGroupName, flowApplication.getGroupName())
				.orderByAsc(FlowApplication::getSort));
		// 处理操作权限
		if (CollUtil.isNotEmpty(records) && CollUtil.isNotEmpty(SecurityUtils.getRoleIds())) {
			records = records.stream().filter(f -> Arrays.stream(f.getPermission()).anyMatch(any -> SecurityUtils.getRoleIds().contains(Long.valueOf(any))))
					.collect(Collectors.toList());
		}
		return records;
	}

}
