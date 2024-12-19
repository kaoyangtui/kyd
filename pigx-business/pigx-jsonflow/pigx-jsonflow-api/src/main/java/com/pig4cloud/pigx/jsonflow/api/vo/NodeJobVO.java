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

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点任务设置
 *
 * @author luolin
 * @date 2021-03-09 17:30:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "节点任务设置")
public class NodeJobVO extends RunJob implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@Schema(description = "")
	private Long id;

	/**
	 * 流程定义ID
	 */
	@NotNull(message = "流程定义ID不能为空")
	@Schema(description = "流程定义ID")
	private Long defFlowId;

	/**
	 * 节点ID
	 */
	@NotNull(message = "节点ID不能为空")
	@Schema(description = "节点ID")
	private Long flowNodeId;

	/**
	 * 工单流程KEY
	 */
	@NotNull(message = "业务KEY不能为空")
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
	 * 任务名称
	 */
	@Schema(description = "任务名称")
	private String jobName;

	/**
	 * 任务时限
	 */
	@Schema(description = "任务时限")
	private Integer timeout;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sort;

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
	 * 修改人
	 */
	@Schema(description = "修改人")
	private Long updateUser;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 被驳回后是否可跳过 0 否 1 是
	 */
	@Schema(description = "被驳回后是否可跳过 0 否  1 是")
	private String isSkipRejected;

	/**
	 * 删除标识
	 */
	@Schema(description = "删除标识")
	private String delFlag;

	/**
	 * 待分配办理人节点ID
	 */
	@Schema(description = "待分配办理人节点ID")
	private Long distFlowNodeId;

	/**
	 * 被分配后是否立即运行 0否 1是
	 */
	@Schema(description = "被分配后是否立即运行 0否 1是")
	private String isNowRun;

	/**
	 * 任务来源类型 0默认任务 1不分离任务 2分离任务
	 */
	@Schema(description = "任务来源类型 0默认任务 1不分离任务 2分离任务")
	private String fromType;

	/**
	 * http请求类型：0GET、1POST、2PUT、3DELETE
	 */
	@Schema(description = "http请求类型：0GET、1POST、2PUT、3DELETE")
	private String httpMethod;

	/**
	 * 节点位置大小
	 */
	@Schema(description = "节点位置大小")
	private String positionSize;
	
	//----------------------配置参数----------------------

	/**
	 * 条件组集合
	 */
	@Schema(description = "条件组集合")
	private List<FlowRuleVO> condGroups = new ArrayList<>();

	/**
	 * Http请求参数
	 */
	@Schema(description = "Http请求参数")
	private List<FlowRule> httpParams = new ArrayList<>();

	/**
	 * 分配的参与者信息
	 */
	@Schema(description = "分配的参与者信息")
	private List<FlowRule> roleUserId = new ArrayList<>();

}
