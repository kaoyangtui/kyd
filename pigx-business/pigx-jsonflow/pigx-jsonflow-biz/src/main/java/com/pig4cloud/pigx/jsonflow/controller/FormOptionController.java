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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.entity.FormOption;
import com.pig4cloud.pigx.jsonflow.api.vo.FormOptionVO;
import com.pig4cloud.pigx.jsonflow.service.FormOptionService;
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
 * 表单/页面操作表
 *
 * @author luolin
 * @date 2024-03-06 22:47:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/form-option")
@Tag(description = "form-option", name = "表单/页面操作表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FormOptionController {

	private final FormOptionService formOptionService;

	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param formOption 表单/页面操作表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute FormOption formOption, @RequestParam(required = false) String[] queryTime) {
		return R.ok(formOptionService.getPage(page, formOption, queryTime));
	}

	/**
	 * 通过id查询表单/页面操作表
	 *
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(formOptionService.getById(id));
	}

	/**
	 * 新增表单/页面操作表
	 *
	 * @param formOptionVO 表单/页面操作表
	 * @return R
	 */
	@Operation(summary = "新增表单/页面操作表", description = "新增表单/页面操作表")
	@PostMapping
	public R save(@RequestBody FormOptionVO formOptionVO) {
		formOptionVO.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(formOptionService.saveOpUpdate(formOptionVO));
	}

	/**
	 * 新增或修改打印模板
	 *
	 * @param formOptionVO 表单/页面操作表
	 * @return R
	 */
	@Operation(summary = "新增或修改打印模板", description = "新增或修改打印模板")
	@PostMapping("/print-temp")
	public R savePrintTemp(@RequestBody FormOptionVO formOptionVO) {
		formOptionVO.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(formOptionService.savePrintTemp(formOptionVO));
	}

	/**
	 * 暂存表单/页面操作表
	 *
	 * @param formOption 表单/页面操作表
	 * @return R
	 */
	@Operation(summary = "暂存表单/页面操作表", description = "暂存表单/页面操作表")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody FormOption formOption) {
		formOption.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(formOptionService.saveOrUpdate(formOption));
	}

	/**
	 * 修改表单/页面操作表
	 *
	 * @param formOption 表单/页面操作表
	 * @return R
	 */
	@Operation(summary = "修改表单/页面操作表", description = "修改表单/页面操作表")
	@PutMapping
	public R updateById(@RequestBody FormOption formOption) {
		formOption.setUpdateUser(SecurityUtils.getUser().getId());
		formOption.setUpdateTime(LocalDateTime.now());
		return R.ok(formOptionService.updateById(formOption));
	}

	/**
	 * 通过id删除表单/页面操作表
	 *
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除表单/页面操作表", description = "通过id删除表单/页面操作表")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(formOptionService.removeById(id));
	}

	/**
	 * 查询租户所有表单/页面操作表
	 *
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(formOptionService.list());
	}

	/**
	 * 通过id删除表单/页面操作表
	 *
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除表单/页面操作表", description = "通过ids删除表单/页面操作表")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(formOptionService.removeByIds(ids));
	}

	/**
	 * 通过formId查询表单/页面操作表
	 *
	 * @param formOptionVO formOptionVO
	 * @return R
	 */
	@Operation(summary = "通过formOption查询", description = "通过formOption查询")
	@GetMapping("/option")
	public R listFormOption(@ModelAttribute FormOptionVO formOptionVO) {
		return R.ok(formOptionService.listFormOption(formOptionVO));
	}

	/**
	 * 通过formId查询表单/页面操作表
	 *
	 * @param formOptionVO formOptionVO
	 * @return R
	 */
	@Operation(summary = "通过formOption查询", description = "通过formOption查询")
	@GetMapping("/start")
	public R listStartPerm(@ModelAttribute FormOptionVO formOptionVO) {
		return R.ok(formOptionService.listStartPerm(formOptionVO));
	}

	/**
	 * 通过formId查询表单/页面操作表
	 *
	 * @param formOptionVO formOptionVO
	 * @return R
	 */
	@Operation(summary = "通过formOption查询", description = "通过formOption查询")
	@GetMapping("/print-temp")
	public R listPrintTemp(@ModelAttribute FormOptionVO formOptionVO) {
		return R.ok(formOptionService.listPrintTemp(formOptionVO));
	}

}
