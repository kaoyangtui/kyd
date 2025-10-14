package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 严格类型 Bean 拷贝工具：
 * - 仅当源值类型可赋给目标字段类型时才复制（同类型或子类）
 * - 不做任何自动类型转换（例如 String -> Long 之类的一律跳过）
 * - 默认 ignoreNull（源值为 null 不覆盖目标）
 * - 支持忽略字段
 */
public final class CopyUtil {

    private CopyUtil() {
    }

    /**
     * 将 source 的属性拷贝到已存在的 target 上（严格同类型，忽略 null）
     */
    public static <T> T copyProperties(Object source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            return target;
        }
        BeanUtil.copyProperties(source, target, strictOptions(ignoreProperties));
        return target;
    }

    /**
     * 新建 target 实例并拷贝（严格同类型，忽略 null）
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, String... ignoreProperties) {
        if (source == null || targetClass == null) {
            return null;
        }
        T target = ReflectUtil.newInstance(targetClass);
        return copyProperties(source, target, ignoreProperties);
    }

    /**
     * 批量拷贝集合（严格同类型，忽略 null）
     */
    public static <S, T> List<T> copyPropertiesList(Collection<S> sources, Class<T> targetClass, String... ignoreProperties) {
        if (CollUtil.isEmpty(sources)) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S s : sources) {
            list.add(copyProperties(s, targetClass, ignoreProperties));
        }
        return list;
    }

    /**
     * 与 {@link #copyProperties(Object, Object, String...)} 相同，但允许覆盖为 null（不忽略 null）
     * 场景：需要明确把目标字段“清空”为 null。
     */
    public static <T> T copyPropertiesAllowNull(Object source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            return target;
        }
        BeanUtil.copyProperties(source, target, strictOptions(false, ignoreProperties));
        return target;
    }

    /* ====================== 内部：构建严格 CopyOptions ====================== */

    /**
     * 默认忽略 null
     */
    private static CopyOptions strictOptions(String... ignoreProperties) {
        return strictOptions(true, ignoreProperties);
    }

    /**
     * @param ignoreNull 是否忽略 null
     */
    private static CopyOptions strictOptions(boolean ignoreNull, String... ignoreProperties) {
        return CopyOptions.create()
                .setIgnoreNullValue(ignoreNull)
                .setIgnoreProperties(ignoreProperties)
                // 严格转换器：仅当 value 的实际类型可赋给目标类型时才返回原值，否则返回 null -> 配合 ignoreNullValue 跳过
                .setConverter((Type targetType, Object value) -> {
                    if (value == null) return null;
                    if (targetType instanceof Class<?>) {
                        Class<?> targetClass = (Class<?>) targetType;
                        if (targetClass.isAssignableFrom(value.getClass())) {
                            return value; // 同类型或子类，直接用原值
                        }
                    }
                    return null; // 其他一律不转换（跳过）
                });
    }
}
