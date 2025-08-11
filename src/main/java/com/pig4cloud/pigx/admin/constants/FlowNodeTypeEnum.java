package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FlowNodeTypeEnum {

    START("-1", "发起"),
    SERIAL("0", "串行"),
    PARALLEL("1", "并行"),
    END("2", "结束"),
    VIRTUAL("3", "虚拟");

    private final String code;
    private final String description;

    FlowNodeTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FlowNodeTypeEnum fromCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }

    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }
}
