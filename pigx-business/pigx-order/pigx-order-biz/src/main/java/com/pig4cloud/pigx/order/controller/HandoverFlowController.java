package com.pig4cloud.pigx.order.controller;

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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.order.api.entity.HandoverFlow;
import com.pig4cloud.pigx.order.api.vo.HandoverFlowVO;
import com.pig4cloud.pigx.order.service.HandoverFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 交接流程
 *
 * @author luolin
 * @date 2020-04-26 10:35:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/handover-flow")
@Tag(description = "handover-flow", name = "交接流程管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class HandoverFlowController {

	private final HandoverFlowService handoverFlowService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param handoverFlow 交接流程
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute HandoverFlow handoverFlow,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<HandoverFlow> query = Wrappers.query(handoverFlow);
		QueryWrapperUtils.userQueryTime(query, HandoverFlow::getCreateTime, queryTime, HandoverFlow::getCreateUser,
				SecurityUtils.getUser().getId());

		return R.ok(handoverFlowService.page(page, query));
	}

	/**
	 * 通过id查询交接流程
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(handoverFlowService.getById(id));
	}

	/**
	 * 新增交接流程
	 * @param handoverFlowVO 交接流程
	 * @return R
	 */
	@Operation(summary = "新增交接流程", description = "新增交接流程")
	@PostMapping
	public R save(@RequestBody HandoverFlowVO handoverFlowVO) {
		return R.ok(handoverFlowService.saveOrUpdate(handoverFlowVO));
	}

	/**
	 * 暂存交接流程
	 * @param handoverFlowVO 交接流程
	 * @return R
	 */
	@Operation(summary = "暂存交接流程", description = "暂存交接流程")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody HandoverFlowVO handoverFlowVO) {
		return R.ok(handoverFlowService.saveOrUpdate(handoverFlowVO));
	}

	/**
	 * 修改交接流程
	 * @param handoverFlowVO 交接流程
	 * @return R
	 */
	@Operation(summary = "修改交接流程", description = "修改交接流程")
	@PutMapping
	public R updateById(@RequestBody HandoverFlowVO handoverFlowVO) {
		return R.ok(handoverFlowService.updateOrder(handoverFlowVO));
	}

	/**
	 * 通过id删除交接流程
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除交接流程", description = "通过id删除交接流程")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(handoverFlowService.removeOrder(id));
	}

	/**
	 * 查询租户所有交接流程
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(handoverFlowService.list());
	}

	/**
	 * 驳回选中项
	 * @param handoverFlowVO 交接流程
	 * @return R
	 */
	@Operation(summary = "驳回选中项", description = "驳回选中项")
	@PostMapping("/check/to-reject")
	public R checkToReject(@RequestBody HandoverFlowVO handoverFlowVO) {
		return R.ok(handoverFlowService.checkToReject(handoverFlowVO));
	}

	/**
	 * 接收人确认接收
	 * @param handoverFlowVO 交接流程
	 * @return R
	 */
	@Operation(summary = "交接流程", description = "交接流程")
	@PostMapping("/confirm/receive")
	public R confirmReceive(@RequestBody HandoverFlowVO handoverFlowVO) {
		return R.ok(handoverFlowService.confirmReceive(handoverFlowVO));
	}

	/**
	 * 分配参与者
	 * @param handoverFlowVO 交接流程
	 * @return R
	 */
	@Operation(summary = "分配参与者", description = "分配参与者")
	@PutMapping("/distribute")
	public R distributePerson(@RequestBody HandoverFlowVO handoverFlowVO) {
		return R.ok(handoverFlowService.distributePerson(handoverFlowVO));
	}

	/**
	 * 作废时更新工作交接任务状态
	 * @param order 工单参数
	 * @return R
	 */
	@Operation(summary = "作废时更新工作交接任务状态", description = "作废时更新工作交接任务状态")
	@PutMapping("/invalid/status")
	public R updateRecordStatus(@RequestParam Map<String, Object> order) {
		return R.ok(handoverFlowService.updateRecordStatus(order));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(handoverFlowService.removeOrder(ids));
	}

}
