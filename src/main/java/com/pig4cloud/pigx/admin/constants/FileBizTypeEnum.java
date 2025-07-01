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
    CLAIMS("权利要求书附件"),
    DESCRIPTION("说明书附件"),
    ABSTRACT("说明书摘要附件"),
    INVENTOR_CONSENT("其它发明人同意证明附件"),
    ASSIGNMENT_REQUEST("赋权申请附件"),
    TRANSFORM_COMMITMENT("专利转化承诺书附件"),
    CERTIFICATE("证书附件")

    ;

    private final String value;

    FileBizTypeEnum(String value) {
        this.value = value;
    }
}
