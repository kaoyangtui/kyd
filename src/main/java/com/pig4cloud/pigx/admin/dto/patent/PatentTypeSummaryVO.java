package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PatentTypeSummaryVO {

    @Schema(description = "专利类型")
    private String patType;

    @Schema(description = "专利类型名称")
    private String patTypeName;

    @Schema(description = "专利数量")
    private Integer totalAmount;
}
