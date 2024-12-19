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
import lombok.Data;
import org.anyline.data.param.ConfigStore;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;

/**
 * 字段与值信息
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Data
public class FieldValues {

	/**
	 * 表名称
	 */
	private String tableName;

	/**
	 * 字段与值变量名
	 */
	private DataRow fieldValues;
	/**
	 * 字段与值变量名集合
	 */
	private DataSet fieldValueSet;

	/**
	 * 条件与值变量名
	 */
	private ConfigStore keyValues;

	/**
	 * SQL模式
	 */
	private TableCrudModelEnum model;

}
