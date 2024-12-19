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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.order.api.entity.AskLeave;
import com.pig4cloud.pigx.order.api.vo.AskLeaveVO;
import com.pig4cloud.pigx.order.base.OrderCommonService;

/**
 * 请假工单
 *
 * @author luolin
 * @date 2020-08-25 10:55:19
 */
public interface AskLeaveService extends OrderCommonService<AskLeave> {

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param askLeave 请假工单
	 * @param queryTime 查询时间
	 */
	IPage<AskLeave> getPage(Page<AskLeave> page, AskLeave askLeave, String[] queryTime);

	/**
	 * 保存工单并启动流程
	 * @param askLeaveVO 请假工单
	 */
	Boolean saveOrUpdate(AskLeaveVO askLeaveVO);

	/**
	 * 暂存工单
	 * @param askLeaveVO 请假工单
	 */
	Boolean tempStore(AskLeaveVO askLeaveVO);

	/**
	 * 更新工单
	 * @param askLeaveVO 请假工单
	 */
	Boolean updateOrder(AskLeaveVO askLeaveVO);

	/**
	 * 删除工单
	 * @param id 主键
	 */
	Boolean removeOrder(Long id);

}
