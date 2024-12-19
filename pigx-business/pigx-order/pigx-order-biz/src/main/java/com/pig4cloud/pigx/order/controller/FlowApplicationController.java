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
package com.pig4cloud.pigx.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.order.api.entity.FlowApplication;
import com.pig4cloud.pigx.order.api.vo.FlowApplicationVO;
import com.pig4cloud.pigx.order.service.FlowApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单申请
 *
 * @author luolin
 * @date 2022-09-17 00:58:32
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/flow-application")
@Tag(description = "flow-application", name = "工单申请管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FlowApplicationController {

	private final FlowApplicationService flowApplicationService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param flowApplication 工单申请
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page,@ModelAttribute FlowApplication flowApplication,@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<FlowApplication> query = Wrappers.query(flowApplication);
		QueryWrapperUtils.queryTime(query,FlowApplication::getCreateTime,queryTime);

		return R.ok(flowApplicationService.page(page, query));
	}

	/**
	 * 通过id查询工单申请
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(flowApplicationService.getById(id));
	}

	/**
	 * 新增工单申请
	 * @param flowApplicationVO 工单申请
	 * @return R
	 */
	@Operation(summary = "新增工单申请", description = "新增工单申请")
	@PostMapping
	public R save(@Validated @RequestBody FlowApplicationVO flowApplicationVO) {
		return R.ok(flowApplicationService.saveOrUpdate(flowApplicationVO));
	}

	/**
	 * 暂存工单申请
	 * @param flowApplicationVO 工单申请
	 * @return R
	 */
	@Operation(summary = "暂存工单申请", description = "暂存工单申请")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody FlowApplicationVO flowApplicationVO) {
		return R.ok(flowApplicationService.saveOrUpdate(flowApplicationVO));
	}

	/**
	 * 修改工单申请
	 * @param flowApplication 工单申请
	 * @return R
	 */
	@Operation(summary = "修改工单申请", description = "修改工单申请")
	@PutMapping
	public R updateById(@RequestBody FlowApplication flowApplication) {
		flowApplication.setUpdateUser(SecurityUtils.getUser().getId());
		flowApplication.setUpdateTime(LocalDateTime.now());
		return R.ok(flowApplicationService.updateById(flowApplication));
	}

	/**
	 * 通过id删除工单申请
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除工单申请", description = "通过id删除工单申请")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(flowApplicationService.removeById(id));
	}

	/**
	 * 查询租户所有工单申请
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(flowApplicationService.list());
	}

	/**
	 * 查询租户所有工单申请
	 * @return R
	 */
	@GetMapping("/list/perm")
	public R listByPerms(@ModelAttribute FlowApplication flowApplication){
		return R.ok(flowApplicationService.listByPerms(flowApplication));
	}

	/**
	 * 查询工单分组名称
	 * @return R
	 */
	@GetMapping("/list/group-name")
	public R listGroupName() {
		return R.ok(flowApplicationService
				.list(Wrappers.<FlowApplication>lambdaQuery().select(FlowApplication::getGroupName)
						.isNotNull(FlowApplication::getGroupName).groupBy(FlowApplication::getGroupName)));
	}

	/**
	 * 根据流程定义查询流程表单信息
	 * @return R
	 */
	@GetMapping("/list/def-flow-id/{defFlowId}")
	public R listByDefFlowId(@PathVariable Long defFlowId){
		return R.ok(flowApplicationService.list(Wrappers.<FlowApplication>lambdaQuery().eq(FlowApplication::getDefFlowId, defFlowId)));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(flowApplicationService.removeByIds(ids));
	}

	/**
	 * 查询租户所有工单申请
	 * @return R
	 */
	@PostMapping("/list/ids")
	public R listByIds(@RequestBody List<Long> ids){
		return R.ok(flowApplicationService.listByIds(ids));
	}

}
