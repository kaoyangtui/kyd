package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.bean.BeanUtil;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaoliang
 */
public class ExportFilterUtil {

    // 修改filterFields方法，支持根据字段key获取中文列头
    public static List<Map<String, Object>> filterFields(
            List<?> dtoList,
            List<String> fieldKeys,
            Class<?> dtoClass
    ) {
        // 1. 先从 DTO 类上读取所有字段的 key→title 映射
        List<ExportFieldResponse> fieldResponses = ExportFieldHelper.getFieldsFromDto(dtoClass);
        Map<String, String> headerMap = fieldResponses.stream()
                .collect(Collectors.toMap(
                        ExportFieldResponse::getKey,
                        ExportFieldResponse::getTitle,
                        (oldVal, newVal) -> oldVal,      // 如果 key 重复，保留第一个
                        LinkedHashMap::new               // 保持有序
                ));

        // 2. 遍历 dtoList，逐条构造导出行
        return dtoList.stream().map(dto -> {
            // 禁止下划线转换，保持 key 与 DTO 属性一致
            Map<String, Object> beanMap = BeanUtil.beanToMap(dto, false, false);

            Map<String, Object> row = new LinkedHashMap<>();
            for (String key : fieldKeys) {
                // 如果没在 DTO 注解里找到标题，就用原 key
                String columnHeader = headerMap.getOrDefault(key, key);
                row.put(columnHeader, beanMap.get(key));
            }
            return row;
        }).collect(Collectors.toList());
    }

}
