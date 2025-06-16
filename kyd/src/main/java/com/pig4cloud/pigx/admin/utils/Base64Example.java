package com.pig4cloud.pigx.admin.utils;

import cn.hutool.core.codec.Base64;

public class Base64Example {

    public static void main(String[] args) {
        // 原始字符串
        String original = "custom:custom";

        // 编码
        String encoded = Base64.encode(original);
        System.out.println("编码后: " + encoded);

        // 解码
        String decoded = Base64.decodeStr(encoded);
        System.out.println("解码后: " + decoded);
    }
}
