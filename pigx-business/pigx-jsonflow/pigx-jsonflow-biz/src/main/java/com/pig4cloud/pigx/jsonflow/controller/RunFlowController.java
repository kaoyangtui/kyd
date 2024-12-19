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
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.RunFlowVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 流程管理
 *
 * @author luolin
 * @date 2021-02-23 14:12:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/run-flow")
@Tag(description = "run-flow", name = "流程管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RunFlowController {

	private final RunFlowService runFlowService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param runFlow 流程管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute RunFlow runFlow, @RequestParam(required = false) String[] queryTime) {
		return R.ok(runFlowService.getPage(page, runFlow, queryTime));
	}

	/**
	 * 通过id查询流程管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(runFlowService.getById(id));
	}

	/**
	 * 通过id查询流程实例图
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/nodes/{id}/{isEdit}")
	public R getNodesById(@PathVariable("id") Long id, @PathVariable("isEdit") String isEdit) {
		return R.ok(runFlowService.getNodesById(id, isEdit));
	}

	/**
	 * 通过id查询流程管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/inner/{id}")
	public R innerGetById(@PathVariable("id") Long id) {
		return R.ok(runFlowService.getById(id));
	}

	/**
	 * 新增流程管理
	 * @param runFlow 流程管理
	 * @return R
	 */
	@Operation(summary = "新增流程管理", description = "新增流程管理")
	@PostMapping
	public R save(@RequestBody RunFlow runFlow) {
		runFlow.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runFlowService.save(runFlow));
	}

	/**
	 * 修改流程实例管理
	 * @param runFlowVO 流程管理
	 * @return R
	 */
	@Operation(summary = "修改流程实例管理", description = "修改流程实例管理")
	@PostMapping("/flow-inst-id")
	public R saveOrUpdate(@RequestBody RunFlowVO runFlowVO) {
		runFlowVO.setUpdateUser(SecurityUtils.getUser().getId());
		runFlowVO.setUpdateTime(LocalDateTime.now());
		return R.ok(runFlowService.saveOrUpdate(runFlowVO));
	}

	/**
	 * 暂存流程管理
	 * @param runFlow 流程管理
	 * @return R
	 */
	@Operation(summary = "暂存流程管理", description = "暂存流程管理")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody RunFlow runFlow) {
		runFlow.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runFlowService.saveOrUpdate(runFlow));
	}

	/**
	 * 修改流程管理
	 * @param runFlow 流程管理
	 * @return R
	 */
	@Operation(summary = "修改流程管理", description = "修改流程管理")
	@PutMapping
	public R updateById(@RequestBody RunFlow runFlow) {
		runFlow.setUpdateUser(SecurityUtils.getUser().getId());
		runFlow.setUpdateTime(LocalDateTime.now());
		return R.ok(runFlowService.updateById(runFlow));
	}

	/**
	 * 通过id删除流程管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除流程管理", description = "通过id删除流程管理")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(runFlowService.removeById(id));
	}

	/**
	 * 查询租户所有流程管理
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(runFlowService.list());
	}

	/**
	 * 获取运行中的流程
	 */
	@Operation(summary = "获取运行中的流程", description = "获取运行中的流程")
	@GetMapping("/key-status/{flowKey}/{status}")
	public R listRunFlows(@PathVariable String flowKey, @PathVariable String status) {
		return R.ok(runFlowService
				.list(Wrappers.<RunFlow>lambdaQuery().eq(RunFlow::getFlowKey, flowKey).eq(RunFlow::getStatus, status)));
	}

	/**
	 * 发起流程
	 * @param order 工单数据
	 * @param params 流程条件与分配参与者参数
	 * @return R
	 */
	@Operation(summary = "发起流程", description = "发起流程")
	@PostMapping("/start")
	public R startFlow(@RequestBody Map<String, Object> order, @RequestParam Map<String, Object> params) {
		return R.ok(runFlowService.startFlow(order, params));
	}

	/**
	 * 正常结束流程
	 * @return R
	 */
	@Operation(summary = "正常结束流程", description = "正常结束流程")
	@PutMapping("/complete")
	public R complete(@RequestBody RunJobVO runJobVO) {
		return R.ok(runFlowService.complete(runJobVO));
	}

	/**
	 * 提前结束流程
	 * @return R
	 */
	@Operation(summary = "提前结束流程", description = "提前结束流程")
	@PutMapping("/early-complete")
	public R earlyComplete(@RequestBody RunFlow runFlow) {
		return R.ok(runFlowService.earlyComplete(runFlow));
	}

	/**
	 * 终止/恢复流程
	 * @return R
	 */
	@Operation(summary = "终止/恢复流程", description = "终止/恢复流程")
	@PutMapping("/terminate")
	public R terminateFlow(@RequestBody RunFlow runFlow) {
		return R.ok(runFlowService.terminateFlow(runFlow));
	}

	/**
	 * 通过id作废流程管理
	 * @return R
	 */
	@Operation(summary = "通过id作废流程管理", description = "通过id作废流程管理")
	@DeleteMapping("/invalid")
	public R invalidFlow(@RequestBody RunFlow runFlow) {
		return R.ok(runFlowService.invalidFlow(runFlow));
	}

	/**
	 * 工单发起异常后删除流程
	 * @param runJobVO 运行任务
	 * @return
	 */
	@DeleteMapping("/del/flow/info")
	public R<Boolean> delFlowInfo(@RequestBody RunJobVO runJobVO) {
		return R.ok(runFlowService.delFlowInfo(runJobVO));
	}

	/**
	 * 根据code查询流程
	 * @return R
	 */
	@GetMapping("/code/{code}")
	public R getByCode(@PathVariable String code) {
		return R.ok(runFlowService.getOne(Wrappers.<RunFlow>lambdaQuery().eq(RunFlow::getCode, code)));
	}

	/**
	 * 根据ID查询流程
	 * @return R
	 */
	@PostMapping("/list/flow-inst-id")
	public R listByFlowInstIds(@RequestBody List<Long> flowInstIds){
		return R.ok(runFlowService.listByIds(flowInstIds));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(runFlowService.removeByIds(ids));
	}

	/**
	 * 撤回或重置当前流程
	 * @param runFlow 运行流程
	 * @return R
	 */
	@PutMapping("/recall-reset")
	public R recallReset(@RequestBody RunFlow runFlow){
		return R.ok(runFlowService.recallReset(runFlow));
	}

	/**
	 * 催办当前流程
	 * @param runFlow 运行流程
	 * @return R
	 */
	@PutMapping("/remind")
	public R remind(@RequestBody RunFlow runFlow){
		return R.ok(runFlowService.remind(runFlow));
	}

}
