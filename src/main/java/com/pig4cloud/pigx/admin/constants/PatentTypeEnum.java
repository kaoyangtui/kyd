package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 专利类型枚举
 *
 * @author zhaoliang
 */
@Getter
public enum PatentTypeEnum {
    INVENTION("1", "发明专利"),
    UTILITY_MODEL("2", "实用新型"),
    DESIGN("3", "外观专利"),
    PCT_INVENTION("8", "PCT发明"),
    PCT_UTILITY_MODEL("9", "PCT实用新型");

    private final String code;
    private final String description;

    PatentTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据 code 获取对应的枚举值
     *
     * @param code 状态码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static PatentTypeEnum getByCode(String code) {
        for (PatentTypeEnum type : PatentTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
