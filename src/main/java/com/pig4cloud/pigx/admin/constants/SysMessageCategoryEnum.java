package com.pig4cloud.pigx.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息分类枚举
 */
@Getter
@AllArgsConstructor
public enum SysMessageCategoryEnum {

    NOTICE("0", "公告"),
    LETTER("1", "站内信");

    private final String code;
    private final String desc;

    public static SysMessageCategoryEnum getByCode(String code) {
        for (SysMessageCategoryEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
