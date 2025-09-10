package com.pig4cloud.pigx.admin.dto.PatentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(name = "PatentPreEvalMainVO", description = "专利申请前评估-内部VO")
@Data
public class PatentPreEvalMainVO implements Serializable {

    private Long id;
    private String code;
    private String title;
    private String appDate;
    private String applicant;
    private String applyDateRangeType;
    private String abstractText;
    private String claimText;
    private String descriptionText;
    private Integer status;
    private String level;
    private String reportDate;
    private String viability;
    private String tech;
    private String market;
    private String reportUrl;
    private Long tenantId;
    private Long deptId;
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
}