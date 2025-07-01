package com.pig4cloud.pigx.admin.dto.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求修改请求")
@Data
public class DemandUpdateRequest extends DemandCreateRequest {

    @Schema(description = "主键")
    private Long id;
}