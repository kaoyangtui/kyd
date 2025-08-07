package com.pig4cloud.pigx.admin.dto.patentEvaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "申请前评估表单")
public class PatentEvaluationCommitRequest {

    /**
     * 系统名称
     */
    @Schema(description = "系统名称")
    private String sysFlag;

    /**
     * 系统业务 ID
     */
    @Schema(description = "系统业务 ID")
    private String sysBizId;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 申请人
     */
    @Schema(description = "申请人")
    private String applicant;

    /**
     * 申请日期
     */
    @Schema(description = "申请日期")
    private String appDate;

    /**
     * 摘要
     */
    @Schema(description = "摘要")
    private String abstractText;

    /**
     * 权利要求(每一个句号结束)
     */
    @Schema(description = "权利要求(每一个句号结束)")
    private String claimText;

    /**
     * 说明书
     */
    @Schema(description = "说明书")
    private String descriptionText;

}
