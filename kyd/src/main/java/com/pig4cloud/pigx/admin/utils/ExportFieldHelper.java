package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.util.ReflectUtil;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldResponse;
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

    /**
     * 构建导出字段列表响应，支持传入嵌套结构的主表 VO。
     *
     * @param bizCode 业务编码
     * @param dtoClass 导出字段实际定义的主表 VO class（如 IpAssignMainVO.class）
     * @return 导出字段列表响应结构
     */
    public static ExportFieldListResponse buildExportFieldList(String bizCode, Class<?> dtoClass) {
        List<ExportFieldResponse> fields = getFieldsFromDto(dtoClass);
        ExportFieldListResponse response = new ExportFieldListResponse();
        response.setBizCode(bizCode);
        response.setFields(fields);
        return response;
    }

}