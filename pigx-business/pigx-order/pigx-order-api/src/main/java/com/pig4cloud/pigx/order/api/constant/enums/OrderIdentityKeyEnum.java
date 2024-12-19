package com.pig4cloud.pigx.order.api.constant.enums;

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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.order.api.constant.OrderCommonConstants;
import com.pig4cloud.pigx.order.api.constant.OrderEntityInfoConstants;
import com.pig4cloud.pigx.order.api.entity.AskLeave;
import com.pig4cloud.pigx.order.api.entity.HandoverFlow;
import com.pig4cloud.pigx.order.api.entity.RunApplication;
import com.pig4cloud.pigx.order.api.vo.AskLeaveVO;
import com.pig4cloud.pigx.order.api.vo.HandoverFlowVO;
import com.pig4cloud.pigx.order.api.vo.RunApplicationVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.ValidationException;
import java.util.Map;
import java.util.Objects;

/**
 * 流程标识 KEY 对应的工单service
 * @author luolin
 * @date 2020/2/7 13:23
 */
@Getter
@AllArgsConstructor
public enum OrderIdentityKeyEnum {

	RUN_APPLICATION("RunApplication","RunApplicationServiceImpl","runApplicationServiceImpl", RunApplication.class, RunApplicationVO.class,"BGSQ-GD","办公申请工单")
    ,HANDOVER_FLOW("HandoverFlow","HandoverFlowServiceImpl","handoverFlowServiceImpl", HandoverFlow.class, HandoverFlowVO.class,"GZJJ-GD","工作交接工单")
    ,ASK_LEAVE("AskLeave","AskLeaveServiceImpl","askLeaveServiceImpl", AskLeave.class, AskLeaveVO.class,"QJ-GD","请假工单");

    /**
     * 必须为实体类类名
     */
    private final String key;
    private final String serviceKey;
    private final String beanId;
    private final Class<?> clazz;
    private final Class<?> clazzVo;
    private final String code;
    private final String name;

    /**
     * 返回枚举常量对象
     * @param key 业务KEY
     */
    public static OrderIdentityKeyEnum getEnumByKey(String key){
        if (StrUtil.isEmpty(key)) throw new ValidationException("流程标识KEY不存在");
        for (OrderIdentityKeyEnum each: values()) {
            if(each.getKey().equals(key)) return  each;
			if(each.getServiceKey().equals(key)) return  each;
            if(each.getBeanId().equals(key)) return  each;
        }
        return null;
    }

	/**
	 * 根据params任意参数返回枚举
	 * @param params 参数
	 */
	public static OrderIdentityKeyEnum getEnumByParams(Map<String, Object> params){
		String flowKey = MapUtil.getStr(params, OrderEntityInfoConstants.FLOW_KEY);
		if (StrUtil.isEmpty(flowKey)) throw new ValidationException("流程标识KEY不存在");
		OrderIdentityKeyEnum enumByKey = OrderIdentityKeyEnum.getEnumByKey(flowKey);
		if (Objects.isNull(enumByKey)) {
			return OrderIdentityKeyEnum.getEnumByKey(OrderCommonConstants.DEF_SERVICE_IMPL);
		}
		return enumByKey;
	}

}
