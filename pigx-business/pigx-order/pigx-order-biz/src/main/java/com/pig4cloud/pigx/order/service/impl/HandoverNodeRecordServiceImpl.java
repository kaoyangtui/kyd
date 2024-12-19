package com.pig4cloud.pigx.order.service.impl;

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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.order.HandoverFlowStatusEnum;
import com.pig4cloud.pigx.order.api.entity.HandoverNodeRecord;
import com.pig4cloud.pigx.order.mapper.HandoverNodeRecordMapper;
import com.pig4cloud.pigx.order.service.HandoverNodeRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 交接任务记录
 *
 * @author luolin
 * @date 2020-04-26 10:45:55
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HandoverNodeRecordServiceImpl extends ServiceImpl<HandoverNodeRecordMapper, HandoverNodeRecord>
		implements HandoverNodeRecordService {

	@Override
	public List<HandoverNodeRecord> batchSaveOrUpdate(List<HandoverNodeRecord> handoverNodeRecords) {
		HandoverNodeRecord one = handoverNodeRecords.get(0);
		// 判断流程中修改
		this.handleFlow(one);
		List<Long> flowInstIds = handoverNodeRecords.stream().map(HandoverNodeRecord::getFlowInstId)
				.collect(Collectors.toList());
		List<Long> runJobIds = handoverNodeRecords.stream().map(HandoverNodeRecord::getRunJobId)
				.collect(Collectors.toList());
		// 先查询是否存在
		List<HandoverNodeRecord> exists = this.list(Wrappers.<HandoverNodeRecord>lambdaUpdate()
				.in(HandoverNodeRecord::getFlowInstId, flowInstIds).in(HandoverNodeRecord::getRunJobId, runJobIds)
				.eq(HandoverNodeRecord::getStatus, one.getStatus()));
		handoverNodeRecords.forEach(f -> {
			// 判断是否存在
			if (Objects.isNull(f.getId())) {
				exists.stream().filter(
						e -> e.getFlowInstId().equals(f.getFlowInstId()) && e.getRunJobId().equals(f.getRunJobId()))
						.findAny().ifPresent(e -> f.setId(e.getId()));
			}
			// 判断流程中修改
			this.handleFlow(f);
		});
		this.saveOrUpdateBatch(handoverNodeRecords);
		return handoverNodeRecords;
	}

	/**
	 * 判断流程中修改
	 * @param f 任务记录
	 */
	private void handleFlow(HandoverNodeRecord f) {
		if (StrUtil.isEmpty(f.getStatus()))
			f.setStatus(HandoverFlowStatusEnum.NO_HANDOVER.getStatus());
		if (Objects.isNull(f.getCreateUser()))
			f.setCreateUser(SecurityUtils.getUser().getId());
	}

	@Override
	public HandoverNodeRecord saveOrUpdateByType(HandoverNodeRecord handoverNodeRecord) {
		// 判断流程中修改
		if (StrUtil.isEmpty(handoverNodeRecord.getStatus()))
			handoverNodeRecord.setStatus(HandoverFlowStatusEnum.NO_HANDOVER.getStatus());
		if (Objects.isNull(handoverNodeRecord.getCreateUser()))
			handoverNodeRecord.setCreateUser(SecurityUtils.getUser().getId());
		// 判断同流程同节点任务
		this.saveOrUpdate(handoverNodeRecord,
				Wrappers.<HandoverNodeRecord>lambdaUpdate()
						.eq(HandoverNodeRecord::getFlowInstId, handoverNodeRecord.getFlowInstId())
						.eq(HandoverNodeRecord::getRunJobId, handoverNodeRecord.getRunJobId())
						.eq(HandoverNodeRecord::getStatus, handoverNodeRecord.getStatus()));
		// 首次保存后未发起
		if (Objects.isNull(handoverNodeRecord.getId())) {
			handoverNodeRecord = this.getOne(Wrappers.<HandoverNodeRecord>lambdaUpdate()
					.eq(HandoverNodeRecord::getFlowInstId, handoverNodeRecord.getFlowInstId())
					.eq(HandoverNodeRecord::getRunJobId, handoverNodeRecord.getRunJobId())
					.eq(HandoverNodeRecord::getStatus, handoverNodeRecord.getStatus()));
		}
		return handoverNodeRecord;
	}

	@Override
	public boolean updateByOrderId(Long orderId, String status) {
		this.lambdaUpdate().set(HandoverNodeRecord::getStatus, status)
				.eq(HandoverNodeRecord::getOrderId, orderId)
				.update();
		return false;
	}

}
