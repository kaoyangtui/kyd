package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaoliang
 */
public class ExportFilterUtil {
    public static List<Map<String, Object>> filterFields(List<?> dtoList, List<String> fieldKeys) {
        return dtoList.stream()
                .map(dto -> {
                    Map<String, Object> map = BeanUtil.beanToMap(dto, false, true);
                    Map<String, Object> result = new LinkedHashMap<>();
                    fieldKeys.forEach(key -> result.put(key, map.get(key)));
                    return result;
                }).collect(Collectors.toList());
    }
}