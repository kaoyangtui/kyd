package com.pig4cloud.pigx.admin.dto.exportExecute;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改导出模板请求")
public class ExportTemplateUpdateRequest extends ExportTemplateCreateRequest {
    @Schema(description = "主键 ID")
    private Long id;
}