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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowClazz;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowClazzVO;
import com.pig4cloud.pigx.jsonflow.service.FlowClazzService;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 节点监听事件设置
 *
 * @author luolin
 * @date 2021-03-19 11:43:00
 */
@RestController
@AllArgsConstructor
@RequestMapping("/flow-clazz")
@Tag(description = "flow-clazz", name = "节点监听事件设置管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FlowClazzController {

	private final FlowClazzService flowClazzService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param flowClazz 节点监听事件设置
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute FlowClazz flowClazz,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<FlowClazz> query = Wrappers.query(flowClazz);
		QueryWrapperUtils.queryTime(query, FlowClazz::getCreateTime, queryTime);

		return R.ok(flowClazzService.page(page, query));
	}

	/**
	 * 通过id查询节点监听事件设置
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(flowClazzService.getById(id));
	}

	/**
	 * 新增节点监听事件设置
	 * @param flowClazzVO 节点监听事件设置
	 * @return R
	 */
	@Operation(summary = "新增节点监听事件设置", description = "新增节点监听事件设置")
	@PostMapping
	public R save(@RequestBody FlowClazzVO flowClazzVO) {
		flowClazzVO.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowClazzService.saveOrUpdate(flowClazzVO));
	}

	/**
	 * 暂存节点监听事件设置
	 * @param flowClazz 节点监听事件设置
	 * @return R
	 */
	@Operation(summary = "暂存节点监听事件设置", description = "暂存节点监听事件设置")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody FlowClazz flowClazz) {
		flowClazz.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowClazzService.saveOrUpdate(flowClazz));
	}

	/**
	 * 修改节点监听事件设置
	 * @param flowClazzVO 节点监听事件设置
	 * @return R
	 */
	@Operation(summary = "修改节点监听事件设置", description = "修改节点监听事件设置")
	@PutMapping
	public R updateById(@RequestBody FlowClazzVO flowClazzVO) {
		return R.ok(flowClazzService.saveOrUpdate(flowClazzVO));
	}

	/**
	 * 通过id删除节点监听事件设置
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除节点监听事件设置", description = "通过id删除节点监听事件设置")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(flowClazzService.removeById(id));
	}

	/**
	 * 查询节点监听事件Val
	 * @return R
	 */
	@GetMapping("/list/var-val")
	public R listVarVal(){
		List<FlowClazz> list = flowClazzService.list(Wrappers.<FlowClazz>lambdaQuery().select(FlowClazz::getVarVal)
				.groupBy(FlowClazz::getVarVal));
		return R.ok(list.stream().filter(f -> Objects.nonNull(f) && StrUtil.isNotBlank(f.getVarVal())).collect(Collectors.toList()));
	}

	/**
	 * 查询租户所有节点监听事件设置
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(flowClazzService.list());
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(flowClazzService.removeByIds(ids));
	}

}
