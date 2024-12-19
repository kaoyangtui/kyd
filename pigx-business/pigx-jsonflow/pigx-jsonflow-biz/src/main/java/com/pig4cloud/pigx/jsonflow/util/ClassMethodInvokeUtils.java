package com.pig4cloud.pigx.jsonflow.util;

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

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.KeyValFromEnum;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.KeyValParamClassEnum;
import com.pig4cloud.pigx.jsonflow.service.FlowVariableService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Class类方法调用工具类封装
 *
 * @author luolin
 * @date 2020/2/5 22:31
 */
@Slf4j
@UtilityClass
public class ClassMethodInvokeUtils {

	private final FlowVariableService flowVariableService = SpringContextHolder.getBean(FlowVariableService.class);

	/**
	 * 处理取值来源参数
	 * @param keyVal 取值来源
	 * @return Map
	 */
	// 例如 #beanName.anyMethod(String#admin,SysUser#{"username": "admin"},SysRole#NULL)
    public Object invokeMethodByParamTypes(String keyVal, String flowKey, Long flowInstId, String errMsg) {
		Object[] paramList = new Object[]{};
		Class<?>[] classTypes = new Class[]{};
		boolean numberSign = keyVal.contains(KeyValFromEnum.NUMBER_SIGN.getFrom());
		// 解析参数
		if (numberSign) {
			String paramStr = keyVal.substring(keyVal.indexOf(KeyValFromEnum.LEFT_METHOD.getFrom()) + 1, keyVal.indexOf(KeyValFromEnum.RIGHT_METHOD.getFrom()));
			String[] commas = paramStr.split(StrUtil.COMMA);
			paramList = new Object[commas.length];
			classTypes = new Class[commas.length];
			for (int i = 0; i < commas.length; i++) {
				String comma = commas[i];
				String[] numberSigns = comma.split(KeyValFromEnum.NUMBER_SIGN.getFrom());
				String paramType = numberSigns[0];
				String param = numberSigns[1];
				Class<?> clazz = KeyValParamClassEnum.getEnumByFrom(paramType).getClazz();
				String finalParam = KeyValFromEnum.NUMBER_SIGN.getFrom() + param;
				if (KeyValFromEnum.getVarOrderUser().stream().anyMatch(finalParam::contains)) {
					Object obj = flowVariableService.handleInvokeKeyValFrom(finalParam, flowKey, flowInstId, errMsg);
					param = CommonHttpUrlInvokeUtils.isTypeJSON(obj);
				}
				// 远程调用返回null
				if (KeyValParamClassEnum.NULL.getFrom().equals(param) || param == null) {
					paramList[i] = null;
				} else if (clazz.isAssignableFrom(String.class)) {
					paramList[i] = Objects.toString(param, null);
				} else if (clazz.isAssignableFrom(Long.class)) {
					paramList[i] = Long.valueOf(param);
				} else if (clazz.isAssignableFrom(Integer.class)) {
					paramList[i] = Integer.valueOf(param);
				} else {
					paramList[i] = JSONUtil.toBean(param, clazz);
				}
				classTypes[i] = clazz;
			}
		}
		return ClassMethodInvokeUtils.doInvokeMethod(keyVal, classTypes, paramList);
	}

	/**
	 * 执行方法调用
	 * @param keyVal 取值来源
	 * @param classTypes class类型
	 * @param paramList 请求参数
	 * @return Object
	 */
	public Object doInvokeMethod(String keyVal, Class<?>[] classTypes, Object[] paramList) {
		String beanName = keyVal.substring(keyVal.indexOf(KeyValFromEnum.NUMBER_SIGN.getFrom()) + 1, keyVal.indexOf(StrUtil.DOT));
		String method = keyVal.substring(keyVal.indexOf(StrUtil.DOT) + 1, keyVal.indexOf(KeyValFromEnum.LEFT_METHOD.getFrom()));
		Object bean = SpringContextHolder.getBean(beanName);
		Class<?> clazz = bean.getClass();
		Object res;
		try {
			Method invokeMethod = clazz.getDeclaredMethod(method, classTypes);
			res = invokeMethod.invoke(bean, paramList);
		} catch (Exception e) {
			throw new RuntimeException("KEY取值来源方法执行异常", e);
		}
		// 远程调用
		if (res instanceof R) res = ((R<?>) res).getData();
		return res;
	}

}
