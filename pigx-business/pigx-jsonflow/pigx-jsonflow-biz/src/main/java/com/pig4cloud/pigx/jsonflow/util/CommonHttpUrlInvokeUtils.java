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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.jsonflow.api.constant.CommonNbrPool;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.enums.FlowParamRuleEnum;
import com.pig4cloud.pigx.jsonflow.api.entity.FlowRule;
import com.pig4cloud.pigx.jsonflow.api.vo.FlowRuleVO;
import com.pig4cloud.pigx.jsonflow.config.FlowCommonProperties;
import com.pig4cloud.pigx.jsonflow.service.FlowRuleService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 通用Http请求调用工具类封装
 *
 * @author luolin
 * @date 2020/2/5 22:31
 */
@Slf4j
@UtilityClass
public class CommonHttpUrlInvokeUtils {

	public static final String QUESTION = "?";

	private final FlowRuleService flowRuleService = SpringContextHolder.getBean(FlowRuleService.class);
	private final FlowCommonProperties flowCommonProperties = SpringContextHolder.getBean(FlowCommonProperties.class);

	public String isTypeJSON(Object obj) {
		if (ObjectUtil.isEmpty(obj)) return null;
		String res = obj.toString();
		// 判断是否为单一值
		String str = JSONUtil.toJsonStr(obj);
		if (JSONUtil.isTypeJSONObject(str))  {
			JSONObject json = JSONUtil.parseObj(str);
			if (json.size() > 0) res = JSONUtil.toJsonStr(json);
		} else if (JSONUtil.isTypeJSONArray(str))  {
			JSONArray json = JSONUtil.parseArray(str);
			if (json.size() > 0) res = JSONUtil.toJsonStr(json);
		}
		return res;
	}

	public Object handleHttpInvoke(FlowRuleVO flowRuleVO) {
		Map<String, String> headers = CommonHttpUrlInvokeUtils.getFlowRuleHeaders(flowRuleVO);
		flowRuleVO.setParamFrom(CommonNbrPool.STR_1);
		List<FlowRule> listParams = flowRuleService.listFlowRuleParams(flowRuleVO, null);
		String paramType = null;
		Map<String, Object> formMap = new HashMap<>();
		if (CollUtil.isNotEmpty(listParams)) {
			flowRuleVO.setParamFrom(CommonNbrPool.STR_1);
			formMap = flowRuleService.buildFlowRuleParams(flowRuleVO, listParams, null);
			paramType = listParams.get(0).getParamType();
		}
		flowRuleVO.setParamType(paramType);
		return CommonHttpUrlInvokeUtils.doHttpInvokeRes(flowRuleVO, formMap, headers);
	}

	private Map<String, String> getFlowRuleHeaders(FlowRuleVO flowRuleVO) {
		flowRuleVO.setParamFrom(CommonNbrPool.STR_0);
		Map<String, Object> mapHeaders = flowRuleService.buildFlowRuleParams(flowRuleVO, null, null);
		return CommonHttpUrlInvokeUtils.getHeaders(mapHeaders);
	}

	// 获取请求头
	public Map<String, String> getHeaders(Map<String, Object> mapHeaders) {
		if (MapUtil.isEmpty(mapHeaders)) {
			AuthorizationHeaderUtils.extractedMapHeaders(mapHeaders);
		}
		Map<String, String> headers = new HashMap<>();
		mapHeaders.forEach((key, val) -> headers.put(key, val.toString()));
		return headers;
	}

	public Map<String, String> buildMapHeaders(String flowKey, Long defFlowId, Long flowInstId, FlowRuleVO flowRuleVO) {
		flowRuleVO.setDefFlowId(defFlowId);
		flowRuleVO.setFlowKey(flowKey);
		flowRuleVO.setFlowInstId(flowInstId);
		// 查询请求头信息
		return CommonHttpUrlInvokeUtils.getFlowRuleHeaders(flowRuleVO);
	}

	private Object doHttpInvokeRes(FlowRuleVO flowRuleVO, Map<String, Object> formMap, Map<String, String> headers) {
		Object obj = CommonHttpUrlInvokeUtils.doHttpInvoke(flowRuleVO, headers, formMap, null);
		if (ObjectUtil.isEmpty(obj)) return null;
		if (FlowParamRuleEnum.LISTENER.getType().equals(flowRuleVO.getType())) {
			Map<String, Object> resMap = JSONUtil.parseObj(obj);
			flowRuleVO.setParamFrom(CommonNbrPool.STR_2);
			Map<String, Object> mapRes = flowRuleService.buildFlowRuleParams(flowRuleVO, null, resMap);
			if (MapUtil.isEmpty(mapRes)) return obj;
			Long flowInstId = flowRuleVO.getFlowInstId();
			String flowKey = flowRuleVO.getFlowKey();
			Map<String, Object> formMap2 = FlowFormHttpInvokeUtils.buildFlowParams(flowInstId, flowKey);
			formMap2.put(FlowCommonConstants.IS_UPDATE_FORM_DATA, FlowCommonConstants.YES);
			FlowFormHttpInvokeUtils.updateOrderInfo(mapRes, flowInstId, formMap2);
		}
		return obj;
	}

	// 处理默认Http请求地址
	private void extractedHttpUrl(FlowRuleVO flowRuleVO) {
		String httpUrl = flowRuleVO.getHttpUrl();
		if (!HttpUtil.isHttp(httpUrl) && !HttpUtil.isHttps(httpUrl)) {
			if (!httpUrl.startsWith(StrUtil.SLASH)) httpUrl = StrUtil.SLASH + httpUrl;
			httpUrl = flowCommonProperties.getOrderHttpAddress() + httpUrl;
			flowRuleVO.setHttpUrl(httpUrl);
		}
	}

	public Object doHttpInvoke(FlowRuleVO flowRuleVO, Map<String, String> headers, Map<String, Object> formMap, Object body) {
		String result = null;
		String httpMethod = flowRuleVO.getHttpMethod();
		String paramType = flowRuleVO.getParamType();
		CommonHttpUrlInvokeUtils.extractedHttpUrl(flowRuleVO);
		String httpUrl = flowRuleVO.getHttpUrl();
		if (Method.GET.name().equals(httpMethod)) {
			result = CommonHttpUrlInvokeUtils.httpGet(httpUrl, headers, formMap);
		}
		if (Method.POST.name().equals(httpMethod)) {
			if (Objects.isNull(body)) {
				if (CommonNbrPool.STR_0.equals(paramType)) {
					result = CommonHttpUrlInvokeUtils.httpPost(httpUrl, headers, JSONUtil.toJsonStr(formMap));
				} else {
					result = CommonHttpUrlInvokeUtils.httpPostForm(httpUrl, headers, formMap);
				}
			} else {
				result = CommonHttpUrlInvokeUtils.httpPostBodyParam(httpUrl, headers, JSONUtil.toJsonStr(body), formMap);
			}
		}
		if (Method.PUT.name().equals(httpMethod)) {
			if (Objects.isNull(body)) {
				if (CommonNbrPool.STR_0.equals(paramType)) {
					result = CommonHttpUrlInvokeUtils.httpPut(httpUrl, headers, JSONUtil.toJsonStr(formMap));
				} else {
					result = CommonHttpUrlInvokeUtils.httpPutForm(httpUrl, headers, formMap);
				}
			} else {
				result = CommonHttpUrlInvokeUtils.httpPutBodyParam(httpUrl, headers, JSONUtil.toJsonStr(body), formMap);
			}
		}
		if (Method.DELETE.name().equals(httpMethod)) {
			if (CommonNbrPool.STR_0.equals(paramType)) {
				result = CommonHttpUrlInvokeUtils.httpDelete(httpUrl, headers, JSONUtil.toJsonStr(formMap));
			} else {
				result = CommonHttpUrlInvokeUtils.httpDeleteForm(httpUrl, headers, formMap);
			}
		}
		JSONObject obj = JSONUtil.parseObj(result, true);
		String code = obj.getStr(FlowCommonConstants.CODE);
		if (!CommonNbrPool.STR_0.equals(code)) {
			throw new ValidationException("Http远程请求失败: " + obj.getStr(FlowCommonConstants.MSG));
		}
		return obj.get(FlowCommonConstants.DATA);
	}

	/**
	 * Get 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param formMap 请求表单
	 */
	public String httpGet(String url, Map<String, String> headers, Map<String, Object> formMap) {
		String object;
		try (HttpResponse execute = HttpRequest.get(url)
				.addHeaders(headers)
				.form(formMap)
				.execute()) {
			object = execute.body();
		}
		catch (Exception e) {
			throw new ValidationException("Get 远程请求异常", e);
		}
		log.info("Get 请求地址:" + url + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Post 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param body    请求体
	 */
	public String httpPost(String url, Map<String, String> headers, String body) {
		String object;
		try (HttpResponse execute = HttpRequest.post(url)
				.addHeaders(headers)
				.body(body)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Post 远程请求异常", e);
		}
		log.info("Post 请求地址:" + url + ", 参数:" + body + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Post Form 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param formMap 请求表单
	 */
	public String httpPostForm(String url, Map<String, String> headers, Map<String, Object> formMap) {
		String object;
		try (HttpResponse execute = HttpRequest.post(url)
				.addHeaders(headers)
				.form(formMap)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Post Form 远程请求异常", e);
		}
		log.info("Post Form 请求地址:" + url + ", 参数:" + formMap + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Post Body Param 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param body    请求体
	 * @param queryMap 查询参数
	 */
	public String httpPostBodyParam(String url, Map<String, String> headers, String body, Map<String, Object> queryMap) {
		String object;
		url = url + QUESTION + HttpUtil.toParams(queryMap);
		try (HttpResponse execute = HttpRequest.post(url)
				.addHeaders(headers)
				.body(body)
				// .form(formMap)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Post Body Param 远程请求异常", e);
		}
		log.info("Post Body Param 请求地址:" + url + ", 参数1:" + body + ", 参数2:" + queryMap + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Put 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param body    请求体
	 */
	public String httpPut(String url, Map<String, String> headers, String body) {
		String object;
		try (HttpResponse execute = HttpRequest.put(url)
				.addHeaders(headers)
				.body(body)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Put 远程请求异常", e);
		}
		log.info("Put 请求地址:" + url + ", 参数:" + body + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Put Form 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param formMap 请求表单
	 */
	public String httpPutForm(String url, Map<String, String> headers, Map<String, Object> formMap) {
		String object;
		try (HttpResponse execute = HttpRequest.put(url)
				.addHeaders(headers)
				.form(formMap)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Put Form 远程请求异常", e);
		}
		log.info("Put Form 请求地址:" + url + ", 参数:" + formMap + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Put Body Param 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param body    请求体
	 * @param queryMap 查询参数
	 */
	public String httpPutBodyParam(String url, Map<String, String> headers, String body, Map<String, Object> queryMap) {
		String object;
		url = url + QUESTION + HttpUtil.toParams(queryMap);
		try (HttpResponse execute = HttpRequest.put(url)
				.addHeaders(headers)
				.body(body)
				// .form(formMap)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Put Body Param 远程请求异常", e);
		}
		log.info("Put Body Param 请求地址:" + url + ", 参数1:" + body + ", 参数2:" + queryMap + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Delete 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param body    请求体
	 */
	public String httpDelete(String url, Map<String, String> headers, String body) {
		String object;
		try (HttpResponse execute = HttpRequest.delete(url)
				.addHeaders(headers)
				.body(body)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Delete 远程请求异常", e);
		}
		log.info("Delete 请求地址:" + url + ", 参数:" + body + ", 返回结果:" + object);
		return object;
	}

	/**
	 * Delete Form 请求方法
	 *
	 * @param url     请求地址
	 * @param headers 请求头
	 * @param formMap 请求表单
	 */
	public String httpDeleteForm(String url, Map<String, String> headers, Map<String, Object> formMap) {
		String object;
		try (HttpResponse execute = HttpRequest.delete(url)
				.addHeaders(headers)
				.form(formMap)
				.execute()) {
			object = execute.body();
		} catch (Exception e) {
			throw new ValidationException("Delete Form 远程请求异常", e);
		}
		log.info("Delete Form 请求地址:" + url + ", 参数:" + formMap + ", 返回结果:" + object);
		return object;
	}

}
