package com.pig4cloud.pigx.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoEnum {
    NO(0, "否"),
    YES(1, "是");

    private final int value;
    private final String label;

    public static YesNoEnum fromValue(Integer value) {
        for (YesNoEnum e : YesNoEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }

    public static YesNoEnum fromLabel(String label) {
        for (YesNoEnum e : YesNoEnum.values()) {
            if (e.label.equals(label) || e.name().equalsIgnoreCase(label)) {
                return e;
            }
        }
        return null;
    }

    // 转换为布尔类型
    public boolean isYes() {
        return this == YES;
    }
    public boolean isNo() {
        return this == NO;
    }
}
