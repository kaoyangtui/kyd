package com.pig4cloud.pigx.admin.constants;
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
 * 流程状态
 * @author luolin
 * @date 2018/9/30
 */
@Getter
@AllArgsConstructor
public enum FlowStatusEnum {

    /**
     * 撤回
     */
    RECALL(-2, "撤回"),
    /**
     * 发起
     */
    INITIATE(-1, "发起"),
    /**
     * 运行中
     */
    RUN(0, "运行中"),

    /**
     * 完结
     */
    FINISH(1, "完结"),

    /**
     * 流程作废
     */
    INVALID(2, "作废"),
    /**
     * 流程终止
     */
    TERMINATE(3, "终止");

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String description;
}
