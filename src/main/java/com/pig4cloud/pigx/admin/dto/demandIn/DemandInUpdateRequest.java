package com.pig4cloud.pigx.admin.dto.demandIn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * &#x4FEE;&#x6539;&#x8BF7;&#x6C42;
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "修改校内需求")
public class DemandInUpdateRequest extends DemandInCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;
}