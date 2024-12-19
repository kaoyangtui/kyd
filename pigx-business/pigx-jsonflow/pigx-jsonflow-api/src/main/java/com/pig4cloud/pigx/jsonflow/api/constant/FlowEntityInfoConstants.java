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
 * 工作流实体类公共常量
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface FlowEntityInfoConstants {

    // 工单ID
    String ID = "id";

	// 流程名称
	String FLOW_NAME = "flowName";

	String GROUP_NAME = "groupName";

	// 排序
	String SORT = "sort";

	// 版本
	String VERSION = "version";

	// 备注
	String REMARK = "remark";

    // 流程信息
    String FLOW_INST_ID = "flowInstId";
	String DEF_FLOW_ID = "defFlowId";
	String FLOW_KEY = "flowKey";

    // 工单ID
    String ORDER_ID = "orderId";

    String CODE = "code";

	String FORM_ID = "formId";
	String FORM_DATA = "formData";

	String FINISH_TIME = "finishTime";

    String STATUS = "status";

    String NODE_JOB_ID = "nodeJobId";

    String USER_ID = "userId";

    // 创建人
    String CREATE_USER = "createUser";
    String CREATE_TIME = "createTime";

    // 开始时间
    String START_TIME = "startTime";

    // 结束时间
    String END_TIME = "endTime";

}
