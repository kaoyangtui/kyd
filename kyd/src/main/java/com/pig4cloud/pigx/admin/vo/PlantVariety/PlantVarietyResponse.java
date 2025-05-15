package com.pig4cloud.pigx.admin.vo.PlantVariety;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "植物新品种权响应")
public class PlantVarietyResponse {

    @Schema(description = "主表信息")
    private PlantVarietyVO main;

    @Schema(description = "权利人列表")
    private List<PlantVarietyOwnerVO> owners;

    @Schema(description = "校内培育人列表")
    private List<PlantVarietyBreederVO> breeders;

    public static final String BIZ_CODE = "plant_variety_list";
}