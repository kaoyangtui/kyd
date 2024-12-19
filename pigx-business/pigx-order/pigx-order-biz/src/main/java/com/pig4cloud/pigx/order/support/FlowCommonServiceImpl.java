package com.pig4cloud.pigx.order.support;
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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.util.DataBaseUtil;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import com.pig4cloud.pigx.order.api.constant.OrderTableInfoConstants;
import com.pig4cloud.pigx.order.api.constant.enums.OrderIdentityKeyEnum;
import com.pig4cloud.pigx.order.api.entity.RunApplication;
import com.pig4cloud.pigx.order.api.vo.RunApplicationVO;
import com.pig4cloud.pigx.order.base.OrderCommonService;
import com.pig4cloud.pigx.order.service.HandoverFlowService;
import com.pig4cloud.pigx.order.service.RunApplicationService;
import com.pig4cloud.pigx.order.table.TableCrudModelEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用于工作流服务 注意：公共接口只用于查询
 *
 * @author luolin
 * @date 2020/2/15 13:16
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowCommonServiceImpl {

	private final RunApplicationService runApplicationService;
	private final Map<String, OrderCommonService<?>> orderCommonServiceMap;
	private final ObjectMapper objectMapper;
	private final HandoverFlowService handoverFlowService;

	/**
	 * 根据flowKey获取工单处理类实例
	 *
	 * @param params 参数
	 */
	private <T> OrderCommonService<T> getOrderCommonService(Map<String, Object> params) {
		String beanId = OrderIdentityKeyEnum.getEnumByParams(params).getBeanId();
		return (OrderCommonService<T>) orderCommonServiceMap.get(beanId);
	}

	/**
	 * 根据nodeList查询对应的工单数据
	 */
	public <T> List<Map<String, Object>> getOrderList(Map<String, Object> params, List<Long> flowInstIds) {
		// 根据流程实例ID查询工单数据
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.in(OrderTableInfoConstants.FLOW_INST_ID, flowInstIds);
		OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
		// TODO 处理单独表存储

		return orderCommonService.listMaps(queryWrapper);
	}

	/**
	 * 根据id查询对应的工单数据
	 */
	public <T> Object getOrderById(Map<String, Object> params) {
		Long id = MapUtil.getLong(params, OrderEntityInfoConstants.ID);
		OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
		// TODO 处理单独表存储

		return orderCommonService.getById(id);
	}

	/**
	 * 根据hiNodeList查询对应的工单数据
	 */
	public <T> List<Map<String, Object>> getHiOrderList(Map<String, Object> params, List<Long> flowInstIds) {
		// 根据流程实例ID查询工单数据
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.in(OrderTableInfoConstants.FLOW_INST_ID, flowInstIds);
		OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
		// TODO 处理单独表存储

		return orderCommonService.listMaps(queryWrapper);
	}

	/**
	 * 根据id查询对应的历史工单数据
	 */
	public <T> Object getHiOrderById(Map<String, Object> params) {
		Long id = MapUtil.getLong(params, OrderEntityInfoConstants.ID);
		OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
		// TODO 处理单独表存储

		return orderCommonService.getById(id);
	}

	//--------------------------------------以下为常用接口--------------------------------------

	/**
	 * 根据流程ID获取工单数据
	 */
	public <T> Object getOrderByFlowInstId(Map<String, Object> params) {
		OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		Long flowInstId = MapUtil.getLong(params, OrderEntityInfoConstants.FLOW_INST_ID);
		queryWrapper.eq(OrderTableInfoConstants.FLOW_INST_ID, flowInstId);
		T one = orderCommonService.getOne(queryWrapper);
		// 处理单独表存储
		if (one instanceof RunApplication) {
			runApplicationService.doTableCrudHandle((RunApplication) one, TableCrudModelEnum.SELECT);
		}
		return one;
	}

	/**
	 * 根据流程ID更新工单状态
	 *
	 * @param order  工单参数
	 * @param params 参数
	 */
	@SneakyThrows
	public <T> void updateOrderInfo(Map<String, Object> order, Map<String, Object> params) {
		if (MapUtil.isEmpty(order)) return;
		Map<String, Object> orderMap = new HashMap<>();
		order.forEach((key, val) -> {
			String column = DataBaseUtil.propertyToColumn(key);
			orderMap.put(column, val);
		});
		String isUpdateFormData = MapUtil.getStr(params, FlowCommonConstants.IS_UPDATE_FORM_DATA);
		params.remove(FlowCommonConstants.IS_UPDATE_FORM_DATA);
		OrderIdentityKeyEnum enumByKey = OrderIdentityKeyEnum.getEnumByParams(params);
		// 处理单独表存储
		if (enumByKey.getClazzVo().equals(RunApplicationVO.class) && FlowCommonConstants.YES.equals(isUpdateFormData)) {
			// 只更新配置的字段值
			Object obj = this.getOrderByFlowInstId(params);
			this.buildFormData((RunApplication) obj, orderMap);
			runApplicationService.doTableCrudHandle((RunApplication) obj, TableCrudModelEnum.UPDATE);
		} else {
			OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
			UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
			Long flowInstId = MapUtil.getLong(params, OrderEntityInfoConstants.FLOW_INST_ID);
			updateWrapper.eq(OrderTableInfoConstants.FLOW_INST_ID, flowInstId);
			orderMap.forEach(updateWrapper::set);
			orderCommonService.update(updateWrapper);
		}

		//-----------------------公共接口处理不同工单逻辑----------------------
		// 更新工作交接附表状态 TODO 已配置在全局监听事件HandoverFlowEndListener
		/*String flowKey = MapUtil.getStr(params, OrderEntityInfoConstants.FLOW_KEY);
		if (OrderIdentityKeyEnum.HANDOVER_FLOW.getKey().equals(flowKey)) {
			Object obj = this.getOrderByFlowInstId(params);
			handoverFlowService.updateRecordStatus(BeanUtil.beanToMap(obj));
		}*/
	}

	/**
	 * 保存工单并启动流程
	 *
	 * @param order  工单参数
	 * @param params  参数
	 */
	@SneakyThrows
	public <T> Object startSubFlow(Map<String, Object> order, Map<String, Object> params) {
		return this.saveOrUpdateFlowForm(order, params, true, false);
	}

	@SneakyThrows
	private <T> Object saveOrUpdateFlowForm(Map<String, Object> order, Map<String, Object> params, boolean start, boolean restart) {
		OrderIdentityKeyEnum enumByKey = OrderIdentityKeyEnum.getEnumByParams(params);
		OrderCommonService<T> orderCommonService = this.getOrderCommonService(params);
		Object objVo;
		if (enumByKey.getClazzVo().equals(RunApplicationVO.class)) {
			// 只更新配置的字段值
			Object obj = this.getOrderByFlowInstId(params);
			if (Objects.nonNull(obj)) {
				if (MapUtil.isNotEmpty(order)) this.buildFormData((RunApplication) obj, order);
				objVo = new RunApplicationVO();
				BeanUtil.copyProperties(obj, objVo);
			} else {
				objVo = JSONUtil.toBean(JSONUtil.toJsonStr(params), enumByKey.getClazzVo());
				if (MapUtil.isNotEmpty(order)) {
					Map<String, Object> formData = new HashMap<>();
					BeanUtil.copyProperties(order, formData);
					((RunApplicationVO) objVo).setFormData(objectMapper.writeValueAsString(formData));
				}
			}
		} else {
			if (MapUtil.isNotEmpty(order)) params.putAll(order);
			objVo = JSONUtil.toBean(JSONUtil.toJsonStr(params), enumByKey.getClazzVo());
		}
		if (start) return orderCommonService.startSubFlow(objVo);
		else if (restart) return orderCommonService.restartSubFlow(objVo);
		else return orderCommonService.backParFlow(objVo);
	}

	@SneakyThrows
	private void buildFormData(RunApplication obj, Map<String, Object> order) {
		Map<String, Object> formData = new HashMap<>();
		String formDataStr = obj.getFormData();
		if (StrUtil.isBlank(formDataStr)) {
			BeanUtil.copyProperties(order, formData);
		} else {
			formData = JSONUtil.parseObj(formDataStr);
			formData.putAll(order);
		}
		obj.setFormData(objectMapper.writeValueAsString(formData));
	}

	/**
	 * 子流程返回父流程，并更新关联数据
	 *
	 * @param order  工单参数
	 * @param params  参数
	 */
	@SneakyThrows
	public <T> Boolean backParFlow(Map<String, Object> order, Map<String, Object> params) {
		this.saveOrUpdateFlowForm(order, params, false, false);
		return Boolean.TRUE;
	}

	/**
	 * 重入或重启子流程，更新关联数据
	 *
	 * @param order  工单参数
	 * @param params  参数
	 */
	@SneakyThrows
	public <T> Boolean restartSubFlow(Map<String, Object> order, Map<String, Object> params) {
		this.saveOrUpdateFlowForm(order, params, false, true);
		return Boolean.TRUE;
	}

}
