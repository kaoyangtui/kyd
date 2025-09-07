package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "业绩点规则-响应")
public class PerfRuleResponse {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "方案ID")
    private Long schemeId;
    @Schema(description = "序号")
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
    @Schema(description = "创建人")
    private Long createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新人")
    private Long updateBy;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}