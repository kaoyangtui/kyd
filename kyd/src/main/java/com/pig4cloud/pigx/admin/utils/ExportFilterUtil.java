package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.bean.BeanUtil;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaoliang
 */
public class ExportFilterUtil {

    // 修改filterFields方法，支持根据字段key获取中文列头
    public static List<Map<String, Object>> filterFields(List<?> dtoList, List<String> fieldKeys, Class<?> dtoClass) {
        // 获取字段对应的中文列头
        List<ExportFieldResponse> fieldResponses = ExportFieldHelper.getFieldsFromDto(dtoClass);

        return dtoList.stream()
                .map(dto -> {
                    Map<String, Object> map = BeanUtil.beanToMap(dto, false, true);
                    Map<String, Object> result = new LinkedHashMap<>();

                    // 为字段设置中文列头
                    fieldKeys.forEach(key -> {
                        // 找到对应字段的中文列头
                        String columnHeader = fieldResponses.stream()
                                .filter(field -> field.getKey().equals(key))  // 使用getKey()进行匹配
                                .map(ExportFieldResponse::getTitle)          // 使用getTitle()获取中文列头
                                .findFirst()
                                .orElse(key);  // 如果没有找到对应的描述，则使用字段名作为列头

                        result.put(columnHeader, map.get(key));
                    });
                    return result;
                }).collect(Collectors.toList());
    }
}
