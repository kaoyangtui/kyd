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
 * Order 工单流程参数公共常量
 *
 * TODO 注意：理论上KEY名称存在【下划线】则表示参与者KEY，如create_user，否则为流程条件
 * TODO 注意：办理人员或角色支持数组
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface FlowVariableConstants {

    // 设置全局发起人
    String CREATE_USER = "create_user";

	// 工作交接工单参数
	// 是否需要接收人接收
	String IS_NEED_RECEIVE = "isNeedReceive";
	// 接收人确认接收
	String RECEIVE_USER = "receive_user";

	// 请假工单参数
	// 请假天数
	String LEAVE_DAYS = "leaveDays";
	// 测试分配多个参与者
	String TEST_ROLE_USER = "test_role_user";

}
