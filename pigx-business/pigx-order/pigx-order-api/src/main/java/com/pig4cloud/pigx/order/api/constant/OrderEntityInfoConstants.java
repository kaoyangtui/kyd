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
 * Order 工单实体类公共常量
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface OrderEntityInfoConstants {

    // 工单ID
    String ID = "id";

    // 流程信息
    String DEF_FLOW_ID = "defFlowId";
    String FLOW_INST_ID = "flowInstId";
    String FLOW_KEY = "flowKey";

	// 工单信息
    String CODE = "code";
    String FORM_ID = "formId";
	String FORM_NAME = "formName";
    String FORM_DATA = "formData";
    String RUN_FORM_ID = "runFormId";
    String TABLE_NAME = "tableName";
    String STATUS = "status";

    // 工单发起人
    String CREATE_USER = "createUser";
	String CREATE_TIME = "createTime";

    // 类型
    String TYPE = "type";

}
