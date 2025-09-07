package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "保存业绩点方案及规则请求")
public class PerfSchemeSaveRequest {
    @Schema(description = "方案ID，更新时必填")
    private Long id;
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
    @Schema(description = "规则行项目列表")
    private List<PerfRuleItemRequest> rules;
}