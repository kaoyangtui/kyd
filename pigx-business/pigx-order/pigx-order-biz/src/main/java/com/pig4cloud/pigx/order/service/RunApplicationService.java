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
package com.pig4cloud.pigx.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.order.api.entity.RunApplication;
import com.pig4cloud.pigx.order.api.vo.RunApplicationVO;
import com.pig4cloud.pigx.order.base.OrderCommonService;
import com.pig4cloud.pigx.order.table.TableCrudModelEnum;

/**
 * 我的工单
 *
 * @author luolin
 * @date 2022-09-17 00:58:49
 */
public interface RunApplicationService extends OrderCommonService<RunApplication> {

	/**
	 * 分页查询
	 *
	 * @param page      分页对象
	 * @param runApplication  请假工单
	 * @param queryTime 查询时间
	 */
	IPage<RunApplication> getPage(Page<RunApplication> page, RunApplication runApplication, String[] queryTime);

	/**
	 * 保存工单并启动流程
	 *
	 * @param runApplicationVO 我的工单
	 */
    Boolean saveOrUpdate(RunApplicationVO runApplicationVO);

	/**
	 * 暂存工单
	 *
	 * @param runApplicationVO 请假工单
	 */
	Boolean tempStore(RunApplicationVO runApplicationVO);

	/**
	 * 更新工单
	 *
	 * @param runApplicationVO 请假工单
	 */
	Boolean updateOrder(RunApplicationVO runApplicationVO);

	/**
	 * 删除工单
	 *
	 * @param id 主键
	 */
	Boolean removeOrder(Long id);

	/**
	 * 获取工单
	 *
	 * @param id 主键
	 */
	RunApplication getOrderById(Long id);

	/**
	 * 表数据CRUD操作
	 * TODO 查询、修改、删除条件暂时为表单ID
	 * @param runApplication 工单信息
	 * @param model 模式
	 */
	void doTableCrudHandle(RunApplication runApplication, TableCrudModelEnum model);

	/**
	 * 获取工单
	 *
	 * @param flowInstId id
	 */
	RunApplication getByFlowInstId(Long flowInstId);

}
