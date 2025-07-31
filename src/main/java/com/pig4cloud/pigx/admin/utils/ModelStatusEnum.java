package com.pig4cloud.pigx.admin.utils;

import lombok.Getter;

/**
 * @author zhaoliang
 */
@Getter
public enum ModelStatusEnum {
    /**
     * 无状态
     */
    NONE("NONE", 0),

    /**
     * 待处理
     */
    PENDING("PENDING", 1),

    /**
     * 处理中
     */
    TRAIN("TRAIN", 2),

    /**
     * 成功
     */
    SUCCESS("SUCCESS", 3),

    /**
     * 失败
     */
    FAILED("FAILED", 4);

    // 获取枚举值
    private final String value;
    // 获取枚举值
    private final Integer intValue;

    // 构造函数
    ModelStatusEnum(String value, Integer intValue) {
        this.value = value;
        this.intValue = intValue;
    }

    // 根据值获取对应的枚举
    public static ModelStatusEnum fromValue(String value) {
        for (ModelStatusEnum statusEnum : ModelStatusEnum.values()) {
            if (statusEnum.getValue().equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
