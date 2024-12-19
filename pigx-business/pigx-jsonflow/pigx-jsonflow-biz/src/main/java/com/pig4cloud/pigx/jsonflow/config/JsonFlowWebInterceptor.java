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
package com.pig4cloud.pigx.jsonflow.config;

import com.pig4cloud.pigx.jsonflow.support.FlowVarContextHolder;
import com.pig4cloud.pigx.jsonflow.support.OrderInfoContextHolder;
import com.pig4cloud.pigx.jsonflow.support.RunFlowContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JsonFlow WEB全局拦截器
 *
 * @author luolin
 * @date 2022/8/25
 */
@Slf4j
@RequiredArgsConstructor
public class JsonFlowWebInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		RunFlowContextHolder.clear();
		OrderInfoContextHolder.clear();
		FlowVarContextHolder.clear();
	}

}
