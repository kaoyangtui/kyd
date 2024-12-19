package com.pig4cloud.pigx.order.service.impl;
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

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.order.api.entity.CreateTable;
import com.pig4cloud.pigx.order.api.vo.CreateTableVO;
import com.pig4cloud.pigx.order.mapper.CreateTableMapper;
import com.pig4cloud.pigx.order.service.CreateTableService;
import com.pig4cloud.pigx.order.table.CreateTableHandler;
import com.pig4cloud.pigx.order.table.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anyline.metadata.Column;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

/**
 * 自动创建表管理
 *
 * @author luolin
 * @date 2022-09-23 21:56:11
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CreateTableServiceImpl extends ServiceImpl<CreateTableMapper, CreateTable> implements CreateTableService {

	private final CreateTableHandler createTableHandler;

	@Override
	public Boolean createTable(CreateTableVO createTableVO) {
		String columnsInfo = createTableVO.getColumnsInfo();
		TableInfo tableInfo = new TableInfo();
		tableInfo.setName(createTableVO.getTableName());
		LinkedHashMap<String, Column> columns = new LinkedHashMap<>();
		LinkedHashMap<String, JSONObject> linkedHashMap = JSONUtil.toBean(columnsInfo, LinkedHashMap.class);
		linkedHashMap.forEach((key, val) -> {
			columns.put(key, JSONUtil.toBean(val, Column.class));
		});
		tableInfo.setColumns(columns);
		return createTableHandler.createTable(tableInfo);
	}

}
