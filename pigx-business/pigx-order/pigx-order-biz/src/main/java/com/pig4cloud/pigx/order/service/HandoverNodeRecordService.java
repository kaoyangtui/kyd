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
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.order.api.entity.HandoverNodeRecord;

import java.util.List;

/**
 * 交接任务记录
 *
 * @author luolin
 * @date 2020-04-26 10:45:55
 */
public interface HandoverNodeRecordService extends IService<HandoverNodeRecord> {

	/**
	 * 批量新增或修改交接任务记录
	 * @param handoverNodeRecords 交接任务记录
	 */
	List<HandoverNodeRecord> batchSaveOrUpdate(List<HandoverNodeRecord> handoverNodeRecords);

	/**
	 * 新增或修改交接任务记录
	 * @param handoverNodeRecord 交接任务记录
	 */
	HandoverNodeRecord saveOrUpdateByType(HandoverNodeRecord handoverNodeRecord);

	/**
	 * 根据工单ID更新状态
	 * @param orderId 工单ID
	 * @param status 状态
	 */
	boolean updateByOrderId(Long orderId, String status);

}
