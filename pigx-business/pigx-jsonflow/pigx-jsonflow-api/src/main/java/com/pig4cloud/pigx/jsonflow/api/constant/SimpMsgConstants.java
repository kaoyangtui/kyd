package com.pig4cloud.pigx.jsonflow.api.constant;

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
 * WS消息 公共常量
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface SimpMsgConstants {

	// 发起
	String LAUNCH = "(发起)";

	String FIRST = " 由 ";

	String INITIATE = " 发起, 请悉知";

	String CHARGE_MONEY = ", 收款金额为 ";

	String UNIT = " 元";

	// 流程中
	String NEW_NODE = "(审批)您有新的待办任务";

	String INITIATOR_ID = ", 发起人: ";

	String ORDER_TYPE = ",工单类型: ";

	String HANDLE_NODE = ",审批节点: ";

	// 结束
	String OWN_NODE = "您发起的: ";

	String END_NODE = "已完成, 请悉知";

}
