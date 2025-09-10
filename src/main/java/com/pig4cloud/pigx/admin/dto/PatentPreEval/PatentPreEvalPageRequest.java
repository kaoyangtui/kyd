package com.pig4cloud.pigx.admin.dto.PatentPreEval;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Schema(description = "专利申请前评估-分页查询请求")
@Data
public class PatentPreEvalPageRequest extends BasePageQuery implements Serializable {

    @Schema(description = "关键字，按标题/申请人模糊")
    private String keyword;

    @Schema(description = "申请人")
    private String applicant;

    @Schema(description = "结果状态：0处理中 1成功 2失败")
    private Integer status;

    @Schema(description = "评估等级：A/B/C/D")
    private String level;

    @Schema(description = "拟申请日范围，开始")
    private LocalDate appDateRangeBegin;

    @Schema(description = "拟申请日范围，结束")
    private LocalDate appDateRangeEnd;

    @Schema(description = "评估报告日范围，开始")
    private LocalDate reportDateRangeBegin;

    @Schema(description = "评估报告日范围，结束")
    private LocalDate reportDateRangeEnd;
}