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

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.entity.TabsOption;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.TabsOptionVO;
import com.pig4cloud.pigx.jsonflow.service.TabsOptionService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程中tabs页面
 *
 * @author luolin
 * @date 2021-02-23 14:12:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/tabs-option")
@Tag(description = "tabs-option", name = "流程中tabs页面管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class TabsOptionController {

	private final TabsOptionService tabsOptionService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param tabsOption 流程中tabs页面
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute TabsOption tabsOption,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<TabsOption> query = Wrappers.query(tabsOption);
		QueryWrapperUtils.queryTime(query, TabsOption::getCreateTime, queryTime);

		return R.ok(tabsOptionService.page(page, query));
	}

	/**
	 * 通过id查询流程中tabs页面
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(tabsOptionService.getById(id));
	}

	/**
	 * 新增流程中tabs页面
	 * @param tabsOptionVO 流程中tabs页面
	 * @return R
	 */
	@Operation(summary = "新增流程中tabs页面", description = "新增流程中tabs页面")
	@PostMapping
	public R save(@Validated @RequestBody TabsOptionVO tabsOptionVO) {
		TabsOption tabsOption = new TabsOption();
		BeanUtil.copyProperties(tabsOptionVO, tabsOption);
		tabsOptionVO.setCreateUser(SecurityUtils.getUser().getId());
		tabsOptionService.saveOrUpdate(tabsOption);
		return R.ok(tabsOption);
	}

	/**
	 * 暂存流程中tabs页面
	 * @param tabsOption 流程中tabs页面
	 * @return R
	 */
	@Operation(summary = "暂存流程中tabs页面", description = "暂存流程中tabs页面")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody TabsOption tabsOption) {
		tabsOption.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(tabsOptionService.saveOrUpdate(tabsOption));
	}

	/**
	 * 修改流程中tabs页面
	 * @param tabsOption 流程中tabs页面
	 * @return R
	 */
	@Operation(summary = "修改流程中tabs页面", description = "修改流程中tabs页面")
	@PutMapping
	public R updateById(@RequestBody TabsOption tabsOption) {
		tabsOption.setUpdateUser(SecurityUtils.getUser().getId());
		tabsOption.setUpdateTime(LocalDateTime.now());
		return R.ok(tabsOptionService.updateById(tabsOption));
	}

	/**
	 * 通过id删除流程中tabs页面
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除流程中tabs页面", description = "通过id删除流程中tabs页面")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(tabsOptionService.removeById(id));
	}

	/**
	 * 查询租户所有流程中tabs页面
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(tabsOptionService.list());
	}

	/**
	 * 根据页面ID设置数据
	 * @return R
	 */
	@PostMapping("/list/ids")
	public R listByIds(@RequestBody List<Long> pcTodoUrls) {
		return R.ok(tabsOptionService.listByIds(pcTodoUrls));
	}

	/**
	 * 查询审批页面分组名称
	 * @return R
	 */
	@GetMapping("/list/group-name")
	public R listGroupName() {
		return R.ok(tabsOptionService.list(Wrappers.<TabsOption>lambdaQuery().select(TabsOption::getGroupName)
				.isNotNull(TabsOption::getGroupName).groupBy(TabsOption::getGroupName)));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(tabsOptionService.removeByIds(ids));
	}

}
