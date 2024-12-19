package com.pig4cloud.pigx.jsonflow.controller;

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
import com.pig4cloud.pigx.jsonflow.api.entity.RunReject;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.service.RunRejectService;
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

/**
 * 驳回记录
 *
 * @author luolin
 * @date 2021-03-05 11:56:29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/run-reject")
@Tag(description = "run-reject", name = "驳回记录管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RunRejectController {

	private final RunRejectService runRejectService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param runReject 驳回记录
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute RunReject runReject,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<RunReject> query = Wrappers.query(runReject);
		QueryWrapperUtils.queryTime(query, RunReject::getCreateTime, queryTime);

		return R.ok(runRejectService.page(page, query));
	}

	/**
	 * 通过id查询驳回记录
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(runRejectService.getById(id));
	}

	/**
	 * 新增驳回记录
	 * @param runReject 驳回记录
	 * @return R
	 */
	@Operation(summary = "新增驳回记录", description = "新增驳回记录")
	@PostMapping
	public R save(@RequestBody RunReject runReject) {
		runReject.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runRejectService.save(runReject));
	}

	/**
	 * 暂存驳回记录
	 * @param runReject 驳回记录
	 * @return R
	 */
	@Operation(summary = "暂存驳回记录", description = "暂存驳回记录")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody RunReject runReject) {
		runReject.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runRejectService.saveOrUpdate(runReject));
	}

	/**
	 * 修改驳回记录
	 * @param runReject 驳回记录
	 * @return R
	 */
	@Operation(summary = "修改驳回记录", description = "修改驳回记录")
	@PutMapping
	public R updateById(@RequestBody RunReject runReject) {
		return R.ok(runRejectService.updateById(runReject));
	}

	/**
	 * 通过id删除驳回记录
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除驳回记录", description = "通过id删除驳回记录")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(runRejectService.removeById(id));
	}

	/**
	 * 查询租户所有驳回记录
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(runRejectService.list());
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(runRejectService.removeByIds(ids));
	}

}
