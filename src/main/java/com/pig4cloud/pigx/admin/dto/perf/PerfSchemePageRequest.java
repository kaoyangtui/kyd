package com.pig4cloud.pigx.admin.dto.perf;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "业绩点方案-分页请求")
public class PerfSchemePageRequest extends BasePageQuery {
    @Schema(description = "关键字(名称模糊)")
    private String keyword;
    @Schema(description = "状态 0停用 1启用")
    private Integer status;
    @Schema(description = "开始时间")
    private LocalDate startDate;
    @Schema(description = "结束时间")
    private LocalDate endDate;
}