package com.pig4cloud.pigx.admin.dto.patentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 专利评估结果 DTO
 */
@Data
@Schema(description = "申请前评估相应")
public class PatentEvaluationMqResponse {

    @Schema(description = "系统业务 ID")
    private String sysBizId;

    @Schema(description = "评估等级：A/B/C/D")
    private String level;

    @Schema(description = "评估报告日，yyyy-MM-dd")
    private LocalDate reportDate;

    @Schema(description = "可专利性（星级1-5）")
    private String viability;

    @Schema(description = "技术竞争（星级1-5）")
    private String tech;

    @Schema(description = "市场前景（星级1-5）")
    private String market;

    @Schema(description = "报告地址URL")
    private String reportUrl;


}
