package com.pig4cloud.pigx.jsonflow.service.impl;

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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.entity.NodeJob;
import com.pig4cloud.pigx.jsonflow.api.vo.NodeJobVO;
import com.pig4cloud.pigx.jsonflow.mapper.NodeJobMapper;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
import com.pig4cloud.pigx.jsonflow.service.NodeJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Objects;

/**
 * 节点任务设置
 *
 * @author luolin
 * @date 2021-03-09 17:30:17
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NodeJobServiceImpl extends ServiceImpl<NodeJobMapper, NodeJob> implements NodeJobService {

	private final FlowNodeService flowNodeService;

	@Override
	public Boolean saveOrUpdate(NodeJobVO nodeJobVO) {
		// 设置任务定义ID
		FlowNode flowNode = flowNodeService.getById(nodeJobVO.getFlowNodeId());

		if (Objects.nonNull(nodeJobVO.getId()) && StrUtil.isNotBlank(nodeJobVO.getUserKey())) {
			// 同节点里相同参与者KEY与人员类型的任务只能有一个
			List<NodeJob> sameCalUserKey = this.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getFlowNodeId, flowNode.getId())
					.eq(NodeJob::getUserKey, nodeJobVO.getUserKey()).eq(NodeJob::getValType, nodeJobVO.getValType())
					.ne(NodeJob::getId, nodeJobVO.getId()));
			if (CollUtil.isNotEmpty(sameCalUserKey)) throw new ValidationException("同节点里相同参与者KEY与人员类型的任务只能有一个");
		}

		// 开始节点或结束节点有且只能有一个任务
		if (NodeTypeEnum.START.getType().equals(flowNode.getNodeType()) || NodeTypeEnum.END.getType().equals(flowNode.getNodeType())) {
			List<NodeJob> nodeJobs = this.list(Wrappers.<NodeJob>lambdaQuery().eq(NodeJob::getFlowNodeId, flowNode.getId())
					.ne(NodeJob::getId, nodeJobVO.getId()));
			if (CollUtil.isNotEmpty(nodeJobs)) throw new ValidationException("开始节点或结束节点有且只能有一个任务");
		}

		NodeJob nodeJob = new NodeJob();
		BeanUtil.copyProperties(nodeJobVO, nodeJob);
		if (Objects.nonNull(nodeJobVO.getId())) this.saveOrUpdate(nodeJob);
		else {
			// 防止再次保存为新增
			this.saveOrUpdate(nodeJob, Wrappers.<NodeJob>lambdaUpdate().eq(NodeJob::getFlowNodeId, nodeJobVO.getFlowNodeId()));
		}
		return Boolean.TRUE;
	}

}
