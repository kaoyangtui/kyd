package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "方案总览-单格数据（IP类型×事件）")
public class PerfSchemeCellResponse {
    @Schema(description = "知识产权类型编码")
    private String ipTypeCode;
    @Schema(description = "知识产权类型名称")
    private String ipTypeName;

    @Schema(description = "规则事件编码")
    private String ruleEventCode;
    @Schema(description = "规则事件名称")
    private String ruleEventName;

    @Schema(description = "分数总和")
    private BigDecimal scoreSum;
    @Schema(description = "件数")
    private Integer eventCount;
}