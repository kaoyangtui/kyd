package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 专利状态枚举
 *
 * @author zhaoliang
 */
@Getter
public enum FileBizTypeEnum {
    ATTACHMENT("附件");

    private final String value;

    FileBizTypeEnum(String value) {
        this.value = value;
    }
}
