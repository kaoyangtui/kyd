package com.pig4cloud.pigx.admin.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/** 咨询类型（consult_type）：仅保留 code / value */
@Getter
public enum ConsultTypeEnum {

    PATENT("PATENT", "专利"),
    RESULT("RESULT", "成果"),
    EXPERT("EXPERT", "专家"),
    DEMAND("DEMAND", "企业需求"),
    DEMAND_IN("DEMAND_IN", "校内需求");

    /** 数据值（建议用于数据库持久化） */
    private final String code;
    /** 展示值（中文） */
    private final String value;

    ConsultTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    /** 作为 JSON 输出时使用 code */
    @JsonValue
    public String jsonValue() {
        return this.code;
    }

    /** 通过 code 反序列化/查找 */
    @JsonCreator
    public static ConsultTypeEnum of(String code) {
        if (code == null) return null;
        for (ConsultTypeEnum e : values()) {
            if (e.code.equalsIgnoreCase(code)) return e;
        }
        throw new IllegalArgumentException("Unknown ConsultType code: " + code);
    }

    /** 返回 [{code, value}]，按声明顺序 */
    public static List<Map<String, String>> options() {
        return Arrays.stream(values()).map(e -> {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("code", e.code);
            m.put("value", e.value);
            return m;
        }).collect(Collectors.toList());
    }
}
