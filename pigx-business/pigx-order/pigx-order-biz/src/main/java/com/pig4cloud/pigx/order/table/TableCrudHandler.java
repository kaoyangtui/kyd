package com.pig4cloud.pigx.order.table;
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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.jsonflow.api.util.DataBaseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 表CRUD模式接口
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TableCrudHandler {

	/**
	 * key变量前缀
	 */
	public static final String KEY_PREFIX = "$";
	public static final String QUOTATION_MARKS = "\"";

	/**
	 * 表数据CRUD操作
	 *
	 * @param tableName 字段与值信息
	 * @param model     字段与值信息
	 * @param data      字段与值信息
	 */
	public List<Map<String, Object>> tableCrudHandle(String tableName, TableCrudModelEnum model, List<Map<String, Object>> data) {
		// 检测数据是否存在
		if (CollUtil.isEmpty(data)) {
			throw new RuntimeException("表数据CRUD操作表[" + tableName + "]数据中至少需要一个字段与值");
		}
		//  判断模式是否正确
		if (Arrays.stream(TableCrudModelEnum.values()).noneMatch(none -> none.equals(model))) {
			throw new RuntimeException("表数据CRUD操作表的SQL模式不正确");
		}
		AnylineService service = ServiceProxy.service();
		// 检测表是否存在
		Table table = service.metadata().table(tableName);
		if(null == table) {
			throw new RuntimeException("表数据CRUD操作表[" + tableName + "]在数据库中不存在");
		}
		// 插入集合时是否检测所有条目的列(默认只检测第一行字段)
		ConfigTable.IS_CHECK_ALL_INSERT_COLUMN = true;
		// 准备DQL DML SQL
		FieldValues fieldValues = this.buildFieldValues(table.getColumns(), tableName, model, data);
		fieldValues.setTableName(tableName);
		fieldValues.setModel(model);
		try {
			DataSet dataSet = this.validateRunFieldValues(service, fieldValues);
			// 执行SQL
			if (TableCrudModelEnum.SELECT.equals(model)) {
				if (dataSet.isEmpty()) return null;
				List<Map<String, Object>> resMaps = new ArrayList<>();
				dataSet.forEach(dataRow -> {
					Map<String, Object> resMap = new HashMap<>();
					dataRow.forEach((key, value) -> {
						String property = DataBaseUtil.columnToProperty(key.toLowerCase());
						// TODO 处理数据类型
						if (value instanceof String) {
							String str = value.toString();
							if (JSONUtil.isTypeJSON(str)) {
								value = JSONUtil.parse(str);
							}
						}
						resMap.put(property, value);
					});
					resMaps.add(resMap);
				});
				return resMaps;
			}
			log.info("表数据CRUD操作处理完成!");
			return null;
		} catch (Exception e) {
			throw new RuntimeException("表数据CRUD操作异常", e);
		}
	}

	/**
	 * 构建字段与条件值对象
	 *
	 * @param data 字段与值
	 */
	public FieldValues buildFieldValues(LinkedHashMap<String, Column> columns, String tableName, TableCrudModelEnum model, List<Map<String, Object>> data) {
		FieldValues res = new FieldValues();
		DataSet fieldValueSet = new DataSet();
		ConfigStore keyValues = new DefaultConfigStore();
		data.forEach(each -> {
			DataRow fieldValues = new DataRow();
			each.forEach((key, value) -> {
				String column;
				if (key.startsWith(KEY_PREFIX)) column = DataBaseUtil.propertyToColumn(key.replace(KEY_PREFIX, StrUtil.EMPTY));
				else column = DataBaseUtil.propertyToColumn(key);
				// 判断列是否存在
				if (!columns.containsKey(column.toUpperCase())) {
					throw new RuntimeException("表数据CRUD操作表[" + tableName + "]中不存在" + column +"字段");
				}
				// 防止非字符类型值为空串
				if (ObjectUtil.isEmpty(value)) value = null;
				if (value instanceof JSONArray || value instanceof JSONObject) {
					value = JSONUtil.toJsonStr(value);
				}
				if (TableCrudModelEnum.SAVE.equals(model)) {
					fieldValues.put(column, value);
				} else if (TableCrudModelEnum.UPDATE.equals(model)) {
					if (key.startsWith(KEY_PREFIX)) keyValues.and(column, value);
					else fieldValues.put(column, value);
				} else if (TableCrudModelEnum.SELECT.equals(model) || TableCrudModelEnum.DEL.equals(model)) {
					keyValues.and(column, value);
				}
			});
			fieldValueSet.add(fieldValues);
		});
		if (TableCrudModelEnum.SAVE.equals(model)) {
			res.setFieldValueSet(fieldValueSet);
		} else if (TableCrudModelEnum.UPDATE.equals(model)) {
			res.setFieldValueSet(fieldValueSet);
			res.setKeyValues(keyValues);
		} else if (TableCrudModelEnum.SELECT.equals(model) || TableCrudModelEnum.DEL.equals(model)) {
			res.setKeyValues(keyValues);
		}
		return res;
	}

	/**
	 * 校验与执行CRUD SQL字段
	 *
	 * @param fieldValues 字段与值信息
	 */
	public DataSet validateRunFieldValues(AnylineService service, FieldValues fieldValues) {
		DataSet resSet = null;
		// 判断增删改查模式
		String tableName = fieldValues.getTableName();
		TableCrudModelEnum model = fieldValues.getModel();
		ConfigStore keyValues = fieldValues.getKeyValues();
		DataSet dataSet = fieldValues.getFieldValueSet();
		if (TableCrudModelEnum.SAVE.equals(model)) {
			if (dataSet.isEmpty()) {
				throw new RuntimeException("新增表[" + tableName + "]数据中至少需要一个字段与值");
			}
			// 批量插入只会取第1条数据字段BUG
			// dataSet.forEach(dataRow -> service.insert(tableName, dataRow, dataRow.keys()));
			service.insert(tableName, dataSet);
		} else if (TableCrudModelEnum.DEL.equals(model)) {
			if (Objects.isNull(keyValues)) {
				throw new RuntimeException("删除表[" + tableName + "]数据中至少需要一个字段与值");
			}
			service.delete(tableName, keyValues);
		} else if (TableCrudModelEnum.UPDATE.equals(model)) {
			DataRow dataRow = dataSet.getRow(0);
			if (Objects.isNull(keyValues) || Objects.isNull(dataRow)) {
				throw new RuntimeException("更新表[" + tableName + "]数据中至少需要一个字段与值");
			}
			service.update(tableName, dataRow, keyValues);
		} else if (TableCrudModelEnum.SELECT.equals(model)) {
			if (Objects.isNull(keyValues)) {
				throw new RuntimeException("查询表[" + tableName + "]数据中至少需要一个字段与值");
			}
			resSet = service.querys(tableName, keyValues);
		}
		return resSet;
	}

}
