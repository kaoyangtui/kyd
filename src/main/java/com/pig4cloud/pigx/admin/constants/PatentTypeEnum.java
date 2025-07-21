package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 专利类型枚举
 *
 * @author zhaoliang
 */
@Getter
public enum PatentTypeEnum {
    INVENTION(1, "发明专利"),
    UTILITY_MODEL(2, "实用新型"),
    DESIGN(3, "外观专利"),
    PCT_INVENTION(8, "PCT发明"),
    PCT_UTILITY_MODEL(9, "PCT实用新型");

    private final int code;
    private final String description;

    PatentTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据 code 获取对应的枚举值
     *
     * @param code 状态码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static PatentTypeEnum getByCode(int code) {
        for (PatentTypeEnum type : PatentTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
