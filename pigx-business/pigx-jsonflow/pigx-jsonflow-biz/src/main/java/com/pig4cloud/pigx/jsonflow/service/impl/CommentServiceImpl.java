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
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysPost;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobBtnTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.JobUserTypeEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.KeyValFromEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.NodeJobStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.SubFlowStatusEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.Comment;
import com.pig4cloud.pigx.jsonflow.api.entity.RunFlow;
import com.pig4cloud.pigx.jsonflow.api.entity.RunJob;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.util.QueryWrapperUtils;
import com.pig4cloud.pigx.jsonflow.api.vo.CommentVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;
import com.pig4cloud.pigx.jsonflow.engine.IAuditorService;
import com.pig4cloud.pigx.jsonflow.mapper.CommentMapper;
import com.pig4cloud.pigx.jsonflow.service.CommentService;
import com.pig4cloud.pigx.jsonflow.service.RunFlowService;
import com.pig4cloud.pigx.jsonflow.service.RunJobService;
import com.pig4cloud.pigx.jsonflow.service.RunNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 节点批注管理
 *
 * @author luolin
 * @date 2021-02-25 16:41:52
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

	@Lazy
	@Autowired
	private RunJobService runJobService;
	@Autowired
	private IAuditorService auditorService;
	@Lazy
	@Autowired
	private RunFlowService runFlowService;
	@Lazy
	@Autowired
	private RunNodeService runNodeService;

	@Override
	public IPage<Comment> getPage(Page<Comment> page, Comment comment, String[] queryTime) {
		QueryWrapper<Comment> query = Wrappers.query(comment);
		QueryWrapperUtils.userQueryTime(query, Comment::getCreateTime, queryTime
				, Comment::getUserId, SecurityUtils.getUser().getId());
		this.page(page, query);
		return page;
	}

	@Override
	public void saveOrUpdateComment(RunNode runNode) {
		RunJobVO runJobVOComment = new RunJobVO();
		BeanUtil.copyProperties(runNode, runJobVOComment);
		runJobVOComment.setRunNodeId(runNode.getId());
		this.saveOrUpdateComment(runJobVOComment, true);
	}

	@Override
	public void saveOrUpdateComment(RunJobVO runJobVO, boolean isAutoAudit) {
		Comment comment = new Comment();
		// 判断MQ消息
		Long userId = SecurityUtils.getUser().getId();
		if (!isAutoAudit) {
			String commentMsg = runJobVO.getComment();
			// 构建批注信息
			String nodeMsg = "【" + JobBtnTypeEnum.getEnumByType(runJobVO.getJobBtn()).getDesc() + "】 意见: ";
			if (StrUtil.isEmpty(commentMsg)) commentMsg = nodeMsg + "无";
			else commentMsg = nodeMsg + commentMsg;
			BeanUtil.copyProperties(runJobVO, comment, FlowEntityInfoConstants.ID);
			comment.setRunJobId(runJobVO.getId());
			comment.setUserId(userId);
			comment.setRemark(commentMsg);
		} else {
			BeanUtil.copyProperties(runJobVO, comment, FlowEntityInfoConstants.ID, FlowEntityInfoConstants.NODE_JOB_ID,
					FlowEntityInfoConstants.USER_ID);
			comment.setRunNodeId(runJobVO.getRunNodeId());
			comment.setFlowNodeId(runJobVO.getFlowNodeId());
			comment.setRemark(FlowCommonConstants.AUTO_AUDIT);
		}
		comment.setCreateUser(userId);
		// TODO 若采用时间线，最好不更新
		comment.setCreateTime(LocalDateTime.now());
		this.save(comment);
	}

	@Override
	public IPage<CommentVO> commentPage(Page<CommentVO> page, CommentVO commentVO) {
		Long flowInstId = commentVO.getFlowInstId();
		List<Comment> comments = this.list(Wrappers.<Comment>lambdaQuery().eq(Comment::getFlowInstId, flowInstId).orderByDesc(Comment::getCreateTime));
		if (CollUtil.isEmpty(comments)) return page;
		List<CommentVO> commentVOS = this.buildCommentVOWithUserRole(flowInstId, comments);
		page.setRecords(QueryWrapperUtils.paging(page, commentVOS));
		return page;
	}

	@Override
	public List<CommentVO> commentList(CommentVO commentVO) {
		Long flowInstId = commentVO.getFlowInstId();
		List<Comment> comments = this.list(Wrappers.<Comment>lambdaQuery().eq(Comment::getFlowInstId, flowInstId).orderByDesc(Comment::getCreateTime));
		if (CollUtil.isEmpty(comments)) return CollUtil.newArrayList();
		List<CommentVO> commentVOS = this.buildCommentVOWithUserRole(flowInstId, comments);
		List<CommentVO> res = new ArrayList<>();
		commentVOS.forEach(each -> {
			if (Objects.nonNull(each.getRunJobId())) {
				boolean b = res.stream().filter(f -> Objects.nonNull(f.getRunJobId())).anyMatch(any -> each.getRunJobId().equals(any.getRunJobId()));
				if (!b) res.add(each);
			} else if (Objects.nonNull(each.getRunNodeId())) {
				boolean b = res.stream().filter(f -> Objects.nonNull(f.getRunNodeId())).anyMatch(any -> each.getRunNodeId().equals(any.getRunNodeId()));
				if (!b) res.add(each);
			}
		});
		return res;
	}

	/**
	 * 构建用户名与角色名
	 *
	 * @param flowInstId 流程ID
	 * @param comments   批注
	 */
	private List<CommentVO> buildCommentVOWithUserRole(Long flowInstId, List<Comment> comments) {
		RunFlow runFlow = runFlowService.getById(flowInstId);
		List<RunJob> runJobs = runJobService.list(Wrappers.<RunJob>lambdaQuery().eq(RunJob::getFlowInstId, flowInstId));
		List<RunNode> runNodes = runNodeService.list(Wrappers.<RunNode>lambdaQuery().eq(RunNode::getFlowInstId, flowInstId));
		List<CommentVO> commentVOS = this.buildCommentVO(comments, runJobs, runNodes, runFlow);
		// 设置用户和角色名称
		List<SysUser> finalSysUsers = this.listSysUsersByUserIds(commentVOS);
		// 设置用户和角色名称
		List<SysUser> sysUsers = this.listSysUsersByRoleIds(commentVOS);
		List<SysRole> sysRoles = this.listSysRolesByRoleIds(commentVOS);
		List<SysPost> sysPosts = this.listSysPostsByPostIds(commentVOS);
		List<SysDept> sysDepts = this.listSysDeptsByDeptIds(commentVOS);
		List<Long> runNodeIds = new ArrayList<>();
		commentVOS.forEach(each -> {
			if (Objects.nonNull(each.getUserId())) {
				Optional<SysUser> any = finalSysUsers.stream().filter(f -> f.getUserId().equals(each.getUserId())).findAny();
				if (any.isPresent()) each.setUserName(any.get().getName());
				else each.setUserName("不存在用户");
			}
			if (Objects.nonNull(each.getRoleId())) {
				String desc = StrUtil.EMPTY;
				if (JobUserTypeEnum.USER.getType().equals(each.getJobType())) {
					Optional<SysUser> any = sysUsers.stream().filter(f -> f.getUserId().equals(each.getRoleId())).findAny();
					if (any.isPresent()) each.setRoleName(any.get().getName());
					else each.setRoleName("不存在的用户");
					desc = JobUserTypeEnum.USER.getDescription();
				} else if (JobUserTypeEnum.ROLE.getType().equals(each.getJobType())) {
					Optional<SysRole> any = sysRoles.stream().filter(f -> f.getRoleId().equals(each.getRoleId())).findAny();
					if (any.isPresent()) each.setRoleName(any.get().getRoleName());
					else each.setRoleName("不存在的角色");
					desc = JobUserTypeEnum.ROLE.getDescription();
				} else if (JobUserTypeEnum.POST.getType().equals(each.getJobType())) {
					Optional<SysPost> any = sysPosts.stream().filter(f -> f.getPostId().equals(each.getRoleId())).findAny();
					if (any.isPresent()) each.setRoleName(any.get().getPostName());
					else each.setRoleName("不存在的岗位");
					desc = JobUserTypeEnum.POST.getDescription();
				} else if (JobUserTypeEnum.DEPT.getType().equals(each.getJobType())) {
					Optional<SysDept> any = sysDepts.stream().filter(f -> f.getDeptId().equals(each.getRoleId())).findAny();
					if (any.isPresent()) each.setRoleName(any.get().getName());
					else each.setRoleName("不存在的部门");
					desc = JobUserTypeEnum.DEPT.getDescription();
				}
				String type = KeyValFromEnum.LEFT_METHOD.getFrom() + desc + KeyValFromEnum.RIGHT_METHOD.getFrom();
				each.setRoleName(each.getRoleName() + type);
			}
			if (runNodeIds.contains(each.getRunNodeId())) {
				this.handleSubFlowStatus(runNodes, each);
				runNodeIds.add(each.getRunNodeId());
			}
		});
		return commentVOS;
	}

	private List<SysUser> listSysUsersByUserIds(List<CommentVO> commentVOS) {
		List<Long> finalUserIds = commentVOS.stream().map(CommentVO::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(finalUserIds) ? auditorService.listUsersByUserIds(finalUserIds) : new ArrayList<>();
	}

	private List<SysUser> listSysUsersByRoleIds(List<CommentVO> commentVOS) {
		List<Long> userIds = commentVOS.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.USER.getType().equals(f.getJobType()))
				.map(CommentVO::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(userIds) ? auditorService.listUsersByUserIds(userIds) : new ArrayList<>();
	}

	private List<SysRole> listSysRolesByRoleIds(List<CommentVO> commentVOS) {
		List<Long> roleIds = commentVOS.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.ROLE.getType().equals(f.getJobType()))
				.map(CommentVO::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(roleIds) ? auditorService.listRolesByRoleIds(roleIds) : new ArrayList<>();
	}

	private List<SysPost> listSysPostsByPostIds(List<CommentVO> commentVOS) {
		List<Long> postIds = commentVOS.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.POST.getType().equals(f.getJobType()))
				.map(CommentVO::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(postIds) ? auditorService.listPostsByPostIds(postIds) : new ArrayList<>();
	}

	private List<SysDept> listSysDeptsByDeptIds(List<CommentVO> commentVOS) {
		List<Long> deptIds = commentVOS.stream().filter(f -> Objects.nonNull(f.getRoleId()) && JobUserTypeEnum.DEPT.getType().equals(f.getJobType()))
				.map(CommentVO::getRoleId).distinct().collect(Collectors.toList());
		return CollUtil.isNotEmpty(deptIds) ? auditorService.listDeptsByDeptIds(deptIds) : new ArrayList<>();
	}

	private void handleSubFlowStatus(List<RunNode> runNodes, CommentVO each) {
		Optional<RunNode> exist = runNodes.stream().filter(f -> f.getId().equals(each.getRunNodeId())).findAny();
		if (exist.isPresent()) {
			RunNode runNode = exist.get();
			each.setSubFlowInstId(runNode.getSubFlowInstId());
			if (Objects.nonNull(runNode.getSubFlowInstId())) each.setSubFlowStatus(runNode.getSubFlowStatus());

			boolean existSubFlow = Objects.nonNull(runNode.getSubFlowInstId()) && NodeJobStatusEnum.RUN.getStatus().equals(runNode.getStatus());
			if (!existSubFlow) return;
			if (SubFlowStatusEnum.SUB_FLOW_RUN.getStatus().equals(runNode.getSubFlowStatus())) {
				each.setEndTime(null);
				String description = SubFlowStatusEnum.SUB_FLOW_RUN.getDescription();
				each.setUseTime(description);
				each.setRemark(description);
			}
		}
	}

	/**
	 * 构建批注额外信息
	 *
	 * @param comments 审批批注集合
	 * @param runJobs  运行任务集合
	 */
	private List<CommentVO> buildCommentVO(List<Comment> comments, List<RunJob> runJobs, List<RunNode> runNodes, RunFlow runFlow) {
		List<CommentVO> commentVOS = comments.stream().map(m -> {
			CommentVO res = new CommentVO();
			BeanUtil.copyProperties(m, res);
			if (Objects.nonNull(m.getRunJobId())) {
				Optional<RunJob> exist = runJobs.stream().filter(f -> f.getId().equals(m.getRunJobId())).findAny();
				if (exist.isPresent()) {
					RunJob runJob = exist.get();
					res.setJobName(runJob.getJobName());
					res.setJobType(runJob.getJobType());
					res.setSignatureType(runJob.getSignatureType());
					res.setBelongType(runJob.getBelongType());
					res.setRoleId(runJob.getRoleId());
					res.setStartTime(runJob.getStartTime());
					// 结束以审批时间为准
					res.setEndTime(res.getCreateTime());
					res.setStatus(NodeJobStatusEnum.COMPLETE.getStatus());
					this.extractedUseTime(res, res.getStartTime(), res.getEndTime());
				} else {
					res.setJobName(FlowCommonConstants.INVALID_JOB);
				}
			} else {
				Optional<RunNode> exist = runNodes.stream().filter(f -> f.getId().equals(m.getRunNodeId())).findAny();
				if (exist.isPresent()) {
					RunNode runNode = exist.get();
					res.setJobName(runNode.getNodeName());
					res.setStartTime(runNode.getStartTime());
					// 结束以审批时间为准
					res.setEndTime(res.getCreateTime());
					res.setStatus(NodeJobStatusEnum.COMPLETE.getStatus());
					this.extractedUseTime(res, res.getStartTime(), res.getEndTime());
				} else {
					res.setJobName(FlowCommonConstants.INVALID_JOB);
				}
			}
			return res;
		}).collect(Collectors.toList());
		// 正在运行中的节点排在最后（含覆盖被驳回的节点）
		List<RunJob> runningJobs = runJobs.stream().filter(f -> NodeJobStatusEnum.getRunRejectRejectedStatuses().contains(f.getStatus())).collect(Collectors.toList());
		commentVOS.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
		if (CollUtil.isEmpty(runningJobs) || FlowStatusEnum.FINISH.getStatus().equals(runFlow.getStatus())) return commentVOS;
		List<CommentVO> runningVOS = CollUtil.newArrayList();
		runningJobs.forEach(running -> {
			CommentVO res = new CommentVO();
			BeanUtil.copyProperties(running, res);
			if (FlowStatusEnum.INVALID.getStatus().equals(runFlow.getStatus())) {
				res.setUseTime(NodeJobStatusEnum.INVALID.getDescription());
				res.setStatus(NodeJobStatusEnum.INVALID.getStatus());
			} else if (FlowStatusEnum.TERMINATE.getStatus().equals(runFlow.getStatus())) {
				res.setUseTime(NodeJobStatusEnum.TERMINATE.getDescription());
				res.setStatus(NodeJobStatusEnum.TERMINATE.getStatus());
			} else res.setUseTime(NodeJobStatusEnum.RUN.getDescription());
			res.setRunJobId(running.getId());
			runningVOS.add(res);
		});
		// 判断分页的页数，只能在排在前面
		runningVOS.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
		runningVOS.addAll(commentVOS);
		return runningVOS;
	}

	private void extractedUseTime(CommentVO res, LocalDateTime startTime,  LocalDateTime endTime) {
		if (Objects.nonNull(endTime)) {
			Duration duration = Duration.between(startTime, endTime);
			String between = DateUtil.formatBetween(duration.getSeconds() * 1000, BetweenFormatter.Level.SECOND);
			res.setUseTime(between);
		}
	}

}
