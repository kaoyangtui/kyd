package com.pig4cloud.pigx.admin.dto.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门户统计数量响应")
public class PortalStatisticResponse {

    @Schema(description = "专利数量")
    private Long patentCount;

    @Schema(description = "成果数量")
    private Long resultCount;

    @Schema(description = "需求数量")
    private Long demandCount;

    @Schema(description = "专家数量")
    private Long expertCount;
}
