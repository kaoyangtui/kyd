package com.pig4cloud.pigx.admin.dto.exportExecute;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "导出字段列表响应")
public class ExportFieldResponse {
    @Schema(description = "字段 Key")
    private String key;

    @Schema(description = "字段标题")
    private String title;
}