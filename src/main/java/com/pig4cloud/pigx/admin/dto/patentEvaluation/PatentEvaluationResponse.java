package com.pig4cloud.pigx.admin.dto.patentEvaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 专利评估结果 DTO
 */
@Data
@Schema(description = "专利评估结果")
public class PatentEvaluationResponse {

    @Schema(description = "报告出具时间")
    private LocalDate reportTime;

    @Schema(description = "整体评价，ABCD 级")
    private String level;

    @Schema(description = "可专利性评价")
    private String viability;

    @Schema(description = "技术竞争")
    private String tech;

    @Schema(description = "市场前景")
    private String market;

}
