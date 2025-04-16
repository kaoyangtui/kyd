package com.pig4cloud.pigx.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * MybatisPlus 自动填充配置
 *
 * 优化说明：
 * 1. 对每个需要填充的字段先判断 metaObject.hasSetter("字段") 是否存在
 * 2. 获取用户信息时增加判断和异常捕获，确保空值安全
 *
 * @author L.cm
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.debug("mybatis plus start insert fill ....");
		LocalDateTime now = LocalDateTime.now();

		if (metaObject.hasSetter("createTime")) {
			fillValIfNullByName("createTime", now, metaObject, true);
		}
		if (metaObject.hasSetter("updateTime")) {
			fillValIfNullByName("updateTime", now, metaObject, true);
		}
		if (metaObject.hasSetter("createBy")) {
			String userName = getUserName();
			fillValIfNullByName("createBy", userName, metaObject, true);
		}
		if (metaObject.hasSetter("updateBy")) {
			String userName = getUserName();
			fillValIfNullByName("updateBy", userName, metaObject, true);
		}
		if (metaObject.hasSetter("createUser")) {
			Long userId = getUserId();
			fillValIfNullByName("createUser", userId, metaObject, true);
		}
		if (metaObject.hasSetter("updateUser")) {
			Long userId = getUserId();
			fillValIfNullByName("updateUser", userId, metaObject, true);
		}
		if (metaObject.hasSetter("delFlag")) {
			fillValIfNullByName("delFlag", CommonConstants.STATUS_NORMAL, metaObject, true);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.debug("mybatis plus start update fill ....");
		if (metaObject.hasSetter("updateTime")) {
			fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
		}
		if (metaObject.hasSetter("updateBy")) {
			String userName = getUserName();
			fillValIfNullByName("updateBy", userName, metaObject, true);
		}
		if (metaObject.hasSetter("updateUser")) {
			Long userId = getUserId();
			fillValIfNullByName("updateUser", userId, metaObject, true);
		}
	}

	/**
	 * 填充值，先判断是否有手动设置，优先手动设置的值
	 *
	 * @param fieldName 属性名
	 * @param fieldVal  属性值
	 * @param metaObject MetaObject
	 * @param isCover   是否覆盖原有值（避免更新操作时覆盖手动传入的值）
	 */
	private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
		// 如果填充值为空则直接返回
		if (fieldVal == null) {
			return;
		}
		// 如果没有 setter 方法则直接返回
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 如果用户有手动设置的值且不需要覆盖，则返回
		Object userSetValue = metaObject.getValue(fieldName);
		String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
		if (StrUtil.isNotBlank(setValueStr) && !isCover) {
			return;
		}
		// 如果类型匹配则进行赋值
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
			metaObject.setValue(fieldName, fieldVal);
		}
	}

	/**
	 * 获取 Spring Security 当前的用户名
	 *
	 * @return 当前用户名，若未登录或异常则返回 null
	 */
	private String getUserName() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			// 匿名或未登录直接返回 null
			if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
				return null;
			}
			return authentication.getName();
		} catch (Exception e) {
			log.error("Error getting user name from SecurityContextHolder", e);
			return null;
		}
	}

	/**
	 * 获取 Spring Security 当前的用户ID
	 *
	 * @return 当前用户ID，若未登录或异常则返回 null
	 */
	private Long getUserId() {
		try {
			if (SecurityUtils.getUser() != null) {
				return SecurityUtils.getUser().getId();
			}
		} catch (Exception e) {
			log.error("Error getting user ID from SecurityUtils", e);
		}
		return null;
	}
}
