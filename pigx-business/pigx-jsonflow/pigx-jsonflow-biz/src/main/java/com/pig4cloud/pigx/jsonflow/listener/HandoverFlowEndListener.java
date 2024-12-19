package com.pig4cloud.pigx.jsonflow.listener;

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

import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.support.HandleOrderRelativeInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作交接工单结束时监听事件
 * 更新子表交接任务状态：日常交接、任务交接、公共交接
 *
 * @author luolin
 * @date 2021/3/19 15:02
 */
@Slf4j
@Component
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HandoverFlowEndListener implements GlobalFlowListener {

    // @GlobalTransactional
    @Override
    public void finish(RunJobVO runJobVO) {
		Long flowInstId = runJobVO.getFlowInstId();
		// 更新工作交接工单及附表状态
		HandleOrderRelativeInfo.updateOrderStatus(flowInstId, HandoverFlowStatusEnum.COMPLETED.getStatus());
    }

}
