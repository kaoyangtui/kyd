package com.pig4cloud.pigx.jsonflow.util;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowCommonConstants;
import com.pig4cloud.pigx.jsonflow.api.constant.FlowEntityInfoConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * Authorization请求头判断封装
 *
 * @author luolin
 * @date 2020/2/5 22:31
 */
@Slf4j
@UtilityClass
public class AuthorizationHeaderUtils {

	// 判断是否微服务版本
	public void extractedMapHeaders(Map<String, Object> mapHeaders) {
		String token = WebUtils.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
		if (StrUtil.isBlank(token)) {
			String userIdStr = WebUtils.getRequest().getHeader(FlowCommonConstants.USERID);
			mapHeaders.put(FlowCommonConstants.USERID, userIdStr);
		} else {
			mapHeaders.put(HttpHeaders.AUTHORIZATION, token);
		}
	}

	// 判断是否微服务版本
	public String validateAuthorization(String key) {
		String token = WebUtils.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
		if (StrUtil.isNotBlank(token)) {
			if (FlowEntityInfoConstants.USER_ID.equals(key)) key = FlowEntityInfoConstants.ID;
		}
		return key;
	}

}
