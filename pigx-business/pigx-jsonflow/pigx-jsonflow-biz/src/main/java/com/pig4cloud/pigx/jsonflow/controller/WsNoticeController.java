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
import com.pig4cloud.pigx.jsonflow.api.entity.WsNotice;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.service.WsNoticeService;
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
 * 消息通知管理
 *
 * @author luolin
 * @date 2021-02-26 10:31:43
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ws-notice")
@Tag(description = "ws-notice", name = "消息通知管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class WsNoticeController {

	private final WsNoticeService wsNoticeService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wsNotice 消息通知管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute WsNotice wsNotice, @RequestParam(required = false) String[] queryTime) {
		QueryWrapper<WsNotice> query = Wrappers.query(wsNotice);
		QueryWrapperUtils.userQueryTime(query, WsNotice::getCreateTime, queryTime, WsNotice::getCreateUser,
				SecurityUtils.getUser().getId());

		return R.ok(wsNoticeService.page(page, query));
	}

	/**
	 * 通过id查询消息通知管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(wsNoticeService.getById(id));
	}

	/**
	 * 新增消息通知管理
	 * @param wsNotice 消息通知管理
	 * @return R
	 */
	@Operation(summary = "新增消息通知管理", description = "新增消息通知管理")
	@PostMapping
	public R save(@RequestBody WsNotice wsNotice) {
		wsNotice.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(wsNoticeService.save(wsNotice));
	}

	/**
	 * 暂存消息通知管理
	 * @param wsNotice 消息通知管理
	 * @return R
	 */
	@Operation(summary = "暂存消息通知管理", description = "暂存消息通知管理")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody WsNotice wsNotice) {
		wsNotice.setCreateUser(SecurityUtils.getUser().getId());
		wsNoticeService.saveOrUpdate(wsNotice);
		return R.ok(wsNotice.getId());
	}

	/**
	 * 修改消息通知管理
	 * @param wsNotice 消息通知管理
	 * @return R
	 */
	@Operation(summary = "修改消息通知管理", description = "修改消息通知管理")
	@PutMapping
	public R updateById(@RequestBody WsNotice wsNotice) {
		return R.ok(wsNoticeService.updateById(wsNotice));
	}

	/**
	 * 通过id删除消息通知管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除消息通知管理", description = "通过id删除消息通知管理")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(wsNoticeService.removeById(id));
	}

	/**
	 * 查询租户所有消息通知管理
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(wsNoticeService.list());
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(wsNoticeService.removeByIds(ids));
	}

}
