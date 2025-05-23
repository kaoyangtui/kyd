package com.pig4cloud.pigx.admin.constants;

import lombok.Getter;

/**
 * 专利状态枚举
 *
 * @author zhaoliang
 */
@Getter
public enum FileBizTypeEnum {
    ATTACHMENT("附件"),
    CLAIMS("专利权利要求书附件"),
    DESCRIPTION("专利说明书附件"),
    ABSTRACT("专利说明书摘要附件"),
    SOFT_COPY("软著证书附件"),
    PLANT_VARIETY("植物新品种权证书"),
    IC_LAYOUT("集成电路布图证书"),

    ;

    private final String value;

    FileBizTypeEnum(String value) {
        this.value = value;
    }
}
