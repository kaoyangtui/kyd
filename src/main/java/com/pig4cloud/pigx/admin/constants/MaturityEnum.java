package com.pig4cloud.pigx.admin.constants;

/**
 * 实验阶段枚举
 * 数据库存 int，字段注释写“字典项 key”
 */
public enum MaturityEnum {

    LAB_STAGE(1, "实验室阶段"),
    PILOT(2, "小试"),
    MID_TEST(3, "中试"),
    INDUSTRIAL_PILOT(4, "可产业化实验");

    private final int code;
    private final String label;

    MaturityEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 根据 code 获取枚举，找不到返回 null（你也可以改成抛异常）
     */
    public static MaturityEnum of(Integer code) {
        if (code == null) return null;
        for (MaturityEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
