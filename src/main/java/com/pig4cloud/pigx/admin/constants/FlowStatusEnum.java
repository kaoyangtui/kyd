package com.pig4cloud.pigx.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程状态枚举
 */
@Getter
@AllArgsConstructor
public enum FlowStatusEnum {

    RUNNING(-1, "运行中", "运行中"),
    COMPLETE(0, "完成", "完成"),
    CREATED(1, "作废", "作废"),
    REVOKE(2, "撤回", "撤回"),
    DRAFT(-2, "暂存", "暂存");

    private final int value;
    private final String label;
    private final String desc;

    public static FlowStatusEnum fromValue(Integer value) {
        if (value == null) return null;
        for (FlowStatusEnum item : FlowStatusEnum.values()) {
            if (item.value == value) {
                return item;
            }
        }
        return null;
    }
}
