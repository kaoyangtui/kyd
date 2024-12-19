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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.order.api.entity.AskLeave;
import com.pig4cloud.pigx.order.api.vo.AskLeaveVO;
import com.pig4cloud.pigx.order.service.AskLeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
 * 请假工单
 *
 * @author luolin
 * @date 2020-08-25 10:55:19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ask-leave")
@Tag(description = "ask-leave", name = "请假工单管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AskLeaveController {

	private final AskLeaveService askLeaveService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param askLeave 请假工单
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute AskLeave askLeave, @RequestParam(required = false) String[] queryTime) {
		return R.ok(askLeaveService.getPage(page, askLeave, queryTime));
	}

	/**
	 * 通过id查询请假工单
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(askLeaveService.getById(id));
	}

	/**
	 * 新增请假工单
	 * @param askLeaveVO 请假工单
	 * @return R
	 */
	@Operation(summary = "新增请假工单", description = "新增请假工单")
	@PostMapping
	public R save(@RequestBody @Validated AskLeaveVO askLeaveVO) {
		return R.ok(askLeaveService.saveOrUpdate(askLeaveVO));
	}

	/**
	 * 暂存请假工单
	 * @param askLeaveVO 请假工单
	 * @return R
	 */
	@Operation(summary = "暂存请假工单", description = "暂存请假工单")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody AskLeaveVO askLeaveVO) {
		return R.ok(askLeaveService.tempStore(askLeaveVO));
	}

	/**
	 * 修改请假工单
	 * @param askLeaveVO 请假工单
	 * @return R
	 */
	@Operation(summary = "修改请假工单", description = "修改请假工单")
	@PutMapping
	public R updateById(@RequestBody AskLeaveVO askLeaveVO) {
		return R.ok(askLeaveService.updateOrder(askLeaveVO));
	}

	/**
	 * 通过id删除请假工单
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除请假工单", description = "通过id删除请假工单")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(askLeaveService.removeById(id));
	}

	/**
	 * 查询租户所有请假工单
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(askLeaveService.list());
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(askLeaveService.removeByIds(ids));
	}

}
