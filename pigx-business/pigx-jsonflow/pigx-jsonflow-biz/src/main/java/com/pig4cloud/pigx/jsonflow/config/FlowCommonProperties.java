package com.pig4cloud.pigx.jsonflow.config;
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
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 核心配置属性
 * @Author luolin
 * @Date 2020/1/15
 */
@Data
@Component
@ConfigurationProperties("flow.core")
public class FlowCommonProperties {

	/**
	 * 是否允许任务无角色无人员
	 */
	private String isAllowNoRoleUser;

	/**
	 * 是否允许任务无角色无人员时发起后初始化为自己
	 */
	private String isAllowInitSelf;

	/**
	 * 是否可以驳回到任意节点
	 */
	private String isRejectAnyNode;

	/**
	 * 工单模块http请求地址
	 */
	private String orderHttpAddress;

	/**
	 * 默认查询工单信息接口
	 */
	private String queryOrder;

	/**
	 * 默认更新工单信息接口
	 */
	private String updateOrder;

	/**
	 * 启动子流程时保存子工单接口
	 */
	private String startSubFlow;

	/**
	 * 重启子流程时更新子工单接口
	 */
	private String restartSubFlow;

	/**
	 * 返回父流程时更新父工单接口
	 */
	private String backParFlow;

	/**
	 * 查询工作交接信息接口
	 */
	private String listRunByRunJobIds;

}
