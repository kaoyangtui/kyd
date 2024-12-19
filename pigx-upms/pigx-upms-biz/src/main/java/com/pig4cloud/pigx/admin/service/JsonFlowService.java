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

package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.vo.UserVO;

import java.util.List;

/**
 * JsonFlow统一接口
 * @author luolin
 * @date 2024/1/28
 */
public interface JsonFlowService extends IService<SysUser> {

	// ---------------- 用户接口 ----------------
	/**
	 * 批量获取用户
	 * @param userIds 用户ID集合
	 * @return 用户信息
	 */
	List<SysUser> listUsersByUserIds(List<Long> userIds);

	/**
	 * 获取用户
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	SysUser getByUserId(Long userId);

	/**
	 * 获取用户岗位
	 * @param userId 用户ID
	 * @return 岗位信息
	 */
	List<SysPost> listPostsByUserId(Long userId);

	/**
	 * 查询租户所有用户
	 * @param userIds 用户ID集合
	 * @return 用户信息
	 */
	List<SysUser> listUsersWidthUserIds(List<Long> userIds);

	/**
	 * 查询用户集合
	 */
	List<SysUser> listUsers();

	// ---------------- 角色接口 ----------------
	/**
	 * 通过角色ID查询用户集合
	 * @param roleIds 角色ID
	 * @return 用户信息
	 */
	List<UserVO> listUserRolesByRoleIds(List<Long> roleIds);
	/**
	 * 通过岗位ID查询用户集合
	 * @param postIds 岗位ID
	 * @return 用户信息
	 */
	List<UserVO> listUserPostsByPostIds(List<Long> postIds);
	/**
	 * 通过部门ID查询用户集合
	 * @param deptIds 部门ID
	 * @return 用户信息
	 */
	List<UserVO> listUserDeptsByDeptIds(List<Long> deptIds);

	/**
	 * 通过角色ID查询用户集合
	 * @param roleId 角色ID
	 * @return 用户信息
	 */
	List<SysUser> listUsersByRoleId(Long roleId);

	/**
	 * 通过岗位ID查询用户集合
	 * @param postId 岗位ID
	 * @return 用户信息
	 */
	List<SysUser> listUsersByPostId(Long postId);

	/**
	 * 通过部门ID查询用户集合
	 * @param deptId 部门ID
	 * @return 用户信息
	 */
	List<SysUser> listUsersByDeptId(Long deptId);

	/**
	 * 通过角色ID集合查询角色集合
	 * @param roleIds 角色ID集合
	 */
	List<SysRole> listRolesByRoleIds(List<Long> roleIds);

	/**
	 * 通过岗位ID集合查询岗位集合
	 * @param postIds 岗位ID集合
	 */
	List<SysPost> listPostsByPostIds(List<Long> postIds);

	/**
	 * 通过部门ID集合查询部门集合
	 * @param deptIds 部门ID集合
	 */
	List<SysDept> listDeptsByDeptIds(List<Long> deptIds);

	/**
	 * 查询角色集合
	 */
	List<SysRole> listRoles();

	/**
	 * 查询岗位集合
	 */
	List<SysPost> listPosts();

	// ---------------- 部门接口 ----------------
	/**
	 * 查询部门树菜单
	 * @param deptName 部门名称
	 * @param parentId 父级部门ID
	 */
	List<Tree<Long>> getDeptTree(String deptName, Long parentId);

	/**
	 * 查询部门集合
	 */
	List<SysDept> listDepts();

	/**
	 * 获取部门负责人
	 * @param deptId deptId
	 */
	List<Long> getDeptAllLeader(Long deptId);

	/**
	 * 通过ID查询
	 * @param deptId ID
	 */
	SysDept getByDeptId(Long deptId);

}
