package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "业绩计算请求")
public class PerfRuleCalcRequest {

    @Schema(description = "方案ID")
    private Long schemeId;

    @Schema(description = "统计期开始，空则用方案周期开始")
    private LocalDate periodStart;

    @Schema(description = "统计期结束，空则用方案周期结束")
    private LocalDate periodEnd;

    @Schema(description = "是否试跑，true则不落库")
    private Boolean dryRun;
}
