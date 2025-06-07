package com.pig4cloud.pigx.admin.utils;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用 Excel 导出工具
 */
public class ExcelExportUtil {

    private ExcelExportUtil() {
    }

    /**
     * 根据 Map 列表直接导出
     *
     * @param response  Http 响应
     * @param fileName  导出文件名（不带后缀）
     * @param sheetName sheet 名称
     * @param rows      每行一个 Map，key 顺序就是列顺序，value 为单元格值
     */
    public static void exportByMap(
            HttpServletResponse response,
            String fileName,
            String sheetName,
            List<Map<String, Object>> rows
    ) throws IOException {
        if (rows == null) {
            rows = Collections.emptyList();
        }

        // 动态表头：取第一行的 key 顺序
        List<String> headerKeys = rows.isEmpty()
                ? Collections.emptyList()
                : new ArrayList<>(rows.get(0).keySet());
        List<List<String>> head = headerKeys.stream()
                .map(title -> Collections.singletonList(title))
                .collect(Collectors.toList());

        // 动态数据体
        List<List<Object>> data = rows.stream()
                .map(row -> headerKeys.stream()
                        .map(row::get)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        // 写响应头
        setResponseHeader(response, fileName);

        // 写 Excel
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet(sheetName)
                .doWrite(data);
    }

    /**
     * 根据业务 Bean + ExportFilterUtil 过滤出的 Map 自动导出
     *
     * @param response  Http 响应
     * @param fileName  导出文件名（不带后缀）
     * @param sheetName sheet 名称
     * @param records   原始 DTO 列表
     * @param fieldKeys 要导出的字段 key 列表
     * @param dtoClass  DTO 类型，用于 ExportFilterUtil.filterFields
     */
    public static <T> void exportByBean(
            HttpServletResponse response,
            String fileName,
            String sheetName,
            List<T> records,
            List<String> fieldKeys,
            Class<T> dtoClass
    ) throws IOException {
        List<Map<String, Object>> rows = ExportFilterUtil.filterFields(records, fieldKeys, dtoClass);
        exportByMap(response, fileName, sheetName, rows);
    }

    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encoded);
    }
}
