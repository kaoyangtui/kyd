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

package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.entity.SysUserPost;
import com.pig4cloud.pigx.admin.api.entity.SysUserRole;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.mapper.JsonFlowMapper;
import com.pig4cloud.pigx.admin.mapper.SysUserPostMapper;
import com.pig4cloud.pigx.admin.mapper.SysUserRoleMapper;
import com.pig4cloud.pigx.admin.service.JsonFlowService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysPostService;
import com.pig4cloud.pigx.admin.service.SysRoleService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JsonFlow统一接口
 * @author luolin
 * @date 2024/1/28
 */
@Slf4j
@Service
@AllArgsConstructor
public class JsonFlowServiceImpl extends ServiceImpl<JsonFlowMapper, SysUser> implements JsonFlowService {

	private final SysRoleService sysRoleService;

	private final SysDeptService sysDeptService;

	private final SysUserRoleMapper sysUserRoleMapper;

	private final SysUserPostMapper sysUserPostMapper;

	private final SysPostService sysPostService;

	// ---------------- 用户接口 ----------------

	@Override
	public List<SysUser> listUsersByUserIds(List<Long> userIds) {
		return this.listByIds(userIds);
	}

	@Override
	public SysUser getByUserId(Long userId) {
		return this.getById(userId);
	}

	@Override
	public List<SysPost> listPostsByUserId(Long userId) {
		List<SysUserPost> userPosts = sysUserPostMapper
				.selectList(Wrappers.<SysUserPost>lambdaQuery().in(SysUserPost::getUserId, userId));
		List<Long> list = userPosts.stream().map(SysUserPost::getPostId).collect(Collectors.toList());
		if (CollUtil.isEmpty(list)) return Collections.emptyList();
		return sysPostService.list(Wrappers.<SysPost>lambdaQuery().in(SysPost::getPostId, list));
	}

	@Override
	public List<SysUser> listUsersWidthUserIds(List<Long> userIds) {
		List<SysUser> sysUsers;
		if (CollUtil.isEmpty(userIds)) sysUsers = this.list(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getLockFlag, CommonConstants.STATUS_NORMAL));
		else sysUsers = this.listByIds(userIds.stream().distinct().collect(Collectors.toList()));
		return sysUsers;
	}

	@Override
	public List<SysUser> listUsers() {
		return this.list();
	}

	// ---------------- 角色接口 ----------------
	@Override
	public List<UserVO> listUserRolesByRoleIds(List<Long> roleIds) {
		List<SysUserRole> userRoles = sysUserRoleMapper
				.selectList(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getRoleId, roleIds));
		List<Long> list = userRoles.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
		if (CollUtil.isEmpty(list)) return Collections.emptyList();
		return this.listByIds(list).stream().map(m -> {
			UserVO userVO = new UserVO();
			BeanUtil.copyProperties(m, userVO);
			List<Long> userRoleIds = userRoles.stream().filter(f -> f.getUserId().equals(m.getUserId()))
					.map(SysUserRole::getRoleId).collect(Collectors.toList());
			List<SysRole> sysRoles = sysRoleService.listByIds(userRoleIds);
			userVO.setRoleList(sysRoles);
			return userVO;
		}).collect(Collectors.toList());
	}

	@Override
	public List<UserVO> listUserPostsByPostIds(List<Long> postIds) {
		List<SysUserPost> userPosts = sysUserPostMapper
				.selectList(Wrappers.<SysUserPost>lambdaQuery().in(SysUserPost::getPostId, postIds));
		List<Long> list = userPosts.stream().map(SysUserPost::getUserId).collect(Collectors.toList());
		if (CollUtil.isEmpty(list)) return Collections.emptyList();
		return this.listByIds(list).stream().map(m -> {
			UserVO userVO = new UserVO();
			BeanUtil.copyProperties(m, userVO);
			List<Long> userPostIds = userPosts.stream().filter(f -> f.getUserId().equals(m.getUserId()))
					.map(SysUserPost::getPostId).collect(Collectors.toList());
			List<SysPost> sysPosts = sysPostService.listByIds(userPostIds);
			userVO.setPostList(sysPosts);
			return userVO;
		}).collect(Collectors.toList());
	}

	@Override
	public List<UserVO> listUserDeptsByDeptIds(List<Long> deptIds) {
		List<SysUser> sysUsers = this.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getDeptId, deptIds));
		return sysUsers.stream().map(m -> {
			UserVO userVO = new UserVO();
			BeanUtil.copyProperties(m, userVO);
			return userVO;
		}).collect(Collectors.toList());
	}

	@Override
	public List<SysUser> listUsersByRoleId(Long roleId) {
		List<Long> list = sysUserRoleMapper
				.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, roleId)).stream()
				.map(SysUserRole::getUserId).collect(Collectors.toList());
		if (CollUtil.isEmpty(list)) return Collections.emptyList();
		return this.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, list));
	}

	@Override
	public List<SysUser> listUsersByPostId(Long postId) {
		List<Long> list = sysUserPostMapper
				.selectList(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getPostId, postId)).stream()
				.map(SysUserPost::getUserId).collect(Collectors.toList());
		if (CollUtil.isEmpty(list)) return Collections.emptyList();
		return this.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, list));
	}

	@Override
	public List<SysUser> listUsersByDeptId(Long deptId) {
		return this.list(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, deptId));
	}

	@Override
	public List<SysRole> listRolesByRoleIds(List<Long> roleIds) {
		return sysRoleService.listByIds(roleIds);
	}

	@Override
	public List<SysPost> listPostsByPostIds(List<Long> postIds) {
		return sysPostService.listByIds(postIds);
	}

	@Override
	public List<SysDept> listDeptsByDeptIds(List<Long> deptIds) {
		return sysDeptService.listByIds(deptIds);
	}

	@Override
	public List<SysRole> listRoles() {
		return sysRoleService.list(Wrappers.emptyWrapper());
	}

	@Override
	public List<SysPost> listPosts() {
		return sysPostService.list(Wrappers.emptyWrapper());
	}

	// ---------------- 部门接口 ----------------
	@Override
	public List<Tree<Long>> getDeptTree(String deptName, Long parentId) {
		return sysDeptService.selectTree(deptName, parentId);
	}

	@Override
	public List<SysDept> listDepts() {
		return sysDeptService.list();
	}

	@Override
	public List<Long> getDeptAllLeader(Long deptId) {
		return sysDeptService.listDeptLeader(deptId);
	}

	@Override
	public SysDept getByDeptId(Long deptId) {
		return sysDeptService.getById(deptId);
	}

}
