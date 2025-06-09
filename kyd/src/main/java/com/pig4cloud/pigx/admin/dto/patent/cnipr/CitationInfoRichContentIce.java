package com.pig4cloud.pigx.admin.dto.patent.cnipr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "引证信息类的详细 DTO")
public class CitationInfoRichContentIce {

    @Schema(description = "引证类型")
    private String referenceDocCategory;

    @Schema(description = "检索日期")
    private String searchDate;

    @Schema(description = "相关权利要求")
    private String relevantClaims;
}
