package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * @author zhaoliang
 */
@Getter
public enum ModelBizNameEnum {
    MATCH("供需匹配")
    ;

    // 获取枚举值
    private final String value;

    // 构造函数
    ModelBizNameEnum(String value) {
        this.value = value;
    }

    // 根据值获取对应的枚举
    public static ModelBizNameEnum fromValue(String value) {
        for (ModelBizNameEnum statusEnum : ModelBizNameEnum.values()) {
            if (statusEnum.getValue().equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
