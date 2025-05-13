package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "软著导出请求包装")
public class SoftCopyExportWrapperRequest {

    @Schema(description = "查询条件")
    private SoftCopyPageRequest query;

    @Schema(description = "导出执行配置")
    private ExportExecuteRequest export;
}
