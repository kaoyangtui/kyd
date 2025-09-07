package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "业绩点方案含规则-详情响应")
public class PerfSchemeWithRulesResponse extends PerfSchemeResponse {
    @Schema(description = "规则行项目")
    private List<PerfRuleResponse> rules;
}