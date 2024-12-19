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
import com.pig4cloud.pigx.jsonflow.api.entity.DefFlow;
import com.pig4cloud.pigx.jsonflow.api.vo.DefFlowVO;

/**
 * 流程定义管理
 *
 * @author luolin
 * @date 2021-02-23 13:52:11
 */
public interface DefFlowService extends IService<DefFlow> {

	/**
	 * 新增流程图信息
	 * @param defFlowVO 流程定义
	 * @return Boolean
	 */
    Boolean saveOrUpdate(DefFlowVO defFlowVO);

	/**
	 * 获取流程图信息
	 * @param id 流程ID
	 * @return DefFlowVO
	 */
	DefFlowVO getNodesById(Long id);

	/**
	 * 新增或修改流程定义
	 * @param defFlow 流程定义
	 * @return Boolean
	 */
    Boolean tempStore(DefFlow defFlow);

	/**
	 * 删除流程
	 * @param id 流程ID
	 */
	Boolean removeById(Long id);

}
