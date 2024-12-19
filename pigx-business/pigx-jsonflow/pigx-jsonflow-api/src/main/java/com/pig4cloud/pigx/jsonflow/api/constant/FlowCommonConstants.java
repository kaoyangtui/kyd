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
 * 公共常量池
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface FlowCommonConstants {

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;

	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * 登录用户名
	 */
	String USERID = "USER-ID";

	String INVALID_JOB = "已作废的任务";

	String AUTO_LAYOUT = "TB";

	// 自动审批/结束
	String AUTO_AUDIT = "自动审批/结束";
	String AUDITING = "审批中";

	// 是否是更新表单信息
	String IS_UPDATE_FORM_DATA = "isUpdateFormData";
	// 流程条件与人员参数
	String FLOW_VAR_USER = "flowVarUser";
	// 流程人员参数
	String FLOW_USERS = "flowUsers";

	String CODE = "code";
	String MSG = "msg";
	String DATA = "data";

	/**
	 * 是否开启
	 */
	String IS_ENABLED = "true";

	/**
	 * 是
	 */
	String YES = "1";

	/**
	 * 否
	 */
	String NO = "0";

}
