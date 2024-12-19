package com.pig4cloud.pigx.jsonflow.api.constant.enums;
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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 特殊的审批页面
 * @author luolin
 * @date 2022/9/16
 *
 */
@Getter
@AllArgsConstructor
public enum TabsOptionEnum {
    /**
     * 查看审批过程
     */
    COMMENT("1", "查看审批过程"),

    /**
     * 查看流程图
     */
    FLOW_PHOTO("2", "查看流程图"),

    /**
     * 修改办公申请工单
     */
    EDIT_RUN_APPLICATION("3", "修改办公申请工单"),

    /**
     * 查看办公申请工单
     */
    VIEW_RUN_APPLICATION("4", "查看办公申请工单");

	/**
	 * 类型
	 */
	private final String id;
	/**
	 * 描述
	 */
	private final String desc;
}
