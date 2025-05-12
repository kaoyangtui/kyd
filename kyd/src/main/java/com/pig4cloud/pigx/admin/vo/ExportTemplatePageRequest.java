package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "导出模板分页查询请求")
public class ExportTemplatePageRequest {
    @Schema(description = "业务编码（如 result_list）")
    private String bizCode;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "页码")
    private long current = 1;

    @Schema(description = "每页数量")
    private long size = 10;
}