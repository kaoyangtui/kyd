package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "业绩点规则行项目")
public class PerfRuleItemRequest {
    @Schema(description = "规则ID，更新时可带")
    private Long id;
    @Schema(description = "显示序号，可空，后端自动填充")
    private Integer ordNo;
    @Schema(description = "知识产权类型编码")
    private String ipTypeCode;
    @Schema(description = "知识产权类型名称")
    private String ipTypeName;
    @Schema(description = "规则事件编码")
    private String ruleEventCode;
    @Schema(description = "规则事件名称")
    private String ruleEventName;
    @Schema(description = "分值")
    private BigDecimal score;
    @Schema(description = "状态 0停用 1启用")
    private Integer status;
    @Schema(description = "附加条件JSON")
    private String extraCondJson;
    @Schema(description = "备注")
    private String remark;
}