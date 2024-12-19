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
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.TodoJobVO;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
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

/**
 * 任务记录
 *
 * @author luolin
 * @date 2021-03-03 09:26:28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/run-job")
@Tag(description = "run-job", name = "任务记录管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class RunJobController {

	private final RunJobService runJobService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param runJob 任务记录
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute RunJob runJob, @RequestParam(required = false) String[] queryTime) {
		QueryWrapper<RunJob> query = Wrappers.query(runJob);
		QueryWrapperUtils.queryTime(query, RunJob::getCreateTime, queryTime);

		return R.ok(runJobService.page(page, query));
	}

	/**
	 * 通过id查询任务记录
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(runJobService.getById(id));
	}

	/**
	 * 新增任务记录
	 * @param runJob 任务记录
	 * @return R
	 */
	@Operation(summary = "新增任务记录", description = "新增任务记录")
	@PostMapping
	public R save(@RequestBody RunJob runJob) {
		runJob.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runJobService.save(runJob));
	}

	/**
	 * 暂存任务记录
	 * @param runJob 任务记录
	 * @return R
	 */
	@Operation(summary = "暂存任务记录", description = "暂存任务记录")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody RunJob runJob) {
		runJob.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(runJobService.saveOrUpdate(runJob));
	}

	/**
	 * 修改任务记录
	 * @param runJob 任务记录
	 * @return R
	 */
	@Operation(summary = "修改任务记录", description = "修改任务记录")
	@PutMapping
	public R updateById(@RequestBody RunJob runJob) {
		return R.ok(runJobService.updateById(runJob));
	}

	/**
	 * 通过id删除任务记录
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除任务记录", description = "通过id删除任务记录")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(runJobService.removeById(id));
	}

	/**
	 * 查询租户所有任务记录
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(runJobService.list());
	}

	/**
	 * 新增任务完成记录
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "新增任务完成记录", description = "新增任务完成记录")
	@PostMapping("/complete")
	public R complete(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.complete(runJobVO));
	}

	/**
	 * 处理任意跳转
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "处理任意跳转", description = "处理任意跳转")
	@PostMapping("/any-jump")
	public R anyJump(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.anyJump(runJobVO));
	}

	/**
	 * 处理退回上一步
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "处理退回上一步", description = "处理退回上一步")
	@PostMapping("/back-pre")
	public R backPreJob(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.backPreJob(runJobVO));
	}

	/**
	 * 处理退回首节点
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "处理退回首节点", description = "处理退回首节点")
	@PostMapping("/back-first")
	public R backFirstJob(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.backFirstJob(runJobVO));
	}

	/**
	 * 新增驳回记录
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "新增驳回记录", description = "新增驳回记录")
	@PostMapping("/reject")
	public R reject(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.reject(runJobVO));
	}

	/**
	 * 新增加签
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "新增加签", description = "新增加签")
	@PostMapping("/signature")
	public R signature(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.signature(runJobVO));
	}

	/**
	 * 处理挂起逻辑
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "处理挂起逻辑", description = "处理挂起逻辑")
	@PutMapping("/suspension")
	public R suspension(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.suspension(runJobVO));
	}

	/**
	 * 签收反签收任务
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "签收反签收任务", description = "签收反签收任务")
	@PutMapping("/sign-for-job")
	public R signForJob(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.signForJob(runJobVO));
	}

	/**
	 * 处理转办逻辑
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "处理转办逻辑", description = "处理转办逻辑")
	@PutMapping("/turn")
	public R turnRunJob(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.turnRunJob(runJobVO));
	}

	/**
	 * 指派人员
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@Operation(summary = "指派人员", description = "指派人员")
	@PutMapping("/appoint")
	public R appointUser(@RequestBody RunJobVO runJobVO) {
		return R.ok(runJobService.appointUser(runJobVO));
	}

	/**
	 * 分页查询待办任务
	 * @param page 分页对象
	 * @param todoJobVO 任务记录
	 * @return
	 */
	@Operation(summary = "分页查询待办任务", description = "分页查询待办任务")
    @GetMapping("/todo/page")
    public R getTodoPage(Page page, @ModelAttribute TodoJobVO todoJobVO) {
        return R.ok(runJobService.getTodoPage(page, todoJobVO));
    }

	/**
	 * 分页查询待办任务
	 * @param page 分页对象
	 * @return
	 */
	@Operation(summary = "分页查询待办任务", description = "分页查询待办任务")
	@GetMapping("/todo/size")
	public R getTodoSize(@ModelAttribute Page page) {
		return R.ok(runJobService.getTodoSize(page, new TodoJobVO()));
	}

	/**
	 * 通过id获取审批前数据
	 * @param todoJobVO 任务信息
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/todo/detail")
	public R getTodoDetailById(@ModelAttribute TodoJobVO todoJobVO) {
		return R.ok(runJobService.getTodoDetailById(todoJobVO));
	}

	/**
	 * 分页查询待办任务
	 * @param page 分页对象
	 * @param toDoneJobVO 任务记录
	 * @return
	 */
	@Operation(summary = "分页查询待办任务", description = "分页查询待办任务")
    @GetMapping("/to-done/page")
    public R getToDonePage(Page page, @ModelAttribute ToDoneJobVO toDoneJobVO) {
        return R.ok(runJobService.getToDonePage(page, toDoneJobVO));
    }

	/**
	 * 通过id获取审批前数据
	 * @param toDoneJobVO 任务信息
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/to-done/detail")
	public R getToDoneDetailById(@ModelAttribute ToDoneJobVO toDoneJobVO) {
		return R.ok(runJobService.getToDoneDetailById(toDoneJobVO));
	}

	/**
	 * 任务交接：查询交接人是自己未完成流程的任务
	 * @param page
	 * @param status 状态
	 * @return
	 */
	@GetMapping("/page/job-handover")
	public R jobHandoverPage(Page page, String status) {
		return R.ok(runJobService.jobHandoverPage(page, status));
	}

	/**
	 * 开启当前userKey节点任务
	 */
	@PostMapping("/start/run-job/user-key")
	public R startRunJobByUserKey(@RequestBody DistPersonVO distPersonVO) {
		return R.ok(runJobService.startRunJobByUserKey(distPersonVO));
	}
	
	/**
	 * 根据任务ID设置办理人
	 * @param runJobs 运行任务集合
	 * @param handoverUser 交接人
	 * @param handoverReason 交接原因
	 * @param type 类型
	 * @return
	 */
	@PutMapping("/handover/ids")
	public R handleJobHandover(@RequestBody List<RunJob> runJobs, @RequestParam Long handoverUser,
			@RequestParam String handoverReason, @RequestParam String type) {
		return R.ok(runJobService.handleJobHandover(runJobs, handoverUser, handoverReason, type));
	}

	/**
	 * 查询租户所有任务记录
	 * @return R
	 */
	@GetMapping("/list/{flowInstId}")
	public R listByFlowInstId(@PathVariable Long flowInstId) {
		return R.ok(runJobService.listByFlowInstId(flowInstId));
	}

	/**
	 * 通过流程id查询运行任务审批人
	 * @param runNodeId 运行节点ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行任务审批人", description = "通过流程id查询运行任务审批人")
	@GetMapping("/list/users/run-node-id/{runNodeId}")
	public R listUsersByRunNodeId(@PathVariable Long runNodeId) {
		return R.ok(runJobService.listUsersByRunNodeId(runNodeId));
	}
	
	/**
	 * 通过流程id查询运行任务审批人
	 * @param runNodeId 运行节点ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行任务审批人", description = "通过流程id查询运行任务审批人")
	@GetMapping("/list/user/run-node-id/{runNodeId}")
	public R listUserByRunNodeId(@PathVariable Long runNodeId) {
		return R.ok(runJobService.listUserByRunNodeId(runNodeId));
	}

	/**
	 * 通过流程id查询运行任务审批角色
	 * @param runNodeId 运行节点ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行任务审批角色", description = "通过流程id查询运行任务审批角色")
	@GetMapping("/list/role/run-node-id/{runNodeId}")
	public R listRoleByRunNodeId(@PathVariable Long runNodeId) {
		return R.ok(runJobService.listRoleByRunNodeId(runNodeId));
	}

	/**
	 * 通过流程id查询运行任务审批岗位
	 * @param runNodeId 运行节点ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行任务审批岗位", description = "通过流程id查询运行任务审批岗位")
	@GetMapping("/list/post/run-node-id/{runNodeId}")
	public R listPostByRunNodeId(@PathVariable Long runNodeId) {
		return R.ok(runJobService.listPostByRunNodeId(runNodeId));
	}

	/**
	 * 通过流程id查询运行任务审批部门
	 * @param runNodeId 运行节点ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行任务审批部门", description = "通过流程id查询运行任务审批部门")
	@GetMapping("/list/dept/run-node-id/{runNodeId}")
	public R listDeptByRunNodeId(@PathVariable Long runNodeId) {
		return R.ok(runJobService.listDeptByRunNodeId(runNodeId));
	}

	/**
	 * 通过流程id查询运行任务
	 * @param runNodeId 运行节点ID
	 * @return R
	 */
	@Operation(summary = "通过流程id查询运行任务", description = "通过流程id查询运行任务")
	@GetMapping("/list/run-job/run-node-id/{runNodeId}")
	public R listRunJobByRunNodeId(@PathVariable Long runNodeId) {
		return R.ok(runJobService.listRunJobByRunNodeId(runNodeId));
	}

	/**
	 * 根据ID查询流程
	 * @return R
	 */
	@PostMapping("/list/run-job-id")
	public R listByRunJobIds(@RequestBody List<Long> runJobIds){
		return R.ok(runJobService.listByIds(runJobIds));
	}
	
	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(runJobService.removeByIds(ids));
	}

	/**
	 * 取回任务
	 * @param runJob 运行任务
	 * @return R
	 */
	@PutMapping("/retake-job")
	public R retakeJob(@RequestBody RunJob runJob){
		return R.ok(runJobService.retakeJob(runJob));
	}

	/**
	 * 减签当前任务
	 * @param runJob 运行任务
	 * @return R
	 */
	@PutMapping("/sign-off")
	public R signOff(@RequestBody RunJob runJob){
		return R.ok(runJobService.signOff(runJob));
	}

	/**
	 * 催办当前任务
	 * @param runJobVO 运行任务
	 * @return R
	 */
	@PutMapping("/remind")
	public R remind(@RequestBody RunJobVO runJobVO){
		return R.ok(runJobService.remind(runJobVO));
	}

	/**
	 * 是否已阅
	 * @param runJob 运行任务
	 * @return R
	 */
	@PutMapping("/is-read")
	public R isRead(@RequestBody RunJob runJob){
		return R.ok(runJobService.isRead(runJob));
	}

}
