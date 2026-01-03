package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * @author zhaoliang
 */
@Getter
public enum ModelVolcEnum {

    DEEP_SEEK_R1("ep-20250731154231-nn5qb"),
    DEEP_SEEK_V3("ep-20250731155108-98sm4"),
    DOUBAO_SEED_1_6("ep-20250930133740-w4l6h"),
    BATCH_MATCH("ep-bi-20260103165830-p9d6p"),
    ;

    // 获取枚举值
    private final String value;

    // 构造函数
    ModelVolcEnum(String value) {
        this.value = value;
    }

    // 根据值获取对应的枚举
    public static ModelVolcEnum fromValue(String value) {
        for (ModelVolcEnum statusEnum : ModelVolcEnum.values()) {
            if (statusEnum.getValue().equalsIgnoreCase(value)) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
