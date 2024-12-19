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
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNodeRel;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeRelVO;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeRelService;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程节点定义关系
 *
 * @author luolin
 * @date 2021-03-10 16:48:00
 */
@RestController
@AllArgsConstructor
@RequestMapping("/flow-node-rel")
@Tag(description = "flow-node-rel", name = "流程节点定义关系管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FlowNodeRelController {

	private final FlowNodeRelService flowNodeRelService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param flowNodeRel 流程节点定义关系
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute FlowNodeRel flowNodeRel,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<FlowNodeRel> query = Wrappers.query(flowNodeRel);
		QueryWrapperUtils.queryTime(query, FlowNodeRel::getCreateTime, queryTime);

		return R.ok(flowNodeRelService.page(page, query));
	}

	/**
	 * 通过id查询流程节点定义关系
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(flowNodeRelService.getById(id));
	}

	/**
	 * 新增流程节点定义关系
	 * @param flowNodeRelVO 流程节点定义关系
	 * @return R
	 */
	@Operation(summary = "新增流程节点定义关系", description = "新增流程节点定义关系")
	@PostMapping
	public R save(@RequestBody FlowNodeRelVO flowNodeRelVO) {
		flowNodeRelVO.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowNodeRelService.saveOrUpdate(flowNodeRelVO, null, null));
	}

	/**
	 * 暂存流程节点定义关系
	 * @param flowNodeRel 流程节点定义关系
	 * @return R
	 */
	@Operation(summary = "暂存流程节点定义关系", description = "暂存流程节点定义关系")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody FlowNodeRel flowNodeRel) {
		flowNodeRel.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(flowNodeRelService.saveOrUpdate(flowNodeRel));
	}

	/**
	 * 修改流程节点定义关系
	 * @param flowNodeRelVO 流程节点定义关系
	 * @return R
	 */
	@Operation(summary = "修改流程节点定义关系", description = "修改流程节点定义关系")
	@PutMapping
	public R updateById(@RequestBody FlowNodeRelVO flowNodeRelVO) {
		flowNodeRelVO.setUpdateUser(SecurityUtils.getUser().getId());
		flowNodeRelVO.setUpdateTime(LocalDateTime.now());
		return R.ok(flowNodeRelService.saveOrUpdate(flowNodeRelVO, null, null));
	}

	/**
	 * 通过id删除流程节点定义关系
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除流程节点定义关系", description = "通过id删除流程节点定义关系")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(flowNodeRelService.removeById(id));
	}

	/**
	 * 查询租户所有流程节点定义关系
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(flowNodeRelService.list());
	}

	/**
	 * 查询节点任务条件key-Val
	 * @return R
	 */
	@GetMapping("/list/var-key-val")
	public R listVarKeyVal() {
		List<FlowNodeRel> list = flowNodeRelService.list(Wrappers.<FlowNodeRel>lambdaQuery()
				.select(FlowNodeRel::getVarKeyVal).groupBy(FlowNodeRel::getVarKeyVal));
		return R.ok(list.stream().filter(f -> Objects.nonNull(f) && StrUtil.isNotBlank(f.getVarKeyVal()))
				.collect(Collectors.toList()));
	}

	/**
	 * 通过流程定义ID查询VarKeyVal
	 * @param defFlowId 流程定义ID
	 * @return VarKeyVal信息
	 */
	@GetMapping("/list/var-key-val/{defFlowId}")
	public R<List<FlowNodeRel>> listVarKeyValByDefFlowId(@PathVariable("defFlowId") Long defFlowId) {
		return R.ok(flowNodeRelService.list(Wrappers.<FlowNodeRel>lambdaQuery().isNotNull(FlowNodeRel::getVarKeyVal)
				.eq(FlowNodeRel::getDefFlowId, defFlowId)));
	}

	/**
	 * 根据流程定义查询节点设置及节点名称
	 * @return R
	 */
	@GetMapping("/list/def-flow-id/{defFlowId}")
	public R listByDefFlowId(@PathVariable Long defFlowId){
		return R.ok(flowNodeRelService.list(Wrappers.<FlowNodeRel>lambdaQuery().eq(FlowNodeRel::getDefFlowId, defFlowId)));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(flowNodeRelService.removeByIds(ids));
	}

}
