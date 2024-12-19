package com.pig4cloud.pigx.jsonflow.api.support;

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


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单权限
 *
 * @author luolin
 * @date 2021-02-23 13:52:11
 */
@Data
@Schema(description = "表单权限")
public class FormPermsVO implements Serializable {
    private static final long serialVersionUID = 1L;

	/**
	 * 表单tabsId
	 */
	@Schema(description = "表单tabsId")
	private Long formTabsId;

	/**
	 * 流程权限
	 */
	private List<FormPermsFlowVO> flowList = new ArrayList<>();

}
