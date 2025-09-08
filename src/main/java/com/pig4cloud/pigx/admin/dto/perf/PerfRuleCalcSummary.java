package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "业绩计算汇总")
public class PerfRuleCalcSummary {

    @Schema(description = "方案ID")
    private Long schemeId;

    @Schema(description = "统计期开始")
    private LocalDate periodStart;

    @Schema(description = "统计期结束")
    private LocalDate periodEnd;

    @Schema(description = "处理事件条数")
    private Integer eventTotal;

    @Schema(description = "写入聚合行数")
    private Integer resultRows;

    @Schema(description = "累计得分")
    private BigDecimal totalScore;
}
