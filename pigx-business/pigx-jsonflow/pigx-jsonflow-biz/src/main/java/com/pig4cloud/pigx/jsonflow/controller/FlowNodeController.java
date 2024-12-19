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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeVO;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
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
 * 流程节点设置
 *
 * @author luolin
 * @date 2021-02-23 14:12:03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/flow-node")
@Tag(description = "flow-node", name = "流程节点设置管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FlowNodeController {

	private final FlowNodeService flowNodeService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param flowNode 流程节点设置
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute FlowNode flowNode, @RequestParam(required = false) String[] queryTime) {
		QueryWrapper<FlowNode> query = Wrappers.query(flowNode);
		QueryWrapperUtils.queryTime(query, FlowNode::getCreateTime, queryTime);

		return R.ok(flowNodeService.page(page, query));
	}

	/**
	 * 通过id查询流程节点设置
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(flowNodeService.getById(id));
	}

	/**
	 * 新增流程节点设置
	 * @param flowNodeVO 流程节点设置
	 * @return R
	 */
	@Operation(summary = "新增流程节点设置", description = "新增流程节点设置")
	@PostMapping
	public R save(@RequestBody FlowNodeVO flowNodeVO) {
		flowNodeVO.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowNodeService.saveOrUpdate(flowNodeVO, null));
	}

	/**
	 * 暂存流程节点设置
	 * @param flowNode 流程节点设置
	 * @return R
	 */
	@Operation(summary = "暂存流程节点设置", description = "暂存流程节点设置")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody FlowNode flowNode) {
		flowNode.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowNodeService.saveOrUpdate(flowNode));
	}

	/**
	 * 修改流程节点设置
	 * @param flowNodeVO 流程节点设置
	 * @return R
	 */
	@Operation(summary = "修改流程节点设置", description = "修改流程节点设置")
	@PutMapping
	public R updateById(@RequestBody FlowNodeVO flowNodeVO) {
		flowNodeVO.setUpdateUser(SecurityUtils.getUser().getId());
		flowNodeVO.setUpdateTime(LocalDateTime.now());
		return R.ok(flowNodeService.saveOrUpdate(flowNodeVO, null));
	}

	/**
	 * 通过id删除流程节点设置
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除流程节点设置", description = "通过id删除流程节点设置")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(flowNodeService.removeById(id));
	}

	/**
	 * 查询租户所有流程节点设置
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(flowNodeService.list());
	}

	/**
	 * 根据流程定义查询节点设置及节点名称
	 * @return R
	 */
	@GetMapping("/list/def-flow-id/{defFlowId}")
	public R listByDefFlowId(@PathVariable Long defFlowId){
		return R.ok(flowNodeService.list(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, defFlowId)));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(flowNodeService.removeByIds(ids));
	}

}
