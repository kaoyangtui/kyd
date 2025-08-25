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

    /**
     * 法律信息
     */
    @Schema(description = "法律信息")
    private String legalList;

    /**
     * 简单同族信息
     */
    @Schema(description = "简单同族信息")
    private String simpleFamilyList;

    /**
     * 扩展同族信息
     */
    @Schema(description = "扩展同族信息")
    private String extendFamilyList;

    /**
     * 海外同族信息
     */
    @Schema(description = "海外同族信息")
    private String overseasFamilyInfo;

    /**
     * 引证信息
     */
    @Schema(description = "引证信息")
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