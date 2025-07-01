package com.pig4cloud.pigx.admin.dto.patent.cnipr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "引证信息 DTO")
public class CitationInfo {

    @Schema(description = "引证阶段")
    private String srepPhase;

    @Schema(description = "引证号（引证文献的公开号）")
    private List<String> citationInfoNo;

    @Schema(description = "引证申请人")
    private String citationInfoApplicant;

    @Schema(description = "分类号")
    private String citationInfoIpc;

    @Schema(description = "引证国家")
    private String citationInfoCountry;

    @Schema(description = "引证文献位置")
    private List<String> citationInfoLocation;

    @Schema(description = "详细内容")
    private List<CitationInfoRichContentIce> citationInfoRichContent;
}
