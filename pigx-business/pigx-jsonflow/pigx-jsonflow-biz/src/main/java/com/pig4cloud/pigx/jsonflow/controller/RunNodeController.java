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
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
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
 * 运行节点管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/run-node")
@Tag(description = "run-node", name = "运行节点管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RunNodeController {

	private final RunNodeService runNodeService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param runNode 运行节点管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute RunNode runNode, @RequestParam(required = false) String[] queryTime) {
		QueryWrapper<RunNode> query = Wrappers.query(runNode);
		QueryWrapperUtils.queryTime(query, RunNode::getCreateTime, queryTime);

		return R.ok(runNodeService.page(page, query));
	}

	/**
	 * 通过id查询运行节点管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(runNodeService.getById(id));
	}

	/**
	 * 新增运行节点管理
	 * @param runNode 运行节点管理
	 * @return R
	 */
	@Operation(summary = "新增运行节点管理", description = "新增运行节点管理")
	@PostMapping
	public R save(@RequestBody RunNode runNode) {
		runNode.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runNodeService.save(runNode));
	}

	/**
	 * 暂存运行节点管理
	 * @param runNode 运行节点管理
	 * @return R
	 */
	@Operation(summary = "暂存运行节点管理", description = "暂存运行节点管理")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody RunNode runNode) {
		runNode.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runNodeService.saveOrUpdate(runNode));
	}

	/**
	 * 修改运行节点管理
	 * @param runNode 运行节点管理
	 * @return R
	 */
	@Operation(summary = "修改运行节点管理", description = "修改运行节点管理")
	@PutMapping
	public R updateById(@RequestBody RunNode runNode) {
		return R.ok(runNodeService.updateById(runNode));
	}

	/**
	 * 通过id删除运行节点管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除运行节点管理", description = "通过id删除运行节点管理")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(runNodeService.removeById(id));
	}

	/**
	 * 查询租户所有运行节点管理
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(runNodeService.list());
	}

	/**
	 * 通过流程id查询小于当前运行节点的节点
	 * @param flowInstId 流程实例ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询小于当前运行节点的节点", description = "通过流程id查询小于当前运行节点的节点")
	@GetMapping("/flow-inst-id/pre/{flowInstId}")
	public R listLtSortByFlowNodeId(@PathVariable Long flowInstId, @RequestParam Long runNodeId) {
		return R.ok(runNodeService.listPreByFlowNodeId(flowInstId, runNodeId));
	}

	/**
	 * 通过流程id查询任意运行节点
	 * @param flowInstId 流程实例ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询任意运行节点", description = "通过流程id查询任意运行节点")
	@GetMapping("/flow-inst-id/any-jump/{flowInstId}")
	public R listAnyJumpByFlowNodeId(@PathVariable Long flowInstId, @RequestParam Long runNodeId) {
		return R.ok(runNodeService.listAnyJumpByFlowNodeId(flowInstId, runNodeId));
	}

	/**
	 * 通过流程id查询运行节点管理
	 * @param flowInstId 流程实例ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行节点管理", description = "通过流程id查询运行节点管理")
	@GetMapping("/list/flow-inst-id/{flowInstId}")
	public R listByFlowNodeId(@PathVariable Long flowInstId) {
		return R.ok(runNodeService.listByFlowNodeId(flowInstId));
	}

	/**
	 * 根据ID查询流程节点
	 * @return R
	 */
	@PostMapping("/list/run-node-id")
	public R listByRunNodeIds(@RequestBody List<Long> runNodeIds){
		return R.ok(runNodeService.listByIds(runNodeIds));
	}
	
	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(runNodeService.removeByIds(ids));
	}

	/**
	 * 催办当前节点
	 * @param runNode 运行节点
	 * @return R
	 */
	@PutMapping("/remind")
	public R remind(@RequestBody RunNode runNode){
		return R.ok(runNodeService.remind(runNode));
	}
	
	/**
	 * 新增加签
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "新增加签", description = "新增加签")
	@PostMapping("/signature")
	public R signature(@RequestBody RunJobVO runJobVO) {
		return R.ok(runNodeService.signature(runJobVO));
	}
	
}
