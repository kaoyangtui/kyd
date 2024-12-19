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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.DefFlowVO;
import com.pig4cloud.pigx.jsonflow.engine.JsonFlowEngineService;
import com.pig4cloud.pigx.jsonflow.mapper.DefFlowMapper;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 流程定义管理
 *
 * @author luolin
 * @date 2021-02-23 13:52:11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DefFlowServiceImpl extends ServiceImpl<DefFlowMapper, DefFlow> implements DefFlowService {

	@Lazy
	@Autowired
	private JsonFlowEngineService jsonFlowEngineService;

	@Override
	public Boolean saveOrUpdate(DefFlowVO defFlowVO) {
		DefFlowVO attrs = defFlowVO.getAttrs();
		attrs.setCreateUser(SecurityUtils.getUser().getId());

		DefFlow defFlow = new DefFlow();
		BeanUtil.copyProperties(attrs, defFlow);
		// 判断版本
		this.upgradeVersion(defFlow);
		attrs.setVersion(defFlow.getVersion());

		this.saveOrUpdate(defFlow);
		// 保存其他信息
		return jsonFlowEngineService.saveOrUpdateDefFlow(defFlowVO);
	}

	@Override
	public DefFlowVO getNodesById(Long id) {
		return jsonFlowEngineService.getNodesByDefFlowId(id);
	}

	/**
	 * 判断版本
	 * @param defFlow 流程定义
	 */
	private void upgradeVersion(DefFlow defFlow) {
		DefFlow byId = this.getById(defFlow.getId());
		if (Objects.isNull(byId)) {
			List<DefFlow> existDefFlows = this.list(Wrappers.<DefFlow>lambdaQuery().eq(DefFlow::getFlowKey, defFlow.getFlowKey())
					.orderByDesc(DefFlow::getVersion));
			if (CollUtil.isNotEmpty(existDefFlows)) {
				int newVersion = existDefFlows.get(0).getVersion() + 1;
				defFlow.setVersion(newVersion);
			}
		}
	}

	@Override
	public Boolean tempStore(DefFlow defFlow) {
		// 判断版本
		this.upgradeVersion(defFlow);
		return this.saveOrUpdate(defFlow);
	}

	@Override
	public Boolean removeById(Long id) {
		super.removeById(id);
		return jsonFlowEngineService.removeById(id);
	}

}
