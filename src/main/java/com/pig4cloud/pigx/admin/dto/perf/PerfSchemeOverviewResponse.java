package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "方案总览（周期、总分、单格列表）")
public class PerfSchemeOverviewResponse {
    @Schema(description = "方案ID")
    private Long schemeId;
    @Schema(description = "方案名称")
    private String schemeName;

    @Schema(description = "统计期-开始")
    private LocalDate periodStart;
    @Schema(description = "统计期-结束")
    private LocalDate periodEnd;

    @Schema(description = "总业绩点")
    private BigDecimal totalScore;

    @Schema(description = "单元格数据")
    private List<PerfSchemeCellResponse> cells;
}