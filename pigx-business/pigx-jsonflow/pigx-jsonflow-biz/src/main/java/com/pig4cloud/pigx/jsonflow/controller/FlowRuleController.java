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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 条件/人员规则
 *
 * @author luolin
 * @date 2024-03-14 22:51:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/flow-rule" )
@Tag(description = "flow-rule", name = "条件/人员规则管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FlowRuleController {

	private final FlowRuleService flowRuleService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param flowRule 条件/人员规则
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page,@ModelAttribute FlowRule flowRule,@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<FlowRule> query = Wrappers.query(flowRule);
		QueryWrapperUtils.userQueryTime(query,FlowRule::getCreateTime,queryTime
				, FlowRule::getCreateUser, SecurityUtils.getUser().getId());

		return R.ok(flowRuleService.page(page, query));
	}

	/**
	 * 通过id查询条件/人员规则
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(flowRuleService.getById(id));
	}

	/**
	 * 新增条件/人员规则
	 * @param flowRule 条件/人员规则
	 * @return R
	 */
	@Operation(summary = "新增条件/人员规则", description = "新增条件/人员规则")
	@PostMapping
	public R save(@RequestBody FlowRule flowRule) {
		flowRule.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowRuleService.save(flowRule));
	}

	/**
	 * 暂存条件/人员规则
	 * @param flowRule 条件/人员规则
	 * @return R
	 */
	@Operation(summary = "暂存条件/人员规则", description = "暂存条件/人员规则")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody FlowRule flowRule) {
		flowRule.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowRuleService.saveOrUpdate(flowRule));
	}

	/**
	 * 修改条件/人员规则
	 * @param flowRule 条件/人员规则
	 * @return R
	 */
	@Operation(summary = "修改条件/人员规则", description = "修改条件/人员规则")
	@PutMapping
	public R updateById(@RequestBody FlowRule flowRule) {
		flowRule.setUpdateUser(SecurityUtils.getUser().getId());
		flowRule.setUpdateTime(LocalDateTime.now());
		return R.ok(flowRuleService.updateById(flowRule));
	}

	/**
	 * 通过id删除条件/人员规则
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除条件/人员规则", description = "通过id删除条件/人员规则")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(flowRuleService.removeById(id));
	}

	/**
	 * 查询租户所有条件/人员规则
	 * @return R
	 */
	@GetMapping("/list")
	public R list(){
		return R.ok(flowRuleService.list());
	}

	/**
	 * 通过id删除条件/人员规则
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除条件/人员规则", description = "通过ids删除条件/人员规则")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(flowRuleService.removeByIds(ids));
	}

}
