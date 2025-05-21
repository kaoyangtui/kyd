package com.pig4cloud.pigx.admin.dto.plantVariety;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "植物新品种权导出请求")
public class PlantVarietyExportWrapperRequest extends ExportWrapperRequest<PlantVarietyPageRequest> {
}
