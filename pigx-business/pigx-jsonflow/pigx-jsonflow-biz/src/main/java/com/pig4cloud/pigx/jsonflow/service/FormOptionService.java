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
import com.pig4cloud.pigx.jsonflow.api.entity.FormOption;
import com.pig4cloud.pigx.jsonflow.api.vo.FormOptionVO;

import java.util.List;

/**
 * 表单/页面操作表
 *
 * @author luolin
 * @date 2024-02-10 22:50:44
 */
public interface FormOptionService extends IService<FormOption> {

	/**
	 * 查询表单/页面操作表
	 *
	 * @param page      分页对象
	 * @param formOption   表单/页面操作表
	 * @param queryTime 查询时间
	 */
	IPage<FormOption> getPage(Page<FormOption> page, FormOption formOption, String[] queryTime);

	/**
	 * 保存与更新已配置项
	 * @param formOptionVO 表单/页面操作
	 */
	boolean saveOpUpdate(FormOptionVO formOptionVO);

	/**
	 * 新增或修改打印模板
	 * @param formOptionVO 表单/页面操作
	 */
	boolean savePrintTemp(FormOptionVO formOptionVO);

	/**
	 * 查询已配置项
	 * @param formOptionVO 表单/页面操作
	 */
    List<FormOption> listFormOption(FormOptionVO formOptionVO);

	/**
	 * 删除已配置项
	 * @param formOption 表单/页面操作
	 */
	boolean removeFormOption(FormOption formOption);

	/**
	 * 查询开始节点已配置权限
	 * @param formOptionVO 表单/页面操作
	 */
	FormOptionVO listStartPerm(FormOptionVO formOptionVO);

	/**
	 * 查询开始节点已配置打印模板
	 * @param formOptionVO 表单/页面操作
	 */
	FormOptionVO listPrintTemp(FormOptionVO formOptionVO);

	void removeByDefFlowId(Long defFlowId, Long flowInstId, String type);

	List<FormOption> listFormOptions(FormOption perm);

}
