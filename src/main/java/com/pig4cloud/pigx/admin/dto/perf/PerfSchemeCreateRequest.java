package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "业绩点方案-新增请求")
public class PerfSchemeCreateRequest {
    @Schema(description = "业绩分计算名称")
    private String schemeName;
    @Schema(description = "计算周期-开始")
    private LocalDate periodStart;
    @Schema(description = "计算周期-结束")
    private LocalDate periodEnd;
    @Schema(description = "状态 0停用 1启用")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
}