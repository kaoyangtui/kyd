package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 专利详情
 *
 * @author pigx
 * @date 2025-05-31 10:49:31
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利信息")
@Data
public class PatentDetailResponse extends PatentInfoResponse {

    /**
     * 主权项
     */
    @Schema(description = "主权项")
    private String cl;

    /**
     * 封面图
     */
    @Schema(description = "封面图")
    private String cover;

    /**
     * 权利要求书
     */
    @Schema(description = "权利要求书")
    private String claimsPath;

    /**
     * 说明书
     */
    @Schema(description = "说明书")
    private String instrPath;

    @Schema(description = "法律信息\n" +
            "newInfo\t\t\t\t最新\n" +
            "prsDate\t\t\t\t公告日\n" +
            "prsCode\t\t\t\t状态\n" +
            "strLegalCode\t代码\n" +
            "codeExpl\t\t\t详细")
    private String legalList;

    @Schema(description = "简单同族信息\n" +
            "appNumber\t申请号\n" +
            "appDate\t\t申请日\n" +
            "pubNumber\t公开公告号\n" +
            "pubDate\t\t公开公告日")
    private String simpleFamilyList;

    @Schema(description = "扩展同族信息\n" +
            "appNumber\t申请号\n" +
            "appDate\t\t申请日\n" +
            "pubNumber\t公开公告号\n" +
            "pubDate\t\t公开公告日")
    private String extendFamilyList;

    @Schema(description = "海外同族信息\n" +
            "country\t\t\t国别\n" +
            "appQuantity\t申请数\n" +
            "pubQuantity\t公开数")
    private String overseasFamilyInfo;

    @Schema(description = "引证信息\n" +
            "srepPhase\t\t\t\t\t\t\t\t引证阶段\n" +
            "citationInfoNo\t\t\t\t\t引证号\n" +
            "citationInfoApplicant\t\t引证申请人\n" +
            "citationInfoIpc\t\t\t\t\t分类号\n" +
            "citationInfoCountry\t\t\t引证国家\n" +
            "citationInfoLocation\t\t引证文献位置\n" +
            "citationInfoRichContent\t详细")
    private String citationInfo;
    /**
     * 上架信息
     */
    @Schema(description = "上架信息")
    private PatentShelfRequest patentShelf;

    /**
     * 说明书附图URL
     */
    @Schema(description = "说明书附图URL")
    private List<String> drawsPic;

    /**
     * 外观专利图
     */
    @Schema(description = "外观专利图")
    private List<String> tifDistributePath;
}