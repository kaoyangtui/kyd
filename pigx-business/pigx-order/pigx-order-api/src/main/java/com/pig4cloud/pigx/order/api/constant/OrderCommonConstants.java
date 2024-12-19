package com.pig4cloud.pigx.order.api.constant;

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

/**
 * Order 工单公共常量
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface OrderCommonConstants {

	/**
	 * 默认存表名称
	 */
	String DEF_TABLE_NAME = "order_run_application";

	/**
	 * 默认实现
	 */
	String DEF_SERVICE_IMPL = "runApplicationServiceImpl";

    // 待办任务ID
    String RUN_JOB_ID = "runJobId";

    // KEY集合
    String FLOW_KEYS = "flowKeys";
	// 流程ID集合
	String FLOW_INST_IDS = "flowInstIds";

    // topic
    String TOPIC = "topic";

}
