package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * @author zhaoliang
 */
@Getter
public enum ModelAliEnum {

    DEEP_SEEK_R1("deepseek-r1"),
    DEEP_SEEK_V3("deepseek-v3");

    // 获取枚举值
    private final String value;

    // 构造函数
    ModelAliEnum(String value) {
        this.value = value;
    }

    // 根据值获取对应的枚举
    public static ModelAliEnum fromValue(String value) {
        for (ModelAliEnum statusEnum : ModelAliEnum.values()) {
            if (statusEnum.getValue().equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
