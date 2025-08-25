package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 专利状态枚举
 *
 * @author zhaoliang
 */
@Getter
public enum PatentStatusEnum {
    VALID("10", "有效专利"),
    PUBLIC("11", "公开"),
    EXPIRED("20", "失效专利"),
    EXPIRATION("21", "专利权届满的专利"),
    OVERDUE_IN_EXAMINATION("22", "在审超期"),
    IN_PROCESS("30", "在审专利");

    private final String code;
    private final String description;

    PatentStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据 code 获取对应的枚举值
     *
     * @param code 状态码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static PatentStatusEnum getByCode(String code) {
        for (PatentStatusEnum status : PatentStatusEnum.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        return null;
    }
}
