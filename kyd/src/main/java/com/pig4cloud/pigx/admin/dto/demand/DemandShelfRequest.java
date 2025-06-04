package com.pig4cloud.pigx.admin.dto.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业需求上下架请求
 */
@Data
@Schema(description = "企业需求上下架请求")
public class DemandShelfRequest {

    @Schema(description = "需求ID")
    private Long id;

    @Schema(description = "上下架状态（0-下架 1-上架）")
    private Integer shelfStatus;
}