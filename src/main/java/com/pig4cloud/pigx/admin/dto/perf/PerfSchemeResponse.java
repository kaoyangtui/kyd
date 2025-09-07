package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "业绩点方案-响应")
public class PerfSchemeResponse {
    public static final String BIZ_CODE = "PERF_SCHEME";
    @Schema(description = "主键ID")
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
    @Schema(description = "创建人")
    private Long createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新人")
    private Long updateBy;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}