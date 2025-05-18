package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 专利状态枚举
 * @author zhaoliang
 */
@Getter
public enum PatentStatus {
    VALID(10, "有效专利"),
    EXPIRED(20, "失效专利"),
    EXPIRATION(21, "专利权届满的专利"),
    OVERDUE_IN_EXAMINATION(22, "在审超期"),
    IN_PROCESS(30, "在审专利");

    private final int code;
    private final String description;

    PatentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据 code 获取对应的枚举值
     *
     * @param code 状态码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static PatentStatus getByCode(int code) {
        for (PatentStatus status : PatentStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
