package com.pig4cloud.pigx.order.base;

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
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.order.OrderStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.feign.RemoteFlowVariableService;
import com.pig4cloud.pigx.jsonflow.api.feign.RemoteRunFlowService;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import com.pig4cloud.pigx.order.api.constant.OrderTableInfoConstants;
import com.pig4cloud.pigx.order.api.constant.enums.OrderIdentityKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 工单业务公共接口实现类
 *
 * @author luolin
 * @date 2020/2/10 10:09
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrderCommonServiceImpl<M extends PigxBaseMapper<T>, T> extends ServiceImpl<M, T> implements OrderCommonService<T> {

	@Autowired
	private RemoteRunFlowService remoteRunFlowService;

	@Autowired
	private RemoteFlowVariableService remoteFlowVariableService;

	@Autowired
	private IdentifierGenerator idGenerator;

	@Override
	public <Vo> Vo startSubFlow(Vo order) {
		throw new ValidationException("开启子流程流程标识KEY不存在");
	}

	@Override
	public <Vo> Boolean backParFlow(Vo order) {
		throw new ValidationException("子流程返回父流程流程标识KEY不存在");
	}

	@Override
	public <Vo> Boolean restartSubFlow(Vo order) {
		throw new ValidationException("重启子流程流程标识KEY不存在");
	}

	@Override
	public Boolean saveOrUpdateOrder(Map<String, Object> params, T order) {
		// 工单公共处理
		DynaBean bean = DynaBean.create(order);
		Long id = bean.get(OrderEntityInfoConstants.ID);
		String code = bean.get(OrderEntityInfoConstants.CODE);
		if (ObjectUtil.isEmpty(id) || StrUtil.isEmpty(code)) this.setOrderInfo(order, bean);
		// 单独处理暂存
		String status = bean.get(OrderEntityInfoConstants.STATUS);
		if (OrderStatusEnum.TEMP.getStatus().equals(status)) {
			bean.set(OrderEntityInfoConstants.STATUS, OrderStatusEnum.RUN.getStatus());
		}
		// 构建流程参数
		this.buildFlowInfo(params, bean);
		// 保存工单
		this.saveOrUpdate(order);
		return true;
	}

	@Override
	public Boolean tempStore(T order) {
		// 工单公共处理
		DynaBean bean = DynaBean.create(order);
		String status = bean.get(OrderEntityInfoConstants.STATUS);
		if (StrUtil.isEmpty(status)) {
			bean.set(OrderEntityInfoConstants.STATUS, OrderStatusEnum.TEMP.getStatus());
		}
		// 初始化
		Long id = bean.get(OrderEntityInfoConstants.ID);
		String code = bean.get(OrderEntityInfoConstants.CODE);
		if (ObjectUtil.isEmpty(id) || StrUtil.isEmpty(code)) this.setOrderInfo(order, bean);
		this.saveOrUpdate(order);
		return true;
	}

	@Override
	public Boolean updateOrder(Map<String, Object> params, T order) {
		return this.updateById(order);
	}

	@Override
	public void setOrderInfo(T order, DynaBean bean) {
		Long id = bean.get(OrderEntityInfoConstants.ID);
		if (ObjectUtil.isEmpty(id)) {
			bean.set(OrderEntityInfoConstants.ID, idGenerator.nextId(order).longValue());
		}
		String code = OrderIdentityKeyEnum.getEnumByKey(order.getClass().getSimpleName()).getCode();
		String format = String.format("%s-%s-%05d", code, LocalDateTimeUtil.format(LocalDate.now(), DatePattern.PURE_DATE_PATTERN),
				RandomUtil.randomInt(1, 1000));
		bean.set(OrderEntityInfoConstants.CODE, format);
		// 业务key不存在，默认为自身工单
		String flowKey = bean.get(OrderEntityInfoConstants.FLOW_KEY);
		if (StrUtil.isEmpty(flowKey)) {
			bean.set(OrderEntityInfoConstants.FLOW_KEY, order.getClass().getSimpleName());
		}
		String status = bean.get(OrderEntityInfoConstants.STATUS);
		if (StrUtil.isEmpty(status)) {
			bean.set(OrderEntityInfoConstants.STATUS, OrderStatusEnum.RUN.getStatus());
		}
	}

	@Override
	public Boolean buildFlowInfo(Map<String, Object> params, DynaBean bean) {
		// 单独处理暂存
		Long flowInstId = bean.get(OrderEntityInfoConstants.FLOW_INST_ID);
		if (Objects.isNull(flowInstId))
			bean.set(OrderEntityInfoConstants.FLOW_INST_ID, idGenerator.nextId(null).longValue());
		// 公共流程参数封装
		params.put(OrderEntityInfoConstants.CODE, bean.get(OrderEntityInfoConstants.CODE));
		params.put(OrderEntityInfoConstants.FLOW_INST_ID, bean.get(OrderEntityInfoConstants.FLOW_INST_ID));
		// 判断是否存在表单ID
		if(bean.containsProp(OrderEntityInfoConstants.FORM_ID)) {
			params.put(OrderEntityInfoConstants.FORM_ID, bean.get(OrderEntityInfoConstants.FORM_ID));
		}
		// 流程KEY不存在，默认为自身工单
		String flowKey = MapUtil.getStr(params, OrderEntityInfoConstants.FLOW_KEY);
		if (StrUtil.isEmpty(flowKey)) {
			params.put(OrderEntityInfoConstants.FLOW_KEY, bean.get(OrderEntityInfoConstants.FLOW_KEY));
		}
		params.put(OrderEntityInfoConstants.CREATE_USER, SecurityUtils.getUser().getId());
		return true;
	}

	@Override
	public <Vo> Boolean updateOrderVOInfo(T order, Vo vo) {
		DynaBean bean = DynaBean.create(order);
		DynaBean beanVo = DynaBean.create(vo);
		beanVo.set(OrderEntityInfoConstants.ID, bean.get(OrderEntityInfoConstants.ID));
		beanVo.set(OrderEntityInfoConstants.CODE, bean.get(OrderEntityInfoConstants.CODE));
		beanVo.set(OrderEntityInfoConstants.FLOW_INST_ID, bean.get(OrderEntityInfoConstants.FLOW_INST_ID));
		beanVo.set(OrderEntityInfoConstants.FLOW_KEY, bean.get(OrderEntityInfoConstants.FLOW_KEY));
		beanVo.set(OrderEntityInfoConstants.STATUS, bean.get(OrderEntityInfoConstants.STATUS));
		return Boolean.TRUE;
	}

	public <Vo> void startFlow(Map<String, Object> params, Vo vo, String topic) {
		if (Objects.isNull(params)) params = new HashMap<>();
		Long id = MapUtil.getLong(params, OrderEntityInfoConstants.ID);
		Long flowInstId = MapUtil.getLong(params, OrderEntityInfoConstants.FLOW_INST_ID);
		// 直接发起，不采用MQ
		Map<String, Object> order = new HashMap<>();
		BeanUtil.copyProperties(vo, order);
		R<Boolean> res = remoteRunFlowService.startFlow(order, params);
		if (!FlowCommonConstants.SUCCESS.equals(res.getCode())) {
			throw new ValidationException("发起流程失败: " + res.getMsg());
		}
		log.info("工单消息:Topic:{},Keys:{},流程ID:{}", topic, id, flowInstId);
	}

	@Override
	public Boolean saveByFlowInstId(Map<String, Object> params, T order) {
		// 工单公共处理
		DynaBean bean = DynaBean.create(order);
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		Long flowInstId = bean.get(OrderEntityInfoConstants.FLOW_INST_ID);
		queryWrapper.eq(OrderTableInfoConstants.FLOW_INST_ID, flowInstId);
		Map<String, Object> map = this.getMap(queryWrapper);
		if (Objects.nonNull(map)) {
			bean.set(OrderEntityInfoConstants.ID, map.get(OrderEntityInfoConstants.ID));
		}
		// 是否已存在
		Long id = bean.get(OrderEntityInfoConstants.ID);
		if (ObjectUtil.isEmpty(id)) this.setOrderInfo(order, bean);

		return this.saveOrUpdate(order);
	}

	@Override
	public <Vo> Boolean updateFlowVariables(Map<String, Object> params, Vo vo) {
		// 流程参数公共处理
		DynaBean bean = DynaBean.create(vo);
		params.put(OrderEntityInfoConstants.FLOW_INST_ID, bean.get(OrderEntityInfoConstants.FLOW_INST_ID));
		params.put(OrderEntityInfoConstants.CODE, bean.get(OrderEntityInfoConstants.CODE));
		params.put(OrderEntityInfoConstants.FLOW_KEY, bean.get(OrderEntityInfoConstants.FLOW_KEY));
		// 修改时记录发起人
		params.put(OrderEntityInfoConstants.CREATE_USER, bean.get(OrderEntityInfoConstants.CREATE_USER));
		R<Boolean> res = remoteFlowVariableService.updateFlowVariables(params);
		if (!FlowCommonConstants.SUCCESS.equals(res.getCode())) {
			throw new ValidationException("更新流程变量失败: " + res.getMsg());
		}
		return Boolean.TRUE;
	}

	@Override
	public <Vo> void buildFlowVariables(Map<String, Object> params, Vo vo) {
		DynaBean beanVo = DynaBean.create(vo);
		// 流程条件与人员公共参数

	}

}
