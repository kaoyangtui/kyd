package com.pig4cloud.pigx.admin.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.anyline.annotation.Component;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * MybatisPlus 自动填充配置
 *
 * @author L.cm
 */
@Slf4j
@Primary
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
            fillValIfNullByName("createBy", userName, metaObject, false);
        }
        if (metaObject.hasSetter("updateBy")) {
            String userName = getUserName();
            fillValIfNullByName("updateBy", userName, metaObject, false);
        }
        if (metaObject.hasSetter("createUserId")) {
            Long userId = getUserId();
            fillValIfNullByName("createUserId", userId, metaObject, false);
        }
        if (metaObject.hasSetter("createUserName")) {
            String name = getName();
            fillValIfNullByName("createUserName", name, metaObject, false);
        }

        if (metaObject.hasSetter("deptId")) {
            Long deptId = getDeptId();
            fillValIfNullByName("deptId", deptId, metaObject, false);
        }

        if (metaObject.hasSetter("deptName")) {
            String deptName = getDeptName();
            fillValIfNullByName("deptName", deptName, metaObject, false);
        }

        if (metaObject.hasSetter("delFlag")) {
            fillValIfNullByName("delFlag", CommonConstants.STATUS_NORMAL, metaObject, true);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("mybatis plus start update fill ....");
        fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
        fillValIfNullByName("updateBy", getUserName(), metaObject, true);
        // 暂时兼容
        fillValIfNullByName("updateUser", getUserId(), metaObject, true);
    }

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     *
     * @param fieldName  属性名
     * @param fieldVal   属性值
     * @param metaObject MetaObject
     * @param isCover    是否覆盖原有值,避免更新操作手动入参
     */
    private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
        // 0. 如果填充值为空
        if (fieldVal == null) {
            return;
        }
        // 1. 没有 get 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
        if (StrUtil.isNotBlank(setValueStr) && !isCover) {
            return;
        }
        // 3. field 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

    /**
     * 获取 spring security 当前的用户名
     *
     * @return 当前用户名
     */
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 匿名接口直接返回
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        if (Optional.ofNullable(authentication).isPresent()) {
            return authentication.getName();
        }

        return null;
    }

    /**
     * 获取 spring security 当前的用户ID
     *
     * @return 当前用户ID
     */
    private Long getUserId() {
        // 登录时为null
        return SecurityUtils.getUser() == null ? null : SecurityUtils.getUser().getId();
    }

    /**
     * @return 当前用户Name
     */
    private String getName() {
        // 登录时为null
        return SecurityUtils.getUser() == null ? null : SecurityUtils.getUser().getName();
    }

    /**
     * 获取 spring security 当前用户的deptId
     *
     * @return 当前用户ID
     */
    private Long getDeptId() {
        // 登录时为null
        return SecurityUtils.getUser() == null ? null : SecurityUtils.getUser().getDeptId();
    }

    private String getDeptName() {
        // 登录时为null
        return SecurityUtils.getUser() == null ? null : SecurityUtils.getUser().getDeptName();
    }
}
