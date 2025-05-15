package com.pig4cloud.pigx.admin.vo.plantVariety;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "植物新品种权权利人请求")
public class PlantVarietyOwnerVO {

    private Long id;

    @Schema(description = "权利人名称")
    private String name;
}
