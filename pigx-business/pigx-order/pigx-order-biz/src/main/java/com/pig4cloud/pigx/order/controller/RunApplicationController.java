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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.xss.core.XssCleanIgnore;
import com.pig4cloud.pigx.order.api.entity.RunApplication;
import com.pig4cloud.pigx.order.api.vo.RunApplicationVO;
import com.pig4cloud.pigx.order.service.RunApplicationService;
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

import java.util.List;

/**
 * 我的工单
 *
 * @author luolin
 * @date 2022-09-17 00:58:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/run-application")
@Tag(description = "run-application", name = "我的工单管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RunApplicationController {

	private final RunApplicationService runApplicationService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param runApplication 我的工单
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute RunApplication runApplication,
			@RequestParam(required = false) String[] queryTime) {
		return R.ok(runApplicationService.getPage(page, runApplication, queryTime));
	}

	/**
	 * 通过id查询我的工单
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(runApplicationService.getOrderById(id));
	}

	/**
	 * 通过流程实例id查询我的工单
	 * @param flowInstId id
	 * @return R
	 */
	@Operation(summary = "通过流程实例id查询我的工单", description = "通过流程实例id查询我的工单")
	@GetMapping("/flow-inst-id/{flowInstId}")
	public R getByFlowInstId(@PathVariable("flowInstId") Long flowInstId) {
		return R.ok(runApplicationService.getByFlowInstId(flowInstId));
	}

	/**
	 * 新增我的工单
	 * @param runApplicationVO 我的工单
	 * @return R
	 */
	@Operation(summary = "新增我的工单", description = "新增我的工单")
	@PostMapping
	@XssCleanIgnore
	public R save(@Validated @RequestBody RunApplicationVO runApplicationVO) {
		return R.ok(runApplicationService.saveOrUpdate(runApplicationVO));
	}

	/**
	 * 暂存我的工单
	 * @param runApplicationVO 我的工单
	 * @return R
	 */
	@Operation(summary = "暂存我的工单", description = "暂存我的工单")
	@PostMapping("/temp-store")
	@XssCleanIgnore
	public R tempStore(@Validated @RequestBody RunApplicationVO runApplicationVO) {
		return R.ok(runApplicationService.tempStore(runApplicationVO));
	}

	/**
	 * 修改我的工单
	 * @param runApplicationVO 我的工单
	 * @return R
	 */
	@Operation(summary = "修改我的工单", description = "修改我的工单")
	@PutMapping
	@XssCleanIgnore
	public R updateById(@Validated @RequestBody RunApplicationVO runApplicationVO) {
		return R.ok(runApplicationService.updateOrder(runApplicationVO));
	}

	/**
	 * 通过id删除我的工单
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除我的工单", description = "通过id删除我的工单")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(runApplicationService.removeOrder(id));
	}

	/**
	 * 查询租户所有我的工单
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(runApplicationService.list());
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(runApplicationService.removeByIds(ids));
	}

}
