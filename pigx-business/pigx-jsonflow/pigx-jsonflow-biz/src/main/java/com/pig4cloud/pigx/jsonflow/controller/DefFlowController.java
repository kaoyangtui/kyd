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
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.DefFlowVO;
import com.pig4cloud.pigx.jsonflow.engine.JsonFlowEngineService;
import com.pig4cloud.pigx.jsonflow.service.DefFlowService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程定义管理
 *
 * @author luolin
 * @date 2021-02-23 13:52:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/def-flow")
@Tag(description = "def-flow", name = "流程定义管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DefFlowController {

	private final DefFlowService defFlowService;
	private final JsonFlowEngineService jsonFlowEngineService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param defFlow 流程定义管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute DefFlow defFlow, @RequestParam(required = false) String[] queryTime) {
		QueryWrapper<DefFlow> query = Wrappers.query(defFlow);
		QueryWrapperUtils.queryTime(query, DefFlow::getCreateTime, queryTime);

		return R.ok(defFlowService.page(page, query));
	}

	/**
	 * 代码生成流程例子
	 * @return R
	 */
	@Operation(summary = "代码生成流程例子", description = "代码生成流程例子")
	@PostMapping("/test-code-gen")
	public R testCodeGen() {
		return R.ok(jsonFlowEngineService.testCodeGen());
	}

	/**
	 * 代码更新流程例子
	 * @return R
	 */
	@Operation(summary = "代码更新流程例子", description = "代码更新流程例子")
	@PostMapping("/test-code-update")
	public R testCodeUpdate() {
		return R.ok(jsonFlowEngineService.testCodeUpdate());
	}

	/**
	 * 通过id查询流程定义管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(defFlowService.getById(id));
	}

	/**
	 * 通过id查询流程定义图
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/nodes/{id}")
	public R getNodesById(@PathVariable("id") Long id) {
		return R.ok(defFlowService.getNodesById(id));
	}

	/**
	 * 新增流程定义管理
	 * @param defFlowVO 流程定义管理
	 * @return R
	 */
	@Operation(summary = "新增流程定义管理", description = "新增流程定义管理")
	@PostMapping
	public R saveOrUpdate(@RequestBody DefFlowVO defFlowVO) {
		return R.ok(defFlowService.saveOrUpdate(defFlowVO));
	}

	/**
	 * 暂存流程定义管理
	 * @param defFlow 流程定义管理
	 * @return R
	 */
	@Operation(summary = "暂存流程定义管理", description = "暂存流程定义管理")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody DefFlow defFlow) {
		defFlow.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(defFlowService.tempStore(defFlow));
	}

	/**
	 * 修改流程定义管理
	 * @param defFlow 流程定义管理
	 * @return R
	 */
	@Operation(summary = "修改流程定义管理", description = "修改流程定义管理")
	@PutMapping
	public R updateById(@RequestBody DefFlow defFlow) {
		defFlow.setUpdateUser(SecurityUtils.getUser().getId());
		defFlow.setUpdateTime(LocalDateTime.now());
		return R.ok(defFlowService.updateById(defFlow));
	}

	/**
	 * 通过id删除流程定义管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除流程定义管理", description = "通过id删除流程定义管理")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(defFlowService.removeById(id));
	}

	/**
	 * 查询租户所有流程定义管理
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(defFlowService.list(Wrappers.<DefFlow>lambdaQuery().orderByDesc(DefFlow::getCreateTime)));
	}

	/**
	 * 根据流程名称获取信息
	 * @param flowName flowName
	 * @return R
	 */
	@Operation(summary = "根据流程名称获取信息", description = "根据流程名称获取信息")
	@GetMapping("/flow-name/{flowName}")
	public R getByFlowName(@PathVariable("flowName") String flowName) {
		return R.ok(defFlowService.getOne(Wrappers.<DefFlow>lambdaQuery().eq(DefFlow::getFlowName, flowName)));
	}

	/**
	 * 查询流程定义分组名称
	 * @return R
	 */
	@GetMapping("/list/group-name")
	public R listGroupName() {
		return R.ok(defFlowService.list(Wrappers.<DefFlow>lambdaQuery().select(DefFlow::getGroupName)
				.isNotNull(DefFlow::getGroupName).groupBy(DefFlow::getGroupName)));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(defFlowService.removeByIds(ids));
	}

}
