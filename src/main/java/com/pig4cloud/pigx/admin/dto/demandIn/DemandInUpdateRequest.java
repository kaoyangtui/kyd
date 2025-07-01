package com.pig4cloud.pigx.admin.dto.demandIn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改请求
 */
@Data
@Schema(description = "修改校内需求")
public class DemandInUpdateRequest extends DemandInCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;
}