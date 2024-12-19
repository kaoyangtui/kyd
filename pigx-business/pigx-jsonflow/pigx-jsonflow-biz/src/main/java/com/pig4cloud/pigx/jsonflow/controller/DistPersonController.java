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
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.service.DistPersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
import java.util.Objects;

/**
 * 分配参与者
 *
 * @author luolin
 * @date 2021-02-23 14:12:22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dist-person")
@Tag(description = "dist-person", name = "分配参与者管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DistPersonController {

	private final DistPersonService distPersonService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param distPerson 分配参与者
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute DistPerson distPerson,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<DistPerson> query = Wrappers.query(distPerson);
		QueryWrapperUtils.userQueryTime(query, DistPerson::getCreateTime, queryTime, DistPerson::getCreateUser,
				SecurityUtils.getUser().getId());

		return R.ok(distPersonService.page(page, query));
	}

	/**
	 * 通过id查询分配参与者
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(distPersonService.getById(id));
	}

	/**
	 * 新增分配参与者
	 * @param distPersonVO 分配参与者
	 * @return R
	 */
	@Operation(summary = "新增分配参与者", description = "新增分配参与者")
	@PostMapping
	public R save(@RequestBody DistPersonVO distPersonVO) {
		return R.ok(distPersonService.saveOrUpdate(distPersonVO));
	}

	/**
	 * 暂存分配参与者
	 * @param distPerson 分配参与者
	 * @return R
	 */
	@Operation(summary = "暂存分配参与者", description = "暂存分配参与者")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody DistPerson distPerson) {
		distPerson.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(distPersonService.saveOrUpdate(distPerson));
	}

	/**
	 * 修改分配参与者
	 * @param distPersonVO 分配参与者
	 * @return R
	 */
	@Operation(summary = "修改分配参与者", description = "修改分配参与者")
	@PutMapping
	public R updateById(@RequestBody DistPersonVO distPersonVO) {
		return R.ok(distPersonService.saveOrUpdate(distPersonVO));
	}

	/**
	 * 通过id删除分配参与者
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除分配参与者", description = "通过id删除分配参与者")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(distPersonService.removeById(id));
	}

	/**
	 * 查询租户所有分配参与者
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(distPersonService.list());
	}

	/**
	 * 流程中通过id查询分配参与者
	 * @param distPersonVO 分配参与者
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/flow-inst-id")
	public R getByFlowInstId(@ModelAttribute DistPersonVO distPersonVO) {
		return R.ok(distPersonService.getByFlowInstId(distPersonVO));
	}

	/**
	 * 流程中保存分配参与者
	 * @param distPersonVO 分配参与者
	 * @return R
	 */
	@Operation(summary = "流程中保存分配参与者", description = "流程中保存分配参与者")
	@PutMapping("/flow-inst-id")
	public R saveByFlowInstId(@RequestBody DistPersonVO distPersonVO) {
		return R.ok(distPersonService.saveByFlowInstId(distPersonVO));
	}

	/**
	 * 增量任务动态计算保存分配参与者
	 * @param distPersonVO 分配参与者
	 * @return R
	 */
	@Operation(summary = "增量任务动态计算保存分配参与者", description = "增量任务动态计算保存分配参与者")
	@PutMapping("/one-by-one/flow-inst-id")
	public R incrementCalculate(@RequestBody DistPersonVO distPersonVO) {
		return R.ok(distPersonService.incrementCalculate(distPersonVO));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(distPersonService.removeByIds(ids));
	}

}
