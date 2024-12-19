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
 * Order 工单数据库表公共常量
 *
 * @author luolin
 * @date 2020/2/17
 */
public interface OrderTableInfoConstants {
    // 工单ID
    String ID = "id";

    // 工单流程ID
    String FLOW_INST_ID = "flow_inst_id";

    // 工单发起人ID
    String CREATE_USER = "create_user";

    String STATUS = "status";

    String FINISH_TIME = "finish_time";

    // 交接人
    String HANDOVER_USER = "handover_user";

    String FLOW_KEY = "flow_key";

    String CREATE_TIME = "CREATE_TIME";

}
