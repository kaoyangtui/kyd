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
import com.pig4cloud.pigx.jsonflow.api.entity.NodeJob;
import com.pig4cloud.pigx.jsonflow.api.vo.NodeJobVO;

/**
 * 节点任务设置
 *
 * @author luolin
 * @date 2021-03-09 17:30:17
 */
public interface NodeJobService extends IService<NodeJob> {

	/**
	 * 新增或修改任务
	 *
	 * @param nodeJobVO 节点任务设置
	 */
	Boolean saveOrUpdate(NodeJobVO nodeJobVO);

}
