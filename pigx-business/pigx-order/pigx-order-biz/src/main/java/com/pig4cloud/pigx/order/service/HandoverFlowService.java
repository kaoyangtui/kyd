package com.pig4cloud.pigx.order.service;
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
import com.pig4cloud.pigx.order.api.entity.HandoverFlow;
import com.pig4cloud.pigx.order.api.vo.HandoverFlowVO;
import com.pig4cloud.pigx.order.base.OrderCommonService;

import java.util.List;
import java.util.Map;

/**
 * 交接流程
 *
 * @author luolin
 * @date 2020-04-26 10:35:55
 */
public interface HandoverFlowService extends OrderCommonService<HandoverFlow> {

	/**
	 * 保存工单并启动流程
	 *
	 * @param handoverFlowVO 交接流程
	 */
	Boolean saveOrUpdate(HandoverFlowVO handoverFlowVO);

	/**
	 * 暂存工单
	 *
	 * @param handoverFlowVO 交接流程
	 */
	Boolean tempStore(HandoverFlowVO handoverFlowVO);

	/**
	 * 更新工单
	 *
	 * @param handoverFlowVO 交接流程
	 */
	Boolean updateOrder(HandoverFlowVO handoverFlowVO);

	/**
	 * 删除工单
	 *
	 * @param id 主键
	 */
	Boolean removeOrder(Long id);

	/**
	 * 删除工单
	 *
	 * @param ids 主键集合
	 */
	Boolean removeOrder(List<Long> ids);

	/**
	 * 分配参与者
	 *
	 * @param handoverFlowVO 交接流程 工单
	 * @return R
	 */
	Boolean distributePerson(HandoverFlowVO handoverFlowVO);

	/**
	 * 驳回选中项
	 *
	 * @param handoverFlowVO 交接流程
	 */
	Boolean checkToReject(HandoverFlowVO handoverFlowVO);

	/**
	 * 确认接收
	 *
	 * @param handoverFlowVO 交接流程
	 */
	Boolean confirmReceive(HandoverFlowVO handoverFlowVO);

	/**
	 * 作废时更新工作交接任务状态
	 *
	 * @param order 工单参数
	 */
	Boolean updateRecordStatus(Map<String, Object> order);
}
