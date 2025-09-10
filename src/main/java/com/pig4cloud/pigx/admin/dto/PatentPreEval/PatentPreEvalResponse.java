package com.pig4cloud.pigx.admin.dto.PatentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "PatentPreEvalResponse", description = "专利申请前评估-响应")
@Data
public class PatentPreEvalResponse implements Serializable {

    public static final String BIZ_CODE = "PATENT_PRE_EVAL";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "业务编码")
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

    @Schema(description = "结果状态：0处理中 1成功 2失败")
    private Integer status;

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

    @Schema(description = "创建人")
    private Long createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private Long updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}