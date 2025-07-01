package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zhaoliang
 */
public class CniprExpUtils {

    public static String getExpEq(String key, String value) {
        return StrUtil.format("{} = ('{}')", key, value);
    }

    public static String getExpBetween(String key, String value1, String value2) {
        return StrUtil.format("{} = [{} to {}]", key, value1, value2);
    }

    public static String getExpIn(String key, List<String> valueList) {
        if (valueList == null || valueList.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(" = (");

        for (int i = 0; i < valueList.size(); i++) {
            if (i > 0) {
                builder.append(" OR ");
            }
            builder.append("'").append(valueList.get(i)).append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    public static String getExpMap(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();

        // 遍历 map，生成条件表达式
        for (Map.Entry<String, String> entry : map.entrySet()) {
            // 如果 builder 中已经有内容，添加 OR 连接符
            if (builder.length() > 0) {
                builder.append(" OR ");
            }
            // 添加键值对条件
            builder.append(entry.getKey())
                    .append(" = ('")
                    .append(entry.getValue())
                    .append("')");
        }
        return builder.toString();
    }


    public static String getExpAnd(List<String> valueList) {
        if (valueList == null || valueList.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < valueList.size(); i++) {
            if (i > 0) {
                builder.append(" AND ");
            }
            builder.append("(").append(valueList.get(i)).append(")");
        }
        return builder.toString();
    }
}
