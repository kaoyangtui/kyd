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

package com.pig4cloud.pigx.jsonflow.api.vo;

import com.pig4cloud.pigx.admin.api.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 办理人/角色选择器数据
 * @author luolin
 * @date 2023/05/29
 */
@Data
@Schema(description = "办理人/角色选择器数据")
public class UserRolePickerVO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前层级部门ID
	 */
	@Schema(description = "当前层级部门ID")
	private Long id;

	/**
	 * 当前层级父级部门ID
	 */
	@Schema(description = "当前层级父级部门ID")
	private Long parentId = 0L;

	/**
	 * 当前层级部门名称
	 */
	@Schema(description = "当前层级部门名称")
	private String name;

	/**
	 * 当前层级部门排序
	 */
	@Schema(description = "当前层级部门排序")
	private Integer sort;

	 /**
	 * 当前层级下办理人/角色子选择器数据
	 */
	@Schema(description = "当前层级下办理人/角色子选择器数据")
	private List<UserRolePickerVO<T>> children = new ArrayList<>();

	/**
	 * 当前层级办理人数据
	 */
	@Schema(description = "当前层级办理人数据")
	private List<SysUser> users = new ArrayList<>();

	/**
	 * 当前层级办理人数据总数
	 */
	@Schema(description = "当前层级办理人数据总数")
	private Long usersCount = 0L;

}
