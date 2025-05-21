package com.pig4cloud.pigx.admin.dto.plantVariety;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "植物新品种权修改请求")
public class PlantVarietyUpdateRequest {

    @Schema(description = "主表信息")
    private PlantVarietyVO main;

    @Schema(description = "权利人列表")
    private List<PlantVarietyOwnerVO> owners;

    @Schema(description = "校内培育人列表")
    private List<PlantVarietyBreederVO> breeders;
}