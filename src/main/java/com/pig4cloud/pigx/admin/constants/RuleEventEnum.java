package com.pig4cloud.pigx.admin.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Schema(description = "规则事件编码枚举")
public enum RuleEventEnum {

    @Schema(description = "申请公示")
    APPLY_PUB("APPLY_PUB", "申请公示"),

    @Schema(description = "授权公示")
    GRANT_PUB("GRANT_PUB", "授权公示"),

    @Schema(description = "转化-允许")
    TRANS_LICENSE_ALLOW("TRANS_LICENSE_ALLOW", "转化-允许"),

    @Schema(description = "转化-达成")
    TRANS_LICENSE_DEAL("TRANS_LICENSE_DEAL", "转化-达成");

    @Schema(description = "事件编码")
    private final String code;

    @Schema(description = "事件名称")
    private final String name;

    RuleEventEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RuleEventEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public static RuleEventEnum fromName(String name) {
        if (name == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
