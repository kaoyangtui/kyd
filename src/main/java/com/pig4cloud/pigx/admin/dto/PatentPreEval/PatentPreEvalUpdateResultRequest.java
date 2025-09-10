package com.pig4cloud.pigx.admin.dto.PatentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Schema(description = "专利申请前评估-修改评估结果请求")
@Data
public class PatentPreEvalUpdateResultRequest implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

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
}