package com.pig4cloud.pigx.admin.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;

/**
 * 专利来源库(db_name) 枚举
 * 用于 t_patent_info.db_name 字段取值的归一化与类型映射
 */
@Getter
@Schema(description = "专利来源库(db_name) 枚举")
public enum PatentDbNameEnum {

    @Schema(description = "中国发明申请")
    FMZL("FMZL", "中国发明申请"),

    @Schema(description = "中国发明授权")
    FMSQ("FMSQ", "中国发明授权"),

    @Schema(description = "中国实用新型")
    SYXX("SYXX", "中国实用新型"),

    @Schema(description = "中国外观专利")
    WGZL("WGZL", "中国外观专利");

    @Schema(description = "库名编码")
    private final String code;

    @Schema(description = "库名中文名称")
    private final String name;

    PatentDbNameEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据编码获取枚举
     */
    public static PatentDbNameEnum fromCode(String code) {
        if (code == null) return null;
        return Arrays.stream(values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据名称获取枚举
     */
    public static PatentDbNameEnum fromName(String name) {
        if (name == null) return null;
        return Arrays.stream(values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElse(null);
    }

}
