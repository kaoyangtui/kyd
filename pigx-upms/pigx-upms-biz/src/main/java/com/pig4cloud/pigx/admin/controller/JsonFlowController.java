/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.service.JsonFlowService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
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
 * @author luolin
 * @date 2024/1/28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/json-flow")
@Tag(description = "JsonFlow", name = "JsonFlow统一接口")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class JsonFlowController {

	private final JsonFlowService jsonFlowService;

	// ---------------- 用户接口 ----------------
	/**
	 * 批量获取用户
	 */
	@Inner
	@GetMapping("/user/list/users")
	public R<List<SysUser>> listUsersByUserIds(@RequestParam List<Long> userIds) {
		return R.ok(jsonFlowService.listUsersByUserIds(userIds));
	}

	/**
	 * 通过ID查询用户
	 * @param userId ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/user/id/{userId}")
	public R getByUserId(@PathVariable Long userId) {
		return R.ok(jsonFlowService.getByUserId(userId));
	}

	/**
	 * 通过ID查询用户岗位
	 * @param userId ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/post/id/{userId}")
	public R listPostsByUserId(@PathVariable Long userId) {
		return R.ok(jsonFlowService.listPostsByUserId(userId));
	}

	/**
	 * 查询用户
	 */
	@PostMapping("/user/list/user-ids")
	public R listUsersWidthUserIds(@RequestBody List<Long> userIds) {
		return R.ok(jsonFlowService.listUsersWidthUserIds(userIds));
	}

	/**
	 * 查询租户所有用户
	 */
	@GetMapping("/user/list")
	public R listUsers() {
		return R.ok(jsonFlowService.listUsers());
	}

	// ---------------- 角色接口 ----------------
	/**
	 * 通过角色ID查询用户集合
	 * @param roleIds 角色ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/role/list-users/role-ids")
	public R<List<UserVO>> listUserRolesByRoleIds(@RequestParam List<Long> roleIds) {
		return R.ok(jsonFlowService.listUserRolesByRoleIds(roleIds));
	}

	/**
	 * 通过岗位ID查询用户集合
	 * @param postIds 岗位ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/post/list-users/post-ids")
	public R<List<UserVO>> listUserPostsByPostIds(@RequestParam List<Long> postIds) {
		return R.ok(jsonFlowService.listUserPostsByPostIds(postIds));
	}

	/**
	 * 通过部门ID查询用户集合
	 * @param deptIds 部门ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/dept/list-users/dept-ids")
	public R<List<UserVO>> listUserDeptsByDeptIds(@RequestParam List<Long> deptIds) {
		return R.ok(jsonFlowService.listUserDeptsByDeptIds(deptIds));
	}

	/**
	 * 通过角色ID查询用户集合
	 * @param roleId 角色ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/role/list-users/{roleId}")
	public R<List<SysUser>> listUsersByRoleId(@PathVariable Long roleId) {
		return R.ok(jsonFlowService.listUsersByRoleId(roleId));
	}
	/**
	 * 通过岗位ID查询用户集合
	 * @param postId 岗位ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/post/list-users/{postId}")
	public R<List<SysUser>> listUsersByPostId(@PathVariable Long postId) {
		return R.ok(jsonFlowService.listUsersByPostId(postId));
	}
	/**
	 * 通过部门ID查询用户集合
	 * @param deptId 部门ID
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/dept/list-users/{deptId}")
	public R<List<SysUser>> listUsersByDeptId(@PathVariable Long deptId) {
		return R.ok(jsonFlowService.listUsersByDeptId(deptId));
	}

	/**
	 * 批量获取角色
	 */
	@Inner
	@GetMapping("/role/list/roles")
	public R<List<SysRole>> listRolesByRoleIds(@RequestParam List<Long> roleIds) {
		return R.ok(jsonFlowService.listRolesByRoleIds(roleIds));
	}

	/**
	 * 批量获取岗位
	 */
	@Inner
	@GetMapping("/post/list/posts")
	public R<List<SysPost>> listPostsByPostIds(@RequestParam List<Long> postIds) {
		return R.ok(jsonFlowService.listPostsByPostIds(postIds));
	}

	/**
	 * 批量获取部门
	 */
	@Inner
	@GetMapping("/dept/list/depts")
	public R<List<SysDept>> listDeptsByDeptIds(@RequestParam List<Long> deptIds) {
		return R.ok(jsonFlowService.listDeptsByDeptIds(deptIds));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/role/list")
	public R listRoles() {
		return R.ok(jsonFlowService.listRoles());
	}

	/**
	 * 获取岗位列表
	 * @return 岗位列表
	 */
	@GetMapping("/post/list")
	public R listPosts() {
		return R.ok(jsonFlowService.listPosts());
	}

	// ---------------- 部门接口 ----------------
	/**
	 * 返回树形菜单集合
	 * @param deptName 部门名称
	 * @return 树形菜单
	 */
	@GetMapping(value = "/dept/tree")
	public R getDeptTree(String deptName, Long parentId) {
		return R.ok(jsonFlowService.getDeptTree(deptName, parentId));
	}

	/**
	 * 查询全部部门
	 */
	@GetMapping("/dept/list")
	public R listDepts() {
		return R.ok(jsonFlowService.listDepts());
	}

	/**
	 * 获取部门负责人
	 */
	@GetMapping(value = "/dept/leader/{deptId}")
	public R getDeptAllLeader(@PathVariable Long deptId) {
		return R.ok(jsonFlowService.getDeptAllLeader(deptId));
	}

	/**
	 * 通过ID查询
	 * @param deptId ID
	 */
	@GetMapping("/dept/{deptId}")
	public R getByDeptId(@PathVariable Long deptId) {
		return R.ok(jsonFlowService.getByDeptId(deptId));
	}

}
