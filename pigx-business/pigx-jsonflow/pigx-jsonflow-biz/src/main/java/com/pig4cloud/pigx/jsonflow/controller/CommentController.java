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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.entity.Comment;
import com.pig4cloud.pigx.jsonflow.api.vo.CommentVO;
import com.pig4cloud.pigx.jsonflow.service.CommentService;
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
 * 节点批注管理
 *
 * @author luolin
 * @date 2021-02-25 16:41:52
 */
@RestController
@AllArgsConstructor
@RequestMapping("/comment")
@Tag(description = "comment", name = "节点批注管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class CommentController {

	private final CommentService commentService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param comment 节点批注管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getPage(Page page, @ModelAttribute Comment comment, @RequestParam(required = false) String[] queryTime) {
		return R.ok(commentService.getPage(page, comment, queryTime));
	}

	/**
	 * 通过id查询节点批注管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(commentService.getById(id));
	}

	/**
	 * 新增节点批注管理
	 * @param comment 节点批注管理
	 * @return R
	 */
	@Operation(summary = "新增节点批注管理", description = "新增节点批注管理")
	@PostMapping
	public R save(@RequestBody Comment comment) {
		comment.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(commentService.save(comment));
	}

	/**
	 * 暂存节点批注管理
	 * @param comment 节点批注管理
	 * @return R
	 */
	@Operation(summary = "暂存节点批注管理", description = "暂存节点批注管理")
	@PostMapping("/temp-store")
	public R tempStore(@RequestBody Comment comment) {
		comment.setCreateUser(SecurityUtils.getUser().getId());
		return R.ok(commentService.saveOrUpdate(comment));
	}

	/**
	 * 修改节点批注管理
	 * @param comment 节点批注管理
	 * @return R
	 */
	@Operation(summary = "修改节点批注管理", description = "修改节点批注管理")
	@PutMapping
	public R updateById(@RequestBody Comment comment) {
		return R.ok(commentService.updateById(comment));
	}

	/**
	 * 通过id删除节点批注管理
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id删除节点批注管理", description = "通过id删除节点批注管理")
	@DeleteMapping("/{id}")
	public R removeById(@PathVariable Long id) {
		return R.ok(commentService.removeById(id));
	}

	/**
	 * 查询租户所有节点批注管理
	 * @return R
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(commentService.list());
	}

	/**
	 * 查询批注信息
	 * @return
	 */
	@Operation(summary = "查询批注信息", description = "查询批注信息")
	@GetMapping("/comment")
	public R commentPage(Page page, @ModelAttribute CommentVO commentVO) {
		return R.ok(commentService.commentPage(page, commentVO));
	}

	/**
	 * 查询批注信息，用于时间线
	 * @return
	 */
	@Operation(summary = "查询批注信息", description = "查询批注信息")
	@GetMapping("/comment/list")
	public R commentList(@ModelAttribute CommentVO commentVO) {
		return R.ok(commentService.commentList(commentVO));
	}

	/**
	 * 通过ids删除
	 * @param ids ids
	 * @return R
	 */
	@Operation(summary = "通过ids删除", description = "通过ids删除")
	@DeleteMapping
	public R removeById(@RequestBody List<Long> ids) {
		return R.ok(commentService.removeByIds(ids));
	}

}
