package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.List;

public class CodeUtils {

    /**
     * 将 JSON 数组格式的代码串（如 ["C4090","O8131","O8199"]）解析并用分号连接。
     * 如果只有一个值，则直接返回该值；输入为空或解析后为空则返回空字符串。
     *
     * @param jsonArrayStr JSON 数组格式的字符串
     * @return 分号拼接后的字符串
     */
    public static String formatCodes(String jsonArrayStr) {
        if (StrUtil.isBlank(jsonArrayStr)) {
            return StrUtil.EMPTY;
        }
        // 解析 JSON 数组
        JSONArray arr = JSONUtil.parseArray(jsonArrayStr);
        List<String> codes = arr.toList(String.class);
        if (CollUtil.isEmpty(codes)) {
            return StrUtil.EMPTY;
        }
        // 用分号拼接
        return StrUtil.join(";", codes);
    }

    /**
     * 解析 JSON 数组格式的代码串并返回第一个元素；为空返回空字符串。
     *
     * @param jsonArrayStr JSON 数组格式的字符串
     * @return 第一个代码元素或空字符串
     */
    public static String getFirstCode(String jsonArrayStr) {
        if (StrUtil.isBlank(jsonArrayStr)) {
            return StrUtil.EMPTY;
        }
        JSONArray arr = JSONUtil.parseArray(jsonArrayStr);
        return arr.isEmpty() ? StrUtil.EMPTY : arr.getStr(0);
    }

    public static void main(String[] args) {
        String input1 = "[\"丛丹丹\",\"英皓\",\"张可为\",\"王洪智\",\"孙乐\",\"靖成\",\"张晟\",\"甘春露\",\"牟振\"]";
        String input2 = "[\"丛丹丹\"]";
        String input3 = "[]";

        System.out.println(formatCodes(input1)); // 输出: C4090;O8131;O8199
        System.out.println(formatCodes(input2)); // 输出: C4090
        System.out.println(formatCodes(input3)); // 输出: （空字符串）
    }
}
