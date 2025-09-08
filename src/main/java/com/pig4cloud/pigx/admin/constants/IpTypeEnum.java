package com.pig4cloud.pigx.admin.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 知识产权类型枚举
 * 说明：将转化细分为四类，直接作为类型常量放入该枚举
 */
@Getter
@Schema(description = "知识产权类型枚举")
public enum IpTypeEnum {

    /**
     * 发明专利
     */
    INVENTION("PATENT_INV", "发明专利"),

    /**
     * 实用新型专利
     */
    UTILITY_MODEL("PATENT_UTL", "实用新型专利"),

    /**
     * 外观设计专利
     */
    DESIGN("PATENT_DSN", "外观设计专利"),

    /**
     * 软件著作权
     */
    SOFTWARE("SOFT", "软著"),

    /**
     * 标准
     */
    STANDARD("STD", "标准"),

    /**
     * 植物新品种
     */
    PLANT_VARIETY("PVP", "植物新品种"),

    /**
     * 集成电路布图设计
     */
    IC_LAYOUT("IC", "集成电路布图设计"),

    /**
     * 赋权
     */
    EMPOWER("EMPOWER", "赋权"),

    /**
     * 转化（开放许可发布）
     */
    TRANS_OPEN_LICENSE_PUBLISH("TRANS_OPEN_LICENSE_PUBLISH", "转化（开放许可发布）"),

    /**
     * 转化（开放许可达成）
     */
    TRANS_OPEN_LICENSE_DEAL("TRANS_OPEN_LICENSE_DEAL", "转化（开放许可达成）"),

    /**
     * 转化（转让）
     */
    TRANS_TRANSFER("TRANS_TRANSFER", "转化（转让）"),

    /**
     * 转化（作价入股）
     */
    TRANS_EQUITY("TRANS_EQUITY", "转化（作价入股）"),

    /**
     * 旧版总类：专利转化
     */
    @Deprecated
    TRANSFORM("TRANS", "专利转化");

    @Schema(description = "类型编码")
    private final String code;

    @Schema(description = "类型名称")
    private final String name;

    IpTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据编码获取枚举
     */
    public static IpTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据中文名称获取枚举
     */
    public static IpTypeEnum fromName(String name) {
        if (name == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * 是否为专利三类之一
     */
    public static boolean isPatentTriad(String code) {
        IpTypeEnum t = fromCode(code);
        return t == INVENTION || t == UTILITY_MODEL || t == DESIGN;
    }

    /**
     * 是否为转化细分类
     */
    public static boolean isTransformSubtype(String code) {
        IpTypeEnum t = fromCode(code);
        return t == TRANS_OPEN_LICENSE_PUBLISH
                || t == TRANS_OPEN_LICENSE_DEAL
                || t == TRANS_TRANSFER
                || t == TRANS_EQUITY;
    }

    /**
     * 转化细分类的集合
     */
    public static Set<IpTypeEnum> transformSubtypes() {
        Set<IpTypeEnum> set = new HashSet<>();
        set.add(TRANS_OPEN_LICENSE_PUBLISH);
        set.add(TRANS_OPEN_LICENSE_DEAL);
        set.add(TRANS_TRANSFER);
        set.add(TRANS_EQUITY);
        return set;
    }
}
