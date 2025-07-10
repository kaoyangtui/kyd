package com.pig4cloud.pigx.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PatentFileTypeEnum {
    ABSTRACT("abstract", "摘要"),
    SPECIFICATION("specification", "说明书"),
    DESIGN("design", "设计图"),
    PDF("pdf", "PDF文件");

    private final String code;
    private final String description;

    public static PatentFileTypeEnum fromCode(String code) {
        for (PatentFileTypeEnum value : values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("未知的专利文件类型: " + code);
    }
}
