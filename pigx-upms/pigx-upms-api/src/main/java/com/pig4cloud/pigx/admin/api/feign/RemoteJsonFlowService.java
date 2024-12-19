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

package com.pig4cloud.pigx.admin.api.feign;

import cn.hutool.core.lang.tree.Tree;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author luolin
 * @date 2024/1/28
 */
@FeignClient(contextId = "remoteJsonFlowService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteJsonFlowService {

	// ---------------- 用户接口 ----------------
	/**
	 * 批量获取用户
	 * @param userIds 用户ID集合
	 * @param from 是否内部
	 * @return
	 */
	@GetMapping("/json-flow/user/list/users")
	R<List<SysUser>> listUsersByUserIds(@RequestParam("userIds") List<Long> userIds,
									@RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过用户名查询用户
	 * @param userId 用户id
	 * @param from 调用标志
	 * @return R
	 */
	@GetMapping("/json-flow/user/id/{userId}")
	R<SysUser> getByUserId(@PathVariable("userId") Long userId, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过ID查询用户岗位
	 * @param userId ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/post/id/{userId}")
	R<List<SysPost>> listPostsByUserId(@PathVariable("userId") Long userId, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 查询租户所有用户
	 */
	@GetMapping("/json-flow/user/list")
	R<List<SysUser>> listUsers();

	/**
	 * 查询用户
	 */
	@PostMapping("/json-flow/user/list/user-ids")
	R<List<SysUser>> listUsersWidthUserIds(@RequestBody List<Long> userIds);

	// ---------------- 角色接口 ----------------
	/**
	 * 通过角色ID查询用户集合
	 * @param roleIds 角色ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/role/list-users/role-ids")
	R<List<UserVO>> listUserRolesByRoleIds(@RequestParam("roleIds") List<Long> roleIds,
									   @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过岗位ID查询用户集合
	 * @param postIds 岗位ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/post/list-users/post-ids")
	R<List<UserVO>> listUserPostsByPostIds(@RequestParam("postIds") List<Long> postIds,
										   @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过部门ID查询用户集合
	 * @param deptIds 部门ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/dept/list-users/dept-ids")
	R<List<UserVO>> listUserDeptsByDeptIds(@RequestParam("deptIds") List<Long> deptIds,
												  @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过角色ID查询用户集合
	 * @param roleId 角色ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/role/list-users/{roleId}")
	R<List<SysUser>> listUsersByRoleId(@PathVariable("roleId") Long roleId,
									   @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过岗位ID查询用户集合
	 * @param postId 岗位ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/post/list-users/{postId}")
	R<List<SysUser>> listUsersByPostId(@PathVariable("postId") Long postId,
											  @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过部门ID查询用户集合
	 * @param deptId 部门ID
	 * @return 用户信息
	 */
	@GetMapping("/json-flow/dept/list-users/{deptId}")
	R<List<SysUser>> listUsersByDeptId(@PathVariable("deptId") Long deptId,
											  @RequestHeader(SecurityConstants.FROM) String from);


	/**
	 * 批量获取角色
	 * @param roleIds 角色ID集合
	 * @return R
	 */
	@GetMapping("/json-flow/role/list/roles")
	R<List<SysRole>> listRolesByRoleIds(@RequestParam("roleIds") List<Long> roleIds,
									@RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 批量获取岗位
	 */
	@GetMapping("/json-flow/post/list/posts")
	R<List<SysPost>> listPostsByPostIds(@RequestParam("postIds") List<Long> postIds,
										@RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 批量获取部门
	 */
	@GetMapping("/json-flow/dept/list/depts")
	R<List<SysDept>> listDeptsByDeptIds(@RequestParam("deptIds") List<Long> deptIds,
										@RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 查询租户所有用角色
	 */
	@GetMapping("/json-flow/role/list")
	R<List<SysRole>> listRoles();

	/**
	 * 获取岗位列表
	 * @return 岗位列表
	 */
	@GetMapping("/json-flow/post/list")
	R<List<SysPost>> listPosts();

	// ---------------- 部门接口 ----------------
	/** 返回树形菜单集合
	 * @param deptName 部门名称
	 * @return 树形菜单
	 */
	@GetMapping(value = "/json-flow/dept/tree")
	R<List<Tree<String>>> getDeptTree(@RequestParam("deptName") String deptName, @RequestParam("parentId") Long parentId);

	/**
	 * 查询租户所有部门
	 */
	@GetMapping("/json-flow/dept/list")
	R<List<SysDept>> listDepts();

	/**
	 * 获取部门负责人
	 * @param deptId 部门id
	 */
	@GetMapping(value = "/json-flow/dept/leader/{deptId}")
	R<List<Long>> getDeptAllLeader(@PathVariable("deptId") Long deptId);

	/**
	 * 通过部门ID查询部门
	 * @param deptId 部门id
	 */
	@GetMapping("/json-flow/dept/{deptId}")
	R<SysDept> getByDeptId(@PathVariable("deptId") Long deptId);

}
