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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.order.FlowTempStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.order.OrderStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.order.api.constant.OrderCommonConstants;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import com.pig4cloud.pigx.order.api.entity.FlowApplication;
import com.pig4cloud.pigx.order.api.entity.RunApplication;
import com.pig4cloud.pigx.order.api.vo.RunApplicationVO;
import com.pig4cloud.pigx.order.base.OrderCommonServiceImpl;
import com.pig4cloud.pigx.order.mapper.RunApplicationMapper;
import com.pig4cloud.pigx.order.service.FlowApplicationService;
import com.pig4cloud.pigx.order.service.RunApplicationService;
import com.pig4cloud.pigx.order.table.TableCrudHandler;
import com.pig4cloud.pigx.order.table.TableCrudModelEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 我的工单
 *
 * @author luolin
 * @date 2022-09-17 00:58:49
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class RunApplicationServiceImpl extends OrderCommonServiceImpl<RunApplicationMapper, RunApplication> implements RunApplicationService {

	private final FlowApplicationService flowApplicationService;
	private final TableCrudHandler tableCrudHandler;
	private final IdentifierGenerator idGenerator;
	private final ObjectMapper objectMapper;

	@Override
	public IPage<RunApplication> getPage(Page<RunApplication> page, RunApplication runApplication, String[] queryTime) {
		QueryWrapper<RunApplication> query = Wrappers.query(runApplication);
		QueryWrapperUtils.userQueryTime(query, RunApplication::getCreateTime, queryTime
				, RunApplication::getCreateUser, SecurityUtils.getUser().getId());

		List<RunApplication> records = this.page(page, query).getRecords();
		// 处理单独表存储
		records.forEach(each -> this.doTableCrudHandle(each, TableCrudModelEnum.SELECT));
		return page;
	}

	@Override
	// @GlobalTransactional
	public <Vo> Vo startSubFlow(Vo order) {
		RunApplicationVO params = (RunApplicationVO) order;
		String flowKey = params.getFlowKey();
		List<FlowApplication> flowApplications = flowApplicationService.list(Wrappers.<FlowApplication>lambdaQuery().eq(FlowApplication::getFlowKey, flowKey)
				.eq(FlowApplication::getDefFlowId, params.getDefFlowId())
				.eq(FlowApplication::getStatus, FlowTempStatusEnum.PUBLISH.getStatus()).orderByDesc(FlowApplication::getVersion)
				.orderByDesc(FlowApplication::getCreateTime));
		if (CollUtil.isEmpty(flowApplications)) throw new RuntimeException("不存在当前关联子流程的已发布表单" + flowKey);
		FlowApplication flowApplication = flowApplications.get(0);
		RunApplicationVO runApplicationVO = new RunApplicationVO();
		BeanUtil.copyProperties(flowApplication, runApplicationVO, OrderEntityInfoConstants.ID, OrderEntityInfoConstants.STATUS,
				OrderEntityInfoConstants.CREATE_USER, OrderEntityInfoConstants.CREATE_TIME);
		runApplicationVO.setFlowInstId(params.getFlowInstId());
		runApplicationVO.setFormData(params.getFormData());
		runApplicationVO.setFormId(flowApplication.getId());
		Map<String, Object> flowVars = this.doSaveOrUpdate(runApplicationVO);
		runApplicationVO.setFlowVarUser(flowVars);
		return (Vo) runApplicationVO;
	}

	@Override
	// @GlobalTransactional
	public <Vo> Boolean backParFlow(Vo order) {
		RunApplication runApplication = this.updateRunApplication((RunApplicationVO) order);
		return super.updateById(runApplication);
	}

	private <Vo> RunApplication updateRunApplication(RunApplicationVO order) {
		RunApplication runApplication = this.getOne(Wrappers.<RunApplication>lambdaUpdate().eq(RunApplication::getFlowInstId, order.getFlowInstId()));
		runApplication.setFormData(order.getFormData());
		// 处理单独表存储
		this.doTableCrudHandle(runApplication, TableCrudModelEnum.UPDATE);
		return runApplication;
	}

	@Override
	public <Vo> Boolean restartSubFlow(Vo order) {
		RunApplication runApplication = this.updateRunApplication((RunApplicationVO) order);
		runApplication.setStatus(OrderStatusEnum.RUN.getStatus());
		return super.updateById(runApplication);
	}

	@Override
	public Boolean saveOrUpdate(RunApplicationVO runApplicationVO) {
		Map<String, Object> params = this.doSaveOrUpdate(runApplicationVO);
		// 启动流程公共接口
		this.startFlow(params, runApplicationVO, null);
		return Boolean.TRUE;
	}

	private Map<String, Object> doSaveOrUpdate(RunApplicationVO runApplicationVO) {
		runApplicationVO.setCreateTime(LocalDateTime.now());
		runApplicationVO.setCreateUser(SecurityUtils.getUser().getId());
		// 对象转化
		RunApplication runApplication = new RunApplication();
		BeanUtil.copyProperties(runApplicationVO, runApplication);
		// 设置流程参数
		Map<String, Object> params = MapUtil.newHashMap();
		this.buildFlowVariables(params, runApplicationVO);
		// 判断新增 处理单独表存储
		if (Objects.isNull(runApplication.getId())) {
			this.doTableCrudHandle(runApplication, TableCrudModelEnum.SAVE);
		}
		else {
			this.doTableCrudHandle(runApplication, TableCrudModelEnum.UPDATE);
		}
		// 发起流程并保存工单
		super.saveOrUpdateOrder(params, runApplication);
		super.updateOrderVOInfo(runApplication, runApplicationVO);
		return params;
	}

	/**
	 * 表数据CRUD操作
	 * TODO 查询、修改、删除条件暂时为表单ID
	 * @param runApplication 工单信息
	 * @param model 模式
	 */
	@Override
	public void doTableCrudHandle(RunApplication runApplication, TableCrudModelEnum model) {
		if (!TableCrudModelEnum.SELECT.equals(model) && StrUtil.isBlank(runApplication.getFormData())) return;
		String tableName = runApplication.getTableName();
		List<String> subTableNames = new ArrayList<>();
		if (ArrayUtil.isNotEmpty(runApplication.getSubTableName())) {
			subTableNames = Arrays.stream(runApplication.getSubTableName()).filter(StrUtil::isNotBlank).collect(Collectors.toList());
		}
		String mainSubProp = runApplication.getMainSubProp();
		boolean a = TableCrudModelEnum.SAVE.equals(model) || TableCrudModelEnum.UPDATE.equals(model);
		boolean b = TableCrudModelEnum.SELECT.equals(model) || TableCrudModelEnum.DEL.equals(model);
		if (OrderCommonConstants.DEF_TABLE_NAME.equals(tableName)) {
			// 判断是否关联子表
			this.extractedOnlySubTableName(runApplication, model, subTableNames, mainSubProp, a, b);
			return;
		}
		Map<String, Object> data = new HashMap<>();
		// 先删后增，update bug
		if (TableCrudModelEnum.UPDATE.equals(model)) {
			data.put(TableCrudHandler.KEY_PREFIX + OrderEntityInfoConstants.RUN_FORM_ID, runApplication.getId());
			tableCrudHandler.tableCrudHandle(tableName, TableCrudModelEnum.DEL, CollUtil.newArrayList(data));
			data.clear();
		}
		if (a) {
			if (Objects.isNull(runApplication.getId())) {
				runApplication.setId(idGenerator.nextId(runApplication).longValue());
			}
			this.extFixedFieldValue(runApplication, data);
			JSONObject entries = JSONUtil.parseObj(runApplication.getFormData(), true);
			if (CollUtil.isNotEmpty(subTableNames)) {
				List<String> mainSubProps = Arrays.stream(mainSubProp.split(StrUtil.COMMA)).collect(Collectors.toList());
				for (int i = 0; i < subTableNames.size(); i++) {
					String each = subTableNames.get(i);
					String mainSubProp2 = mainSubProps.get(i);
					this.doSubTableCrudHandle(runApplication, model, (Long) data.get(OrderEntityInfoConstants.ID), each, mainSubProp2);
				}
				mainSubProps.forEach(entries::remove);
			}
			/*if (entries.isEmpty()) { 主表必须存在数据，否则导致子表关联主表列名null
				runApplication.setFormData(null);
				return new HashMap<>();
			}*/
			entries.remove(OrderEntityInfoConstants.ID);
			data.putAll(entries);
			if (TableCrudModelEnum.UPDATE.equals(model)) model = TableCrudModelEnum.SAVE;
			// TODO 业务是否保留两份数据
			runApplication.setFormData(null);
			tableCrudHandler.tableCrudHandle(tableName, model, CollUtil.newArrayList(data));
		} else if (b) {
			boolean select = TableCrudModelEnum.SELECT.equals(model);
			data.put(TableCrudHandler.KEY_PREFIX + OrderEntityInfoConstants.RUN_FORM_ID, runApplication.getId());
			List<Map<String, Object>> mainMaps = tableCrudHandler.tableCrudHandle(tableName, model, CollUtil.newArrayList(data));
			if (CollUtil.isEmpty(mainMaps)) return;
			if (CollUtil.isNotEmpty(subTableNames)) {
				List<String> mainSubProps = Arrays.stream(mainSubProp.split(StrUtil.COMMA)).collect(Collectors.toList());
				for (int i = 0; i < subTableNames.size(); i++) {
					String each = subTableNames.get(i);
					String mainSubProp2 = mainSubProps.get(i);
					List<Map<String, Object>> maps = this.doSubTableCrudHandle(runApplication, model, null, each, mainSubProp2);
					if (CollUtil.isEmpty(maps)) continue;
					if (select) mainMaps.get(0).put(mainSubProp2, maps);
				}
			}
			if (select) {
				try {
					runApplication.setFormData(objectMapper.writeValueAsString(mainMaps.get(0)));
				} catch (JsonProcessingException e) {
					throw new RuntimeException("自建表返回数据序列化异常", e);
				}
			}
		}
	}

	private void extractedOnlySubTableName(RunApplication runApplication, TableCrudModelEnum model, List<String> subTableNames, String mainSubProp, boolean a, boolean b) {
		if (CollUtil.isEmpty(subTableNames)) return;
		List<String> mainSubProps = Arrays.stream(mainSubProp.split(StrUtil.COMMA)).collect(Collectors.toList());
		JSONObject entries = JSONUtil.parseObj(runApplication.getFormData(), true);
		if (a) {
			for (int i = 0; i < subTableNames.size(); i++) {
				String each = subTableNames.get(i);
				String mainSubProp2 = mainSubProps.get(i);
				this.doSubTableCrudHandle(runApplication, model, null, each, mainSubProp2);
			}
			mainSubProps.forEach(entries::remove);
			runApplication.setFormData(entries.toString());
		} else if (b) {
			boolean select = TableCrudModelEnum.SELECT.equals(model);
			for (int i = 0; i < subTableNames.size(); i++) {
				String each = subTableNames.get(i);
				String mainSubProp2 = mainSubProps.get(i);
				List<Map<String, Object>> maps = this.doSubTableCrudHandle(runApplication, model, null, each, mainSubProp2);
				if (CollUtil.isEmpty(maps)) continue;
				if (select) entries.set(mainSubProp2, maps);
			}
			if (select && MapUtil.isNotEmpty(entries)) {
				try {
					runApplication.setFormData(objectMapper.writeValueAsString(entries));
				} catch (JsonProcessingException e) {
					throw new RuntimeException("自建表返回数据序列化异常", e);
				}
			}
		}
	}

	/**
	 * 子表数据CRUD操作
	 * TODO 查询、修改、删除条件暂时为表单ID
	 * @param runApplication 工单信息
	 * @param model 模式
	 */
	private List<Map<String, Object>> doSubTableCrudHandle(RunApplication runApplication, TableCrudModelEnum model, Long manId, String subTableName, String mainSubProp) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> res = new ArrayList<>();
		if (StrUtil.isBlank(subTableName)) return res;
		// 先删后增，update bug
		if (TableCrudModelEnum.UPDATE.equals(model)) {
			data.put(TableCrudHandler.KEY_PREFIX + OrderEntityInfoConstants.RUN_FORM_ID, runApplication.getId());
			tableCrudHandler.tableCrudHandle(subTableName, TableCrudModelEnum.DEL, CollUtil.newArrayList(data));
			data.clear();
		}
		if (TableCrudModelEnum.SAVE.equals(model) || TableCrudModelEnum.UPDATE.equals(model)) {
			if (Objects.isNull(runApplication.getId())) {
				runApplication.setId(idGenerator.nextId(runApplication).longValue());
			}
			JSONObject entries = JSONUtil.parseObj(runApplication.getFormData(), true);
			JSONArray jsonArray = entries.getJSONArray(mainSubProp);
			if (jsonArray.isEmpty()) return res;
			List<Map<String, Object>> subData = new ArrayList<>();
			jsonArray.forEach(each -> {
				JSONObject entry = JSONUtil.parseObj(each, true);
				this.extFixedFieldValue(runApplication, entry);
				if (Objects.isNull(manId)) {
					entry.set(runApplication.getSubMainField(), runApplication.getId());
				} else {
					entry.set(runApplication.getSubMainField(), manId);
				}
				subData.add(entry);
			});
			if (TableCrudModelEnum.UPDATE.equals(model)) model = TableCrudModelEnum.SAVE;
			tableCrudHandler.tableCrudHandle(subTableName, model, subData);
		} else if (TableCrudModelEnum.SELECT.equals(model) || TableCrudModelEnum.DEL.equals(model)){
			data.put(TableCrudHandler.KEY_PREFIX + OrderEntityInfoConstants.RUN_FORM_ID, runApplication.getId());
			return tableCrudHandler.tableCrudHandle(subTableName, model, CollUtil.newArrayList(data));
		}
		return res;
	}

	private void extFixedFieldValue(RunApplication runApplication, Map<String, Object> data) {
		data.put(OrderEntityInfoConstants.RUN_FORM_ID, runApplication.getId());
		data.put(OrderEntityInfoConstants.ID, idGenerator.nextId(null).longValue());
		data.put(OrderEntityInfoConstants.CREATE_USER, runApplication.getCreateUser());
		data.put(OrderEntityInfoConstants.CREATE_TIME, runApplication.getCreateTime());
	}

	/**
	 * 构建流程参数
	 *
	 * @param params 参数
	 * @param runApplicationVO 请假工单
	 */
	private void buildFlowVariables(Map<String, Object> params, RunApplicationVO runApplicationVO) {
		JSONObject order = JSONUtil.parseObj(runApplicationVO.getFormData());
		// 构建流程公共参数
		super.buildFlowVariables(params, order);
	}

	@Override
	// @GlobalTransactional
	public Boolean tempStore(RunApplicationVO runApplicationVO) {
		runApplicationVO.setCreateTime(LocalDateTime.now());
		runApplicationVO.setCreateUser(SecurityUtils.getUser().getId());
		// 对象转化
		RunApplication runApplication = new RunApplication();
		BeanUtil.copyProperties(runApplicationVO, runApplication);
		// 判断新增 处理单独表存储
		if (Objects.isNull(runApplication.getId())) {
			this.doTableCrudHandle(runApplication, TableCrudModelEnum.SAVE);
		} else {
			this.doTableCrudHandle(runApplication, TableCrudModelEnum.UPDATE);
		}
		return super.tempStore(runApplication);
	}

	@Override
	// @GlobalTransactional
	public Boolean updateOrder(RunApplicationVO runApplicationVO) {
		runApplicationVO.setUpdateTime(LocalDateTime.now());
		runApplicationVO.setUpdateUser(SecurityUtils.getUser().getId());

		RunApplication runApplication = new RunApplication();
		BeanUtil.copyProperties(runApplicationVO, runApplication);
		// 处理单独表存储
		this.doTableCrudHandle(runApplication, TableCrudModelEnum.UPDATE);
		boolean save = super.updateById(runApplication);
		// TODO 自己决定更新时是否更新流程参数
		/*String runJobId = runApplicationVO.getRunJobId();
		if (StrUtil.isEmpty(runJobId)) return true;
		Map<String, Object> params = MapUtil.of(OrderCommonConstants.RUN_JOB_ID, runJobId);
		this.buildFlowVariables(params, runApplicationVO);
		super.updateFlowVariables(params, runApplicationVO);*/
		return save;
	}

	@Override
	public Boolean removeOrder(Long id) {
		RunApplication runApplication = this.getById(id);
		// 处理单独表存储
		this.doTableCrudHandle(runApplication, TableCrudModelEnum.DEL);
		return this.removeById(id);
	}

	@Override
	public RunApplication getOrderById(Long id) {
		RunApplication runApplication = this.getById(id);
		// 处理单独表存储
		this.doTableCrudHandle(runApplication, TableCrudModelEnum.SELECT);
		return runApplication;
	}

	@Override
	public RunApplication getByFlowInstId(Long flowInstId) {
		RunApplication runApplication = this.getOne(Wrappers.<RunApplication>lambdaQuery().eq(RunApplication::getFlowInstId, flowInstId));
		// 处理单独表存储
		this.doTableCrudHandle(runApplication, TableCrudModelEnum.SELECT);
		return runApplication;
	}

}
