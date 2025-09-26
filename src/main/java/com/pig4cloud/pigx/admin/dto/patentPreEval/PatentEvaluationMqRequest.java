package com.pig4cloud.pigx.admin.dto.patentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "申请前评估提交")
public class PatentEvaluationMqRequest {

    @Schema(description = "系统名称")
    private String sysFlag;

    @Schema(description = "系统业务 ID")
    private String sysBizId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "拟申请日，yyyy-MM-dd")
    private LocalDate appDate;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "申请日范围类型：ANY 不限；BEFORE_EXCLUSIVE 之前不含；BEFORE_INCLUSIVE 之前含")
    private String applyDateRangeType;

    @Schema(description = "摘要")
    private String abstractText;

    @Schema(description = "权利要求(每一个句号结束)")
    private String claimText;

    @Schema(description = "说明书")
    private String descriptionText;

}
