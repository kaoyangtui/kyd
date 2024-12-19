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

import cn.hutool.core.bean.DynaBean;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 工单业务公共接口封装
 *
 * @author luolin
 * @date 2020/2/10 10:45
 */
public interface OrderCommonService<T> extends IService<T> {

	/**
	 * 保存工单并启动流程
	 *
	 * @param order  工单参数
	 */
	<Vo> Vo startSubFlow(Vo order);

	/**
	 * 子流程返回父流程，并更新关联数据
	 *
	 * @param order  工单参数
	 */
	<Vo> Boolean backParFlow(Vo order);

	/**
	 * 重入或重启子流程，更新关联数据
	 *
	 * @param order  工单参数
	 */
	<Vo> Boolean restartSubFlow(Vo order);

	/**
	 * 保存工单
	 *
	 * @param params 参数
	 * @param order  工单参数
	 */
	Boolean saveOrUpdateOrder(Map<String, Object> params, T order);

	/**
	 * 暂存工单公共接口
	 *
	 * @param order  工单参数
	 */
	Boolean tempStore(T order);

	/**
	 * 修改工单公共接口
	 *
	 * @param params 参数
	 * @param order  工单参数
	 */
	Boolean updateOrder(Map<String, Object> params, T order);

	/**
	 * 设置工单信息公共接口
	 *
	 * @param order  工单参数
	 * @param bean   反射Bean
	 */
	void setOrderInfo(T order, DynaBean bean);

	/**
	 * 构造流程公共参数
	 *
	 * @param params 参数
	 * @param bean   反射Bean
	 */
	Boolean buildFlowInfo(Map<String, Object> params, DynaBean bean);

	/**
	 * 启动流程公共接口
	 *
	 * @param params 参数
	 * @param vo     vo
	 * @param topic  主题
	 */
	<Vo> void startFlow(Map<String, Object> params, Vo vo, String topic);

	/**
	 * 流程中只保存工单
	 *
	 * @param params 参数
	 * @param order 工单
	 */
	Boolean saveByFlowInstId(Map<String, Object> params, T order);

	/**
	 * 更新流程参数
	 *
	 * @param params 参数
	 * @param vo     vo
	 */
	<Vo> Boolean updateFlowVariables(Map<String, Object> params, Vo vo);

	/**
	 * 更新Vo信息
	 *
	 * @param order 工单参数
	 * @param vo    vo
	 */
	<Vo> Boolean updateOrderVOInfo(T order, Vo vo);

	/**
	 * 构建流程公共参数
	 *
	 * @param params 参数
	 * @param vo    vo
	 */
	<Vo> void buildFlowVariables(Map<String, Object> params, Vo vo);

}
