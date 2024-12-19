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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.TabsOptionEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.entity.FormOption;
import com.pig4cloud.pigx.jsonflow.api.entity.TabsOption;
import com.pig4cloud.pigx.jsonflow.api.order.FlowTempStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.FormOptionVO;
import com.pig4cloud.pigx.jsonflow.mapper.FormOptionMapper;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
import com.pig4cloud.pigx.jsonflow.service.FormOptionService;
import com.pig4cloud.pigx.jsonflow.service.TabsOptionService;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 表单/页面操作表
 *
 * @author luolin
 * @date 2024-02-10 22:50:44
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FormOptionServiceImpl extends ServiceImpl<FormOptionMapper, FormOption> implements FormOptionService {

	@Autowired
	private FlowNodeService flowNodeService;
	@Autowired
	private TabsOptionService tabsOptionService;
	@Autowired
	private DefFlowService defFlowService;

	@Override
	public IPage<FormOption> getPage(Page<FormOption> page, FormOption formOption, String[] queryTime) {
		QueryWrapper<FormOption> query = Wrappers.query(formOption);
		QueryWrapperUtils.userQueryTime(query, FormOption::getCreateTime, queryTime
				, FormOption::getCreateUser, SecurityUtils.getUser().getId());
		this.page(page, query);
		return page;
	}

	@Override
	public boolean saveOpUpdate(FormOptionVO formOptionVO) {
		List<FormOption> columns = formOptionVO.getColumns();
		if (CollUtil.isEmpty(columns)) {
			FormOption formOption = new FormOption();
			BeanUtil.copyProperties(formOptionVO, formOption);
			this.removeFormOption(formOption);
			return true;
		}
		columns.forEach(each -> {
			each.setId(null);
			each.setType(formOptionVO.getType());
			each.setFormType(formOptionVO.getFormType());
			each.setFormId(formOptionVO.getFormId());
			each.setFormName(formOptionVO.getFormName());
			each.setPath(formOptionVO.getPath());
			each.setDefFlowId(formOptionVO.getDefFlowId());
			each.setFlowNodeId(formOptionVO.getFlowNodeId());
			each.setFlowKey(formOptionVO.getFlowKey());
		});
		this.removeFormOption(columns.get(0));
		this.saveBatch(columns);
		return true;
	}

	@Override
	public boolean savePrintTemp(FormOptionVO formOptionVO) {
		FormOption formOption = new FormOption();
		BeanUtil.copyProperties(formOptionVO, formOption);
		this.removeFormOption(formOption);
		formOption.setId(null);
		this.save(formOption);
		return true;
	}

	@Override
	public List<FormOption> listFormOption(FormOptionVO formOptionVO) {
		FormOption formOption = new FormOption();
		BeanUtil.copyProperties(formOptionVO, formOption);
		List<FormOption> list = this.getList(formOption);
		if (FlowCommonConstants.YES.equals(formOptionVO.getIsOnlyCurr())) {
			return list;
		}
		if (CollUtil.isEmpty(list) && !CommonNbrPool.STR_0.equals(formOption.getType())) {
			formOption.setDefFlowId(null);
			formOption.setFlowNodeId(null);
			formOption.setType(CommonNbrPool.STR_0);
			list = this.getList(formOption);
		}
		return list;
	}

	private List<FormOption> getList(FormOption formOption) {
		Long flowInstId = formOption.getFlowInstId();
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		return this.list(Wrappers.<FormOption>lambdaQuery().eq(FormOption::getType, formOption.getType())
				// .eq(StrUtil.isNotBlank(formOption.getFormType()), FormOption::getFormType, formOption.getFormType())
				.eq(Objects.nonNull(formOption.getFormId()), FormOption::getFormId, formOption.getFormId())
				.eq(Objects.nonNull(formOption.getDefFlowId()), FormOption::getDefFlowId, formOption.getDefFlowId())
				.eq(Objects.nonNull(formOption.getFlowNodeId()), FormOption::getFlowNodeId, formOption.getFlowNodeId())
				// 查询流程实例数据
				.eq(Objects.nonNull(flowInstId), FormOption::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FormOption::getFlowInstId));
	}

	@Override
	public boolean removeFormOption(FormOption formOption) {
		Long flowInstId = formOption.getFlowInstId();
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		return this.remove(Wrappers.<FormOption>lambdaQuery().eq(FormOption::getType, formOption.getType())
				// .eq(FormOption::getFormType, formOption.getFormType())
				.eq(FormOption::getFormId, formOption.getFormId())
				.eq(Objects.nonNull(formOption.getDefFlowId()), FormOption::getDefFlowId, formOption.getDefFlowId())
				.eq(Objects.nonNull(formOption.getFlowNodeId()), FormOption::getFlowNodeId, formOption.getFlowNodeId())
				// 查询流程实例数据
				.eq(Objects.nonNull(flowInstId), FormOption::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FormOption::getFlowInstId));
	}

	@Override
	public FormOptionVO listStartPerm(FormOptionVO formOptionVO) {
		FormOption formOption = new FormOption();
		BeanUtil.copyProperties(formOptionVO, formOption);
		// 查询开始节点
		FlowNode flowNode;
		if (Objects.nonNull(formOption.getDefFlowId())) {
			flowNode = flowNodeService.getOne(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, formOption.getDefFlowId())
					.eq(FlowNode::getNodeType, NodeTypeEnum.START.getType()));
		} else {
			String flowKey = formOptionVO.getFlowKey();
			List<DefFlow> defFlows = defFlowService.list(Wrappers.<DefFlow>lambdaQuery().eq(DefFlow::getFlowKey, flowKey)
					.eq(DefFlow::getStatus, FlowTempStatusEnum.PUBLISH.getStatus()).orderByDesc(DefFlow::getVersion)
					.orderByDesc(DefFlow::getCreateTime));
			if (CollUtil.isEmpty(defFlows)) throw new RuntimeException("不存在当前办公申请的已发布流程" + flowKey);
			DefFlow defFlow = defFlows.get(0);
			flowNode = flowNodeService.getOne(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, defFlow.getId())
					.eq(FlowNode::getNodeType, NodeTypeEnum.START.getType()));
			formOptionVO.setDefFlowId(defFlow.getId());
		}
		formOption.setFlowNodeId(flowNode.getId());
		List<FormOption> list = this.getList(formOption);
		if (CollUtil.isEmpty(list)) return formOptionVO;
		FormOption res = list.get(0);
		TabsOption tabsOption = tabsOptionService.getById(res.getFormId());
		if (Objects.isNull(tabsOption)) {
			List<String> pcTodoUrl = Arrays.stream(flowNode.getPcTodoUrl()).collect(Collectors.toList());
			tabsOption = new TabsOption();
			tabsOption.setIsView(FlowCommonConstants.YES);
			if (pcTodoUrl.contains(TabsOptionEnum.EDIT_RUN_APPLICATION.getId())) {
				tabsOption.setIsView(FlowCommonConstants.NO);
			}
			// 前端查询type
			// tabsOption.setType(flowApplication.getType());
		}
		formOptionVO.setFlowNodeId(flowNode.getId());
		formOptionVO.setTabsOption(tabsOption);
		formOptionVO.setColumns(list);
		return formOptionVO;
	}

	@Override
	public FormOptionVO listPrintTemp(FormOptionVO formOptionVO) {
		FormOption formOption = new FormOption();
		BeanUtil.copyProperties(formOptionVO, formOption);
		List<FormOption> list = this.getList(formOption);
		if (CollUtil.isNotEmpty(list)) formOptionVO.setPrintInfo(list.get(0).getPrintInfo());
		if (FlowCommonConstants.YES.equals(formOptionVO.getIsOnlyCurr())) {
			return formOptionVO;
		}
		if (!CommonNbrPool.STR_0.equals(formOption.getType())) {
			formOption.setDefFlowId(null);
			formOption.setFlowNodeId(null);
			formOption.setType(CommonNbrPool.STR_0);
			list = this.getList(formOption);
			formOptionVO.setColumns(list);
		}
		return formOptionVO;
	}

	@Override
	public void removeByDefFlowId(Long defFlowId, Long flowInstId, String type) {
		flowInstId = RunFlowContextHolder.validateIndependent(flowInstId);
		this.remove(Wrappers.<FormOption>lambdaQuery().eq(StrUtil.isNotBlank(type), FormOption::getType, type)
				.eq(FormOption::getDefFlowId, defFlowId)
				.eq(Objects.nonNull(flowInstId), FormOption::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FormOption::getFlowInstId));
	}

	@Override
	public List<FormOption> listFormOptions(FormOption perm) {
		Long flowInstId = RunFlowContextHolder.validateIndependent(perm.getFlowInstId());
		perm.setFlowInstId(null);
		return this.list(Wrappers.lambdaQuery(perm).eq(Objects.nonNull(flowInstId), FormOption::getFlowInstId, flowInstId)
				.isNull(Objects.isNull(flowInstId), FormOption::getFlowInstId));
	}

}
