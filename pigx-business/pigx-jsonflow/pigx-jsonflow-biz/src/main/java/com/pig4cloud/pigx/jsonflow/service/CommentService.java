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
import com.pig4cloud.pigx.jsonflow.api.entity.Comment;
import com.pig4cloud.pigx.jsonflow.api.entity.RunNode;
import com.pig4cloud.pigx.jsonflow.api.vo.CommentVO;
import com.pig4cloud.pigx.jsonflow.api.vo.RunJobVO;

import java.util.List;

/**
 * 节点批注管理
 *
 * @author luolin
 * @date 2021-02-25 16:41:52
 */
public interface CommentService extends IService<Comment> {


	/**
	 * 查询个人的批注信息
	 *
	 * @param page      分页对象
	 * @param comment   节点批注管理
	 * @param queryTime 查询时间
	 */
	IPage<Comment> getPage(Page<Comment> page, Comment comment, String[] queryTime);

	void saveOrUpdateComment(RunNode runNode);

	/**
	 * 保存修改节点审批批注
	 *
	 * @param runJobVO 运行任务
	 * @param isAutoAudit 是否自动审批
	 */
	void saveOrUpdateComment(RunJobVO runJobVO, boolean isAutoAudit);

	/**
	 * 查询批注信息
	 *
	 * @param page      分页对象
	 * @param commentVO 批准
	 */
	IPage<CommentVO> commentPage(Page<CommentVO> page, CommentVO commentVO);

	/**
	 * 查询批注信息
	 *
	 * @param commentVO 批准
	 */
	List<CommentVO> commentList(CommentVO commentVO);

}
