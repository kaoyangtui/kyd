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
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeCircTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.TabsOptionEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowNodeVO;
import com.pig4cloud.pigx.jsonflow.mapper.FlowNodeMapper;
import com.pig4cloud.pigx.jsonflow.service.FlowNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import java.util.List;

/**
 * 流程节点设置
 *
 * @author luolin
 * @date 2021-02-23 14:12:03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowNodeServiceImpl extends ServiceImpl<FlowNodeMapper, FlowNode> implements FlowNodeService {

	/**
	 * 保存前校验节点
	 * 串行节点、并行节点、结束节点有上一节点
	 * 虚拟节点、开始节点不能有上一节点
	 *
	 * @param flowNodeVO 流程节点设置
	 */
	@Override
	public Boolean saveOrUpdate(FlowNodeVO flowNodeVO, String fromType) {
		// 保存前校验节点
		String type = flowNodeVO.getNodeType();
		Integer sort = flowNodeVO.getSort();
		List<FlowNode> flowNodes = this.list(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getDefFlowId, flowNodeVO.getDefFlowId()));
		if (NodeTypeEnum.SERIAL.getType().equals(type) && CollUtil.isNotEmpty(flowNodes)) {
			// TODO
		} else if (NodeTypeEnum.PARALLEL.getType().equals(type) && CollUtil.isNotEmpty(flowNodes)) {
			// TODO
		}
		// 判断虚拟节点必须是直接返回 且不能有条件 排序必须为负数
		else if (NodeTypeEnum.VIRTUAL.getType().equals(type)) {
			boolean rejectType = NodeCircTypeEnum.DIRECT_RETURN.getType().equals(flowNodeVO.getRejectType());
			if (!rejectType) throw new ValidationException("虚拟节点【被驳回后再次提交时】必须是直接返回");
			if (sort >= 0) flowNodeVO.setSort(CommonNbrPool.MINUS_INT_1);
		}
		// 判断开始节点与结束节点一定是只能有一个 TODO 结束节点支持多个
		else if (NodeTypeEnum.START.getType().equals(type)/* || NodeTypeEnum.END.getType().equals(type)*/) {
			if (CollUtil.isNotEmpty(flowNodes)) {
				boolean present = flowNodes.stream().anyMatch(f -> f.getNodeType().equals(type) && !f.getId().equals(flowNodeVO.getId()));
				if (present) throw new ValidationException("开始节点与结束节点有且只能有一个");
			}
		}
		// 默认增加审批过程、查看流程图
		List<String> tabs = CollUtil.newArrayList(TabsOptionEnum.COMMENT.getId(), TabsOptionEnum.FLOW_PHOTO.getId());
		// 判断一键快捷设计
		if (CommonNbrPool.STR_0.equals(fromType)) tabs.add(TabsOptionEnum.VIEW_RUN_APPLICATION.getId());
		if (ArrayUtil.isEmpty(flowNodeVO.getPcTodoUrl())) {
			flowNodeVO.setPcTodoUrl(ArrayUtil.toArray(tabs, String.class));
		}
		if (ArrayUtil.isEmpty(flowNodeVO.getPcFinishUrl())) {
			flowNodeVO.setPcFinishUrl(ArrayUtil.toArray(tabs, String.class));
		}
		// 保存或修改
		FlowNode flowNode = new FlowNode();
		BeanUtil.copyProperties(flowNodeVO, flowNode);
		return super.saveOrUpdate(flowNode);
	}

}
