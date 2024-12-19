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
 * 异常抛出文字描述
 * @author luolin
 * @date 2021/4/1 17:04
 */
public interface ValidationExceptions {

    //----------------分配参与者校验字段----------------

    String NO_USER_KEY = "分配参与者参数无userKey";

    String NO_FLOWINSTID = "分配参与者参数无流程ID";

    String NO_FLOW_KEY = "分配参与者参数无工单流程KEY";

    String NO_FLOW_CODE = "分配参与者参数无工单流程CODE";

    String NO_ROLE_ID = "分配参与者参数无参与者角色ID";

    String NO_PERMIT_UPDATE = "该记录由他人创建，您无权修改";

    String NO_JOB_TYPE = "分配参与者参数无参与者类型";

    String NO_USER_KEY_JOB = "不存在指定userKey的任务";

    String DIST_VAL_TYPE = "当前节点任务为非分配类型的任务，不允许单独分配参与者";

    //----------------流程条件校验字段----------------

    String PLEASE_TO_FLOW_NODE = "请选择到达节点";

    String NOT_FROM_FLOW_NODE = "来源节点不能是虚拟节点或结束节点";

    String TO_FROM_FLOW_NODE = "下一节点不能是虚拟节点或开始节点";

}
