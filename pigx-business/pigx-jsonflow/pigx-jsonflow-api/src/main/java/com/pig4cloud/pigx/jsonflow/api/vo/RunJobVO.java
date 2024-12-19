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
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务记录
 *
 * @author luolin
 * @date 2021-03-03 09:26:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务记录")
public class RunJobVO extends FlowNodeVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@Schema(description = "")
	private Long id;

	/**
	 * 流程ID
	 */
	@NotNull(message = "流程ID不能为空")
	@Schema(description = "流程ID")
	private Long flowInstId;
	
	/**
	 * 节点ID
	 */
	@NotNull(message = "节点ID不能为空")
	@Schema(description = "节点ID")
	private Long runNodeId;

	/**
	 * 工单流程KEY
	 */
	@NotBlank(message = "业务KEY不能为空")
	@Schema(description = "工单流程KEY")
	private String flowKey;

	/**
	 * 任务类型 -1无 0用户 1角色 2岗位 3部门
	 */
	@NotBlank(message = "任务类型不能为空")
	@Schema(description = "任务类型 -1无 0用户 1角色 2岗位 3部门")
	private String jobType;
	/**
	 * 任务名称
	 */
	@Schema(description = "任务名称")
	private String jobName;
	/**
	 * 办理人(用户ID）
	 */
	@Schema(description = "办理人(用户ID）")
	private Long userId;

	/**
	 * 参与者角色ID
	 */
	@Schema(description = "参与者角色ID")
	private Long roleId;

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
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

	/**
	 * 任务状态 -1：未开始 0：办理中 1：结束  2：驳回中 3：跳过 9：被驳回
	 */
	@NotBlank(message = "任务状态不能为空")
	@Schema(description = "任务状态 -1：未开始 0：办理中 1：结束  2：驳回中 3：跳过 9：被驳回")
	private String status;

	/**
	 * 任务时限
	 */
	@Schema(description = "任务时限")
	private Integer timeout;

	/**
	 * 是否挂起（0否 1是）
	 */
	@Schema(description = "是否挂起（0否 1是）")
	private String suspension;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private LocalDateTime endTime;

	/**
	 * 任务ID
	 */
	@NotNull(message = "任务ID不能为空")
	@Schema(description = "任务ID")
	private Long nodeJobId;

	/**
	 * 动态参与者KEY
	 */
	@Schema(description = "动态参与者KEY")
	private String userKey;

	/**
	 * 动态参与者取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )
	 */
	@Schema(description = "动态参与者取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )")
	private String userKeyVal;

	/**
	 * 人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式
	 */
	@Schema(description = "人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式")
	private String valType;
	
	/**
	 * 被驳回后是否可跳过 0 否 1 是
	 */
	@Schema(description = "被驳回后是否可跳过 0 否  1 是")
	private String isSkipRejected;

	/**
	 * 加签类型：1前加签、2后加签、3加并签
	 */
	@Schema(description = "加签类型：1前加签、2后加签、3加并签")
	private String signatureType;

	/**
	 * 加签关联前置任务ID
	 */
	@Schema(description = "加签关联前置任务ID")
	private Long signatureId;

	/**
	 * 挂起原因
	 */
	@Schema(description = "挂起原因")
	private String suspensionReason;

	/**
	 * 流程定义ID
	 */
	@NotNull(message = "流程定义ID不能为空")
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 节点ID
	 */
	@NotNull(message = "流程节点ID不能为空")
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 被分配后是否立即运行 0否 1是
	 */
	@Schema(description = "被分配后是否立即运行 0否 1是")
	private String isNowRun;

	/**
	 * 是否已阅 0：否 1：是
	 */
	@Schema(description = "是否已阅 0：否 1：是")
	private String isRead;

	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;

	/**
	 * 任务来源类型 0默认任务 1不分离任务 2分离任务
	 */
	@Schema(description = "任务来源类型 0默认任务 1不分离任务 2分离任务")
	private String fromType;

	/**
	 * 节点位置大小
	 */
	@Schema(description = "节点位置大小")
	private String positionSize;

	/**
	 * 归属类型 0普通任务 1抄送任务 2传阅任务
	 */
	@Schema(description = "归属类型 0普通任务 1抄送任务 2传阅任务")
	private String belongType;

	/**
	 * 是否为配置任务：0 否 1 是
	 */
	@Schema(description = "是否为配置任务：0 否 1 是")
	private String isConfigJob;

	// -------------------内部逻辑信息-------------------

	/**
	 * 流转类型
	 */
	@Schema(description = "流转类型")
	private String circularType;

	/**
	 * 节点完成类型： 0 未完成 1 节点完成 2 子流程完成
	 */
	@Schema(description = "节点完成类型： 0 未完成 1 节点完成 2 子流程完成")
	private String nodeCompleteType;

	/**
	 * 动态计算类型：0 更新 1 新增
	 */
	@Schema(description = "动态计算类型：0 更新 1 新增")
	private String calculateType;

	/**
	 * 是否强制审批：0 否 1 是
	 */
	@Schema(description = "是否强制审批：0 否 1 是")
	private String isForceAudit;

	//-------------------额外信息-------------------

	/**
	 * 节点子流程实例ID
	 */
	@Schema(description = "节点子流程实例ID")
	private Long subFlowInstId;

	/**
	 * 工单ID
	 */
	@Schema(description = "工单ID")
	private Long orderId;
	
	/**
	 * 运行节点信息
	 */
	@Schema(description = "运行节点信息")
	private RunNodeVO runNodeVO;

	/**
	 * 前端指定下一步参与者
	 */
	@Schema(description = "前端指定下一步参与者")
	private DistPerson nextUserRole;

	/**
	 * 审批动作：按钮名称
	 */
	@Schema(description = "审批动作：按钮名称")
	private String jobBtn;

	/**
	 * 审批意见
	 */
	@Schema(description = "审批意见")
	private String comment;

	/**
	 * 作废原因
	 */
	@Schema(description = "作废原因")
	private String invalidReason;

	// -------------------驳回逻辑信息-------------------

	/**
	 * 驳回信息
	 */
	@Schema(description = "驳回信息")
	private RunRejectVO runRejectVO;

	//-------------------消息通知信息-------------------
	
	/**
	 * 流程发起人名称
	 */
	@Schema(description = "流程发起人名称")
	private String initiatorName;

	/**
	 * 催办提醒类型 0 任务 1 子流程
	 */
	@Schema(description = "催办提醒类型 0 任务 1 子流程")
	private String remindType;

	// ---------------流程图信息---------------
	/**
	 * 办理人名称
	 */
	@Schema(description = "办理人名称")
	private String userName;

	/**
	 * 参与者角色名称
	 */
	@Schema(description = "参与者角色名称")
	private String roleName;

	//---------------加签信息---------------
	/**
	 * 被加节点类型：1前加节点、2后节点、3加并节点
	 */
	@Schema(description = "被加节点类型：1前加节点、2后节点、3加并节点")
	private String nodeSignedType;

	/**
	 * 节点名称
	 */
	@Schema(description = "节点名称")
	private String nodeName;
	
	/**
	 * 被加签类型：1前加签、2后加签、3加并签
	 */
	@Schema(description = "被加签类型：1前加签、2后加签、3加并签")
	private String signedType;

	//---------------签收信息---------------
	/**
	 * 签收类型：0反签收 1签收
	 */
	@Schema(description = "签收类型：0反签收 1签收")
	private String signForType;

	//----------------------流程实例图参数----------------------
	/**
	 * 当前任务实例列表
	 */
	@Schema(description = "当前任务实例列表")
	private List<RunJob> currRunJobs = new ArrayList<>();

	/**
	 * 运行任务ID
	 */
	@Schema(description = "运行任务ID")
	private Long runJobId;
	
}
