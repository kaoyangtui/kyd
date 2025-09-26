package com.pig4cloud.pigx.admin.dto.patentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Schema(description = "专利申请前评估-提交请求")
@Data
public class PatentPreEvalCreateRequest implements Serializable {

    @Schema(description = "编码")
    private String code;

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