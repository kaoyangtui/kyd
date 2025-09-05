package com.pig4cloud.pigx.admin.dto.plantVariety;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "植物新品种权登记修改请求")
public class PlantVarietyUpdateRequest extends PlantVarietyCreateRequest {

    @Schema(description = "主键")
    private Long id;
}
