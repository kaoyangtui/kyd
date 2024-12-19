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
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.order.api.entity.FlowApplication;
import com.pig4cloud.pigx.order.api.vo.FlowApplicationVO;

import java.util.List;

/**
 * 工单申请
 *
 * @author luolin
 * @date 2022-09-17 00:58:32
 */
public interface FlowApplicationService extends IService<FlowApplication> {

	/**
	 * 保存或修改办公申请
	 * @param flowApplicationVO 办公申请
	 * @return FlowApplication
	 */
	FlowApplication saveOrUpdate(FlowApplicationVO flowApplicationVO);

	/**
	 * 查询工单分组名称
	 * @return List
	 */
	List<FlowApplication> listByPerms(FlowApplication flowApplication);

}
