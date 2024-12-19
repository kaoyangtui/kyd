package com.pig4cloud.pigx.jsonflow.api.constant.enums;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowNode;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import jakarta.validation.ValidationException;
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

/**
 * 节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟
 * @author luolin
 * @date 2018/9/30
 *
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {
    /**
     * 开始节点
     */
    START("-1","start", "开始节点"),

    /**
     * 串行节点
     */
    SERIAL("0","serial", "串行节点"),

    /**
     * 并行节点
     */
    PARALLEL("1", "parallel","并行节点"),

    /**
     * 结束节点
     */
    END("2", "end","结束节点"),

    /**
     * 虚拟节点
     */
    VIRTUAL("3", "virtual","虚拟节点"),

	//--------------------------以下为流程图任务类型--------------------------
	/**
	 * 任务
	 */
	JOB("", "job","任务"),


	//--------------------------以下为流程图其他类型--------------------------
	/**
	 * 连线
	 */
	LINK("", "link","连线"),

	/**
	 * 横向泳道
	 */
	X_LANE("8", "x_lane","横向泳道"),

	/**
	 * 纵向泳道
	 */
	Y_LANE("9", "y_lane","纵向泳道");

	/**
	 * 类型
	 */
	private final String type;
	/**
	 * 流程节点类型
	 */
	private final String nodeType;

	/**
	 * 描述
	 */
	private final String description;

	public static boolean validateRunNodeAutoEnd(RunNode runNode) {
		return NodeTypeEnum.END.getType().equals(runNode.getNodeType()) && FlowCommonConstants.YES.equals(runNode.getIsAutoAudit());
	}

	public static boolean validateFlowNodeAutoEnd(FlowNode flowNode) {
		return NodeTypeEnum.END.getType().equals(flowNode.getNodeType()) && FlowCommonConstants.YES.equals(flowNode.getIsAutoAudit());
	}

	/**
	 * 返回枚举常量对象
	 * @param nodeType 流程图类型
	 */
	@SneakyThrows
	public static NodeTypeEnum getEnumByNodeType(String nodeType){
		if (StrUtil.isEmpty(nodeType)) throw new ValidationException("流程节点类型不存在");
		for (NodeTypeEnum each: values()) {
			if(each.getNodeType().equals(nodeType)) return  each;
			if(each.getType().equals(nodeType)) return  each;
		}
		throw new ValidationException("流程节点类型不存在");
	}

}
