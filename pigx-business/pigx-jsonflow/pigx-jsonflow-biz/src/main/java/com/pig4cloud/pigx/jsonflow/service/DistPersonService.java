package com.pig4cloud.pigx.jsonflow.service;

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
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;

import java.util.List;
import java.util.Map;

/**
 * 分配参与者
 *
 * @author luolin
 * @date 2021-02-23 14:12:22
 */
public interface DistPersonService extends IService<DistPerson> {

	/**
	 * 发起工单时动态分配参与者
	 *
	 * @param params 参数
	 */
	boolean recordDistPersons(Map<String, Object> params);

	/**
	 * 修改工单时动态分配参与者
	 *
	 * @param params 参数
	 */
	boolean updateDistPersons(Map<String, Object> params);

	/**
	 * 流程中获取分配参与者信息
	 *
	 * @param distPersonVO 分配参与者
	 */
	DistPersonVO getByFlowInstId(DistPersonVO distPersonVO);

	/**
	 * 更新单个分配的办理人：动态分配或动态计算
	 *
	 * @param distPersonVO 分配参与者
	 */
	boolean saveOrUpdate(DistPersonVO distPersonVO);

	/**
	 * 流程中保存分配参与者信息：正常分配或动态分配或动态计算
	 *
	 * @param distPersonVO 分配参与者
	 */
	boolean saveByFlowInstId(DistPersonVO distPersonVO);

	/**
	 * 增量任务动态计算保存分配参与者
	 * 增量任务动态计算保存分配参与者，一般由上一节点监听触发业务侧来调用
	 *
	 * @param distPersonVO 分配参与者
	 */
	boolean incrementCalculate(DistPersonVO distPersonVO);

	/**
	 * 根据分配的办理人更新为接收人
	 *
	 * @param handoverUser 交接人
	 * @param receiveUser  接收人
	 * @param flowInstIds  流程实例ID集合
	 */
	boolean handleJobHandover(Long handoverUser, Long receiveUser, List<Long> flowInstIds);

}
