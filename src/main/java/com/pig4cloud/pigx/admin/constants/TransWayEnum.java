package com.pig4cloud.pigx.admin.constants;

/**
 * 技术类型枚举
 * 数据库存 int，字段注释写“字典项 key”
 */
public enum TransWayEnum {

    OTHER(0, "其他"),
    LICENSE(1, "技术许可"),
    TRANSFER(2, "技术转让"),
    DEVELOPMENT(3, "技术开发"),
    SERVICE(4, "技术服务"),
    CONSULTING(5, "技术咨询");

    private final int code;
    private final String label;

    TransWayEnum(int code, String label) {
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
     * 根据 code 获取枚举，找不到默认 OTHER
     */
    public static TransWayEnum of(Integer code) {
        if (code == null) return OTHER;
        for (TransWayEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return OTHER;
    }
}
