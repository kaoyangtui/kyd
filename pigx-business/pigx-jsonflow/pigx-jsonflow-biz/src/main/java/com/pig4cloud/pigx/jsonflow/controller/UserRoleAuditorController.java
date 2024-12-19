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
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.jsonflow.engine.IAuditorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审批人/角色接口请求
 *
 * @author luolin
 * @date 2021-02-26 10:31:43
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user-role-auditor")
@Tag(description = "user-role-auditor", name = "审批人/角色接口请求")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class UserRoleAuditorController {

	private final IAuditorService auditorService;

	/**
	 * 通过角色ID查询用户集合
	 *
	 * @param roleId 角色ID
	 * @return 用户信息
	 */
	@GetMapping("/list-users/{roleId}")
	public R<List<SysUser>> listUsersByRoleId(@PathVariable Long roleId, @RequestParam String jobType) {
		return R.ok(auditorService.listUsersByRoleId(roleId, jobType));
	}

	/**
	 * 办理人/角色选择器数据
	 *
	 * @return 用户及角色集合
	 */
	@GetMapping("/user-role/picker")
	public R fetchUserRolePicker() {
		return R.ok(auditorService.fetchUserRolePicker());
	}

	/**
	 * 查询租户所有用户
	 */
	@PostMapping("/list/user-ids")
	public R listUsersWidthUserIds(@RequestBody List<Long> userIds) {
		return R.ok(auditorService.listUsersWidthUserIds(userIds));
	}

	/**
	 * 查询租户所有用户
	 */
	@GetMapping("/list/user-ids")
	public R listUsersWidthUserIds(){
		return R.ok(auditorService.listUsersWidthUserIds(null));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/list/roles")
	public R listRoles() {
		return R.ok(auditorService.listRoles());
	}

	/**
	 * 查询所有用户
	 */
	@GetMapping("/list/users")
	public R listUsers(){
		return R.ok(auditorService.listUsers());
	}

	/**
	 * 查询全部岗位
	 */
	@GetMapping("/list/posts")
	public R listPosts() {
		return R.ok(auditorService.listPosts());
	}

	/**
	 * 查询全部部门
	 */
	@GetMapping("/list/depts")
	public R listDepts() {
		return R.ok(auditorService.listDepts());
	}

}
