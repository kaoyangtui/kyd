package com.pig4cloud.pigx.jsonflow.api.vo;

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

import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 分配参与者
 *
 * @author luolin
 * @date 2021-02-23 14:12:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分配参与者")
public class DistPersonVO extends DistPerson implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@Schema(description = "")
	private Long id;

	/**
	 * 工单CODE，便于业务人员查看
	 */
	@Schema(description = "工单CODE，便于业务人员查看")
	private String code;

	/**
	 * 参与者角色ID
	 */
	@Schema(description = "参与者角色ID")
	private Long roleId;
	/**
	 * 任务类型 -1无 0用户 1角色 2岗位 3部门
	 */
	@Schema(description = "任务类型 -1无 0用户 1角色 2岗位 3部门")
	private String jobType;

	/**
	 * 任务名称
	 */
	@Schema(description = "任务名称")
	private String jobName;

	/**
	 * 流程ID
	 */
	@Schema(description = "流程ID")
	private Long flowInstId;

	/**
	 * 工单流程KEY
	 */
	@Schema(description = "工单流程KEY")
	private String flowKey;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	private Long createUser;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Schema(description = "更新人")
	private Long updateUser;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 动态参与者KEY
	 */
	@Schema(description = "动态参与者KEY")
	private String userKey;

	// -------------------内部逻辑信息-------------------

	/**
	 * 是否已存在该分配参与者
	 */
	@Schema(description = "是否已存在该分配参与者")
	private String isExistPerson;

	/**
	 * 流程是否已结束
	 */
	@Schema(description = "流程是否已结束")
	private String isFlowFinish;

	/**
	 * 人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式
	 */
	@Schema(description = "人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式")
	private String valType;

	/**
	 * 被分配后是否立即运行 0否 1是
	 */
	@Schema(description = "被分配后是否立即运行 0否 1是")
	private String isNowRun;

	// -------------------动态计算信息-------------------

	/**
	 * 分配的参与者信息
	 */
	@Schema(description = "分配的参与者信息")
	private List<DistPerson> roleUserId = new ArrayList<>();

	/**
	 * 流程定义ID
	 */
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 开启指定人员任务
	 */
	@Schema(description = "开启指定人员任务")
	private List<Long> startRoleIds;

	//----------------------额外参数----------------------

	/**
	 * 节点ID
	 */
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 任务ID
	 */
	@Schema(description = "任务ID")
	private Long nodeJobId;

}
