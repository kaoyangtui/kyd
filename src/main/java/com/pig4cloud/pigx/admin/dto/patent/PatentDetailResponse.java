package com.pig4cloud.pigx.admin.dto.patent;

import com.pig4cloud.pigx.admin.dto.patent.cnipr.CitationInfo;
import com.pig4cloud.pigx.admin.dto.patent.cnipr.Legal;
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

    @Schema(description = "外观专利图URL")
    private List<String> tifDistributePath;

    @Schema(description = "说明书附图")
    private List<String> drawsPic;

    /**
     * 权利要求书
     */
    @Schema(description = "权利要求书")
    private String claimsPath;
    /**
     * 法律信息
     */
    @Schema(description = "法律信息")
    private List<Legal> legalList;
    /**
     * 引证信息
     */
    @Schema(description = "引证信息")
    private List<CitationInfo> citationInfoList;
    /**
     * 上架信息
     */
    @Schema(description = "上架信息")
    private PatentShelfRequest patentShelf;

}