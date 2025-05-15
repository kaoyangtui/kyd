package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.util.ReflectUtil;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportFieldResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoliang
 */
public class ExportFieldHelper {
    public static List<ExportFieldResponse> getFieldsFromDto(Class<?> dtoClass) {
        Field[] fields = ReflectUtil.getFields(dtoClass);
        List<ExportFieldResponse> result = new ArrayList<>();
        for (Field field : fields) {
            Schema schema = field.getAnnotation(Schema.class);
            if (schema != null) {
                result.add(new ExportFieldResponse(field.getName(), schema.description()));
            }
        }
        return result;
    }
}