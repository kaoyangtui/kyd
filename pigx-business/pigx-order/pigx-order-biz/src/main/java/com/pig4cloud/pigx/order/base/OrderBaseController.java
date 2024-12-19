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

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.order.support.FlowCommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 工单业务公共Controller封装
 *
 * @author luolin
 * @date 2020/2/10 14:51
 */
@RestController
@RequestMapping("/order-base")
public class OrderBaseController {

	@Autowired
	private FlowCommonServiceImpl flowCommonService;

	/**
	 * 获取工单数据
	 *
	 * @param params 参数
	 * @param flowInstIds 流程实例ID集合
	 * @return
	 */
	@PostMapping("/get/order-list")
	public R<List<Map<String, Object>>> getOrderList(@RequestParam Map<String, Object> params, @RequestBody List<Long> flowInstIds) {
		return R.ok(flowCommonService.getOrderList(params, flowInstIds));
	}

	/**
	 * 获取单个工单数据
	 *
	 * @param params 参数
	 * @return
	 */
	@GetMapping("/get/order-id")
	public R<Object> getOrderById(@RequestParam Map<String, Object> params) {
		return R.ok(flowCommonService.getOrderById(params));
	}

	/**
	 * 获取历史工单数据
	 *
	 * @param params 参数
	 * @param flowInstIds 流程实例ID集合
	 * @return
	 */
	@PostMapping("/get/hi-order-list")
	public R<List<Map<String, Object>>> getHiOrderList(@RequestParam Map<String, Object> params, @RequestBody List<Long> flowInstIds) {
		return R.ok(flowCommonService.getHiOrderList(params, flowInstIds));
	}

	/**
	 * 获取单个历史工单数据
	 *
	 * @param params 参数
	 * @return
	 */
	@GetMapping("/get/hi-order-id")
	public R<Object> getHiOrderById(@RequestParam Map<String, Object> params) {
		return R.ok(flowCommonService.getHiOrderById(params));
	}

	/**
	 * 根据流程ID获取工单数据
	 *
	 * @param params 参数
	 * @return
	 */
	@GetMapping("/order/flow-inst-id")
	public R getOrderByFlowInstId(@RequestParam Map<String, Object> params) {
		return R.ok(flowCommonService.getOrderByFlowInstId(params));
	}

	/**
	 * 保存工单并启动子流程
	 *
	 * @param order 工单数据
	 * @param params 参数
	 * @return R
	 */
	@PostMapping("/start-sub-flow")
	public R startSubFlow(@RequestBody Map<String, Object> order, @RequestParam Map<String, Object> params) {
		return R.ok(flowCommonService.startSubFlow(order, params));
	}

	/**
	 * 子流程返回父流程，并更新关联数据
	 *
	 * @param order 工单数据
	 * @param params 参数
	 * @return R
	 */
	@PutMapping("/back-par-flow")
	public R backParFlow(@RequestBody Map<String, Object> order, @RequestParam Map<String, Object> params){
		return R.ok(flowCommonService.backParFlow(order, params));
	}

	/**
	 * 重入或重启子流程，更新关联数据
	 *
	 * @param order 工单数据
	 * @param params 参数
	 * @return R
	 */
	@PutMapping("/restart-sub-flow")
	public R restartSubFlow(@RequestBody Map<String, Object> order, @RequestParam Map<String, Object> params) {
		return R.ok(flowCommonService.restartSubFlow(order, params));
	}

	/**
	 * 更新工单信息接口
	 *
	 * @param order 工单数据
	 * @param params 参数
	 * @return R
	 */
	@PutMapping("/update-order-info")
	public R updateOrderInfo(@RequestBody Map<String, Object> order, @RequestParam Map<String, Object> params) {
		flowCommonService.updateOrderInfo(order, params);
		return R.ok();
	}

}
