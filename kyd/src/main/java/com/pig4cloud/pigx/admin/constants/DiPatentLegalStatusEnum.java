package com.pig4cloud.pigx.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhaoliang
 */

@Getter
@AllArgsConstructor
public enum DiPatentLegalStatusEnum {
    PUBLICATION("PUBLICATION", "公开"),
    EXAMINATION("EXAMINATION", "实审"),
    UNAUTHORIZED("UNAUTHORIZED", "未授权"),
    EFFECTIVE("EFFECTIVE", "有效"),
    UNEFFECTIVE("UNEFFECTIVE", "失效"),
    OTHER("OTHER", "其他");
    private final String code;
    private final String desc;

    public static String patentCountry(String code) {
        DiPatentLegalStatusEnum[] values = DiPatentLegalStatusEnum.values();
        for (DiPatentLegalStatusEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
