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
import com.pig4cloud.pigx.jsonflow.api.entity.NodeJob;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.NodeJobVO;
import com.pig4cloud.pigx.jsonflow.service.NodeJobService;
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
 * 节点任务设置
 *
 * @author luolin
 * @date 2021-03-09 17:30:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/node-job")
@Tag(description = "node-job", name = "节点任务设置管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class NodeJobController {

	private final NodeJobService nodeJobService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param nodeJob 节点任务设置
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute NodeJob nodeJob, @RequestParam(required = false) String[] queryTime) {
		QueryWrapper<NodeJob> query = Wrappers.query(nodeJob);
		QueryWrapperUtils.queryTime(query, NodeJob::getCreateTime, queryTime);

		return R.ok(nodeJobService.page(page, query));
	}

	/**
	 * 通过id查询节点任务设置
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(nodeJobService.getById(id));
	}

	/**
	 * 新增节点任务设置
	 * @param nodeJobVO 节点任务设置
	 * @return R
	 */
	@Operation(summary = "新增节点任务设置", description = "新增节点任务设置")
	@PostMapping
	public R save(@RequestBody NodeJobVO nodeJobVO) {
		nodeJobVO.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(nodeJobService.saveOrUpdate(nodeJobVO));
	}

	/**
	 * 暂存节点任务设置
	 * @param nodeJob 节点任务设置
	 * @return R
	 */
	@Operation(summary = "暂存节点任务设置", description = "暂存节点任务设置")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody NodeJob nodeJob) {
		nodeJob.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(nodeJobService.saveOrUpdate(nodeJob));
	}

	/**
	 * 修改节点任务设置
	 * @param nodeJobVO 节点任务设置
	 * @return R
	 */
	@Operation(summary = "修改节点任务设置", description = "修改节点任务设置")
	@PutMapping
	public R updateById(@RequestBody NodeJobVO nodeJobVO) {
		nodeJobVO.setUpdateUser(SecurityUtils.getUser().getId());
		nodeJobVO.setUpdateTime(LocalDateTime.now());
		return R.ok(nodeJobService.saveOrUpdate(nodeJobVO));
	}

	/**
	 * 通过id删除节点任务设置
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除节点任务设置", description = "通过id删除节点任务设置")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(nodeJobService.removeById(id));
	}

	/**
	 * 查询租户所有节点任务设置
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(nodeJobService.list());
	}

	/**
	 * 根据流程定义查询任务设置及任务名称
	 * @return R
	 */
	@GetMapping("/list/def-flow-id/{defFlowId}")
	public R listByDefFlowId(@PathVariable Long defFlowId){
		return R.ok(nodeJobService.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getDefFlowId, defFlowId)));
	}

	/**
	 * 根据节点定义查询任务设置及任务名称
	 * @return R
	 */
	@GetMapping("/list/flow-node-id/{flowNodeId}")
	public R listByFlowNodeId(@PathVariable String flowNodeId){
		return R.ok(nodeJobService.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getFlowNodeId, flowNodeId)));
	}

	/**
	 * 查询节点任务userKey
	 * @return R
	 */
	@GetMapping("/list/user-key")
	public R listUserKey() {
		List<NodeJob> list = nodeJobService
				.list(Wrappers.<NodeJob>lambdaQuery().select(NodeJob::getUserKey).groupBy(NodeJob::getUserKey));
		return R.ok(list.stream().filter(f -> Objects.nonNull(f) && StrUtil.isNotBlank(f.getUserKey()))
				.collect(Collectors.toList()));
	}

	/**
	 * 查询节点任务userKeyVal
	 * @return R
	 */
	@GetMapping("/list/user-key-val")
	public R listUserKeyVal() {
		List<NodeJob> list = nodeJobService
				.list(Wrappers.<NodeJob>lambdaQuery().select(NodeJob::getUserKeyVal).groupBy(NodeJob::getUserKeyVal));
		return R.ok(list.stream().filter(f -> Objects.nonNull(f) && StrUtil.isNotBlank(f.getUserKeyVal()))
				.collect(Collectors.toList()));
	}

	/**
	 * 通过流程定义ID查询UserKeyVal
	 * @param defFlowId 流程定义ID
	 * @return UserKeyVal信息
	 */
	@GetMapping("/list/user-key-val/{defFlowId}")
	public R<List<NodeJob>> listUserKeyValByDefFlowId(@PathVariable("defFlowId") Long defFlowId) {
		return R.ok(nodeJobService.list(Wrappers.<NodeJob>lambdaQuery().isNotNull(NodeJob::getUserKeyVal)
				.eq(NodeJob::getDefFlowId, defFlowId)));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(nodeJobService.removeByIds(ids));
	}

}
