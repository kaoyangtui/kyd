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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.jsonflow.api.entity.DistPerson;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.DistPersonVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobHandoverVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunNodeVO;
import com.pig4cloud.pigx.jsonflow.api.vo.ToDoneJobVO;
import com.pig4cloud.pigx.jsonflow.api.vo.TodoJobVO;

import java.util.List;

/**
 * 任务记录
 *
 * @author luolin
 * @date 2021-03-03 09:26:28
 */
public interface RunJobService extends IService<RunJob> {

	/**
	 * 任务审批
	 *
	 * @param runJobVO 运行任务
	 */
	boolean complete(RunJobVO runJobVO);

	/**
	 * 新增或修改任务
	 *
	 * @param runJobVO 节点任务设置
	 */
	Boolean saveOrUpdate(RunJobVO runJobVO);

	/**
	 * 结束时检查任务是否全部审批完成
	 *
	 * @param runNode 运行节点
	 */
	boolean endCheckAllSkipComplete(RunNode runNode);

	/**
	 * 处理跳转逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean anyJump(RunJobVO runJobVO);

	/**
	 * 处理退回上一步
	 *
	 * @param runJobVO 运行任务
	 */
	boolean backPreJob(RunJobVO runJobVO);

	/**
	 * 处理退回首环节
	 *
	 * @param runJobVO 运行任务
	 */
	boolean backFirstJob(RunJobVO runJobVO);

	/**
	 * 处理驳回逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean reject(RunJobVO runJobVO);

	/**
	 * 处理加签
	 *
	 * @param runJobVO 运行任务
	 */
	boolean signature(RunJobVO runJobVO);

	/**
	 * 处理挂起逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean suspension(RunJobVO runJobVO);

	/**
	 * 签收反签收任务
	 *
	 * @param runJobVO 运行任务
	 */
	boolean signForJob(RunJobVO runJobVO);

	/**
	 * 处理转办逻辑
	 *
	 * @param runJobVO 运行任务
	 */
	boolean turnRunJob(RunJobVO runJobVO);

	/**
	 * 指派人员
	 *
	 * @param runJobVO 运行任务
	 */
	boolean appointUser(RunJobVO runJobVO);

	/**
	 * 分页查询待办任务
	 *
	 * @param page      分页对象
	 * @param todoJobVO 任务记录
	 */
	IPage getTodoPage(Page page, TodoJobVO todoJobVO);

	/**
	 * 分页查询待办任务数
	 *
	 * @param page      分页对象
	 * @param todoJobVO 任务记录
	 */
	IPage getTodoSize(Page page, TodoJobVO todoJobVO);

	/**
	 * 通过id获取审批前数据
	 *
	 * @param todoJobVO 任务信息
	 */
	TodoJobVO getTodoDetailById(TodoJobVO todoJobVO);

	/**
	 * 分页查询已办任务
	 *
	 * @param page      分页对象
	 * @param toDoneJobVO 任务信息
	 */
	IPage getToDonePage(Page page, ToDoneJobVO toDoneJobVO);

	/**
	 * 通过id获取已办数据
	 *
	 * @param toDoneJobVO 任务信息
	 */
	ToDoneJobVO getToDoneDetailById(ToDoneJobVO toDoneJobVO);

	/**
	 * 流程任务交接：查询交接人是自己未完成流程的任务
	 *
	 * @param page   分页对象
	 * @param status 状态
	 */
	IPage<RunJobHandoverVO> jobHandoverPage(Page<RunJobHandoverVO> page, String status);

	/**
	 * 开启当前userKey节点任务
	 *
	 * @param distPersonVO 分配参与者
	 */
	boolean startRunJobByUserKey(DistPersonVO distPersonVO);

	/**
	 * 根据任务ID设置办理人
	 *
	 * @param runJobs        运行任务集合
	 * @param handoverUser   交接人
	 * @param handoverReason 交接原因
	 * @param type           类型
	 */
	Boolean handleJobHandover(List<RunJob> runJobs, Long handoverUser, String handoverReason, String type);

	/**
	 * @param flowInstId 流程实例ID
	 */
	List<RunJobVO> listByFlowInstId(Long flowInstId);

	List<SysUser> listSysUsersByUserIds(List<RunJob> runJobs);

	List<SysUser> listSysUsersByRoleIds(List<RunJob> runJobs);

	List<SysRole> listSysRolesByRoleIds(List<RunJob> runJobs);

	List<SysPost> listSysPostsByPostIds(List<RunJob> runJobs);

	List<SysDept> listSysDeptsByDeptIds(List<RunJob> runJobs);

	/**
	 * 通过流程id查询运行任务审批人
	 *
	 * @param runNodeId 运行节点ID
	 */
	List<SysUser> listUsersByRunNodeId(Long runNodeId);

	/**
	 * 通过流程id查询运行任务审批人
	 *
	 * @param runNodeId 运行节点ID
	 */
	List<SysUser> listUserByRunNodeId(Long runNodeId);

	/**
	 * 通过流程id查询运行任务审批角色
	 *
	 * @param runNodeId 运行节点ID
	 */
	List<SysRole> listRoleByRunNodeId(Long runNodeId);

	/**
	 * 通过流程id查询运行任务审批岗位
	 *
	 * @param runNodeId 运行节点ID
	 */
	List<SysPost> listPostByRunNodeId(Long runNodeId);

	/**
	 * 通过流程id查询运行任务审批部门
	 *
	 * @param runNodeId 运行节点ID
	 */
	List<SysDept> listDeptByRunNodeId(Long runNodeId);

	/**
	 * 通过流程id查询运行任务
	 *
	 * @param runNodeId 运行节点ID
	 */
	List<RunJob> listRunJobByRunNodeId(Long runNodeId);

	/**
	 * 完成节点任务
	 * @param runNodeId 运行节点ID
	 * @param suspension 是否挂起
	 */
	void completeRunJobStatus(Long runNodeId, List<Long> runJobIds, String suspension);

	List<Long> listRunJobsByStatus(List<RunJob> currRunJobs);

	/**
	 * 更新符合条件任务状态并设置办理人
	 *
	 * @param runJobs 运行任务集合
	 * @param status  状态
	 */
	void handleRunJobsUser(List<RunJob> runJobs, String status);

	/**
	 * 更新符合条件任务状态
	 *
	 * @param runJobs 运行任务集合
	 * @param status  状态
	 */
	void handleRunJobStatus(List<RunJob> runJobs, String status);

	void handleRunJobStatusByRunNodeIds(List<Long> runNodeIds, String status);

	/**
	 * 更新运行任务
	 * @param runJobs 运行任务集合
	 */
	void handleRunNodeJobStatus(List<RunJob> runJobs, String status, boolean isUpdateNextStatus, boolean isUpdateCurrStatus);

	/**
	 * 构建加签任务信息
	 * @param runJobVO 运行任务
	 */
	RunJob buildRunJob(RunJobVO runJobVO, RunNodeVO runNodeVO, RunNode runNode);

	/**
	 * 重置加签任务信息
	 * @param newRunJob 加签任务
	 */
	RunJob resetRunJob(RunJob newRunJob, String signedType);

	/**
	 * 重置任务信息
	 * @param runJob 任务信息
	 */
	void resetByDistPerson(DistPerson distPerson, RunJob runJob);

	/**
	 * 取回任务
	 * @param runJob 运行任务
	 */
	Boolean retakeJob(RunJob runJob);

	/**
	 * 减签当前任务
	 * @param runJob 运行任务
	 */
	Boolean signOff(RunJob runJob);

	/**
	 * 催办当前任务集合
	 * @param flowInstId 流程实例ID
	 * @param runNodeId 运行节点ID
	 */
	List<RunJob> doRemind(Long flowInstId, Long runNodeId);

	/**
	 * 催办当前任务
	 * @param runJobVO 运行任务
	 */
	Boolean remind(RunJobVO runJobVO);

	/**
	 * 是否已阅
	 * @param runJob 运行任务
	 */
	Boolean isRead(RunJob runJob);

}
