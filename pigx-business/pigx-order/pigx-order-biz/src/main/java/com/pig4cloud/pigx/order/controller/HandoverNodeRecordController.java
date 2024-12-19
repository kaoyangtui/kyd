package com.pig4cloud.pigx.order.controller;

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
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowStatusEnum;
import com.pig4cloud.pigx.order.api.entity.HandoverNodeRecord;
import com.pig4cloud.pigx.order.service.HandoverNodeRecordService;
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
 * 交接任务记录
 *
 * @author luolin
 * @date 2020-04-26 10:45:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/handover-node-record")
@Tag(description = "handover-node-record", name = "交接任务记录管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class HandoverNodeRecordController {

	private final HandoverNodeRecordService handoverNodeRecordService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param handoverNodeRecord 交接任务记录
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute HandoverNodeRecord handoverNodeRecord,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<HandoverNodeRecord> query = Wrappers.query(handoverNodeRecord);
		QueryWrapperUtils.userQueryTime(query, HandoverNodeRecord::getCreateTime, queryTime,
				HandoverNodeRecord::getCreateUser, SecurityUtils.getUser().getId());

		return R.ok(handoverNodeRecordService.page(page, query));
	}

	/**
	 * 通过id查询交接任务记录
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(handoverNodeRecordService.getById(id));
	}

	/**
	 * 新增交接任务记录
	 * @param handoverNodeRecord 交接任务记录
	 * @return R
	 */
	@Operation(summary = "新增交接任务记录", description = "新增交接任务记录")
	@PostMapping
	public R save(@RequestBody HandoverNodeRecord handoverNodeRecord) {
		handoverNodeRecord.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(handoverNodeRecordService.saveOrUpdateByType(handoverNodeRecord));
	}

	/**
	 * 批量新增或修改交接任务记录
	 * @param handoverNodeRecords 交接任务记录
	 * @return R
	 */
	@Operation(summary = "批量新增或修改交接任务记录", description = "批量新增或修改交接任务记录")
	@PostMapping("/batch")
	public R batchSaveOrUpdate(@RequestBody List<HandoverNodeRecord> handoverNodeRecords) {
		return R.ok(handoverNodeRecordService.batchSaveOrUpdate(handoverNodeRecords));
	}

	/**
	 * 修改交接任务记录
	 * @param handoverNodeRecord 交接任务记录
	 * @return R
	 */
	@Operation(summary = "修改交接任务记录", description = "修改交接任务记录")
	@PutMapping
	public R updateById(@RequestBody HandoverNodeRecord handoverNodeRecord) {
		return R.ok(handoverNodeRecordService.saveOrUpdateByType(handoverNodeRecord));
	}

	/**
	 * 通过id删除交接任务记录
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除交接任务记录", description = "通过id删除交接任务记录")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(handoverNodeRecordService.removeById(id));
	}

	/**
	 * 查询租户所有交接任务记录
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(handoverNodeRecordService.list());
	}

	/**
	 * 根据任务ID集合查询交接中的任务
	 * @return R
	 */
	@PostMapping("/list-run/run-job-ids")
	public R listRunByRunJobIds(@RequestBody List<String> ids) {
		return R.ok(handoverNodeRecordService
				.list(Wrappers.<HandoverNodeRecord>lambdaQuery().in(HandoverNodeRecord::getRunJobId, ids)
						.eq(HandoverNodeRecord::getStatus, HandoverFlowStatusEnum.RUN.getStatus())));
	}

	/**
	 * 查看/修改/接收、被驳回分页查询
	 * @param page 分页对象
	 * @param handoverNodeRecord 交接工作记录
	 * @return
	 */
	@Operation(summary = "被驳回分页查询", description = "被驳回分页查询")
	@GetMapping("/flow/page")
	public R getFlowPage(Page page, HandoverNodeRecord handoverNodeRecord,
			@RequestParam(required = false) String[] queryTime) {
		QueryWrapper<HandoverNodeRecord> query = Wrappers.query(handoverNodeRecord);
		// 判断是否被驳回
		String retStatus = handoverNodeRecord.getRetStatus();
		if (FlowCommonConstants.YES.equals(retStatus)) {
			QueryWrapperUtils.userQueryTime(query, HandoverNodeRecord::getCreateTime, queryTime,
					HandoverNodeRecord::getCreateUser, handoverNodeRecord.getCreateUser());
		}
		else {
			QueryWrapperUtils.queryTime(query, HandoverNodeRecord::getCreateTime, queryTime);
		}
		return R.ok(handoverNodeRecordService.page(page, query));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(handoverNodeRecordService.removeByIds(ids));
	}

}
