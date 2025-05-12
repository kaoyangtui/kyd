package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增导出模板请求")
public class ExportTemplateCreateRequest {
    @Schema(description = "业务编码（如 result_list）")
    private String bizCode;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "导出字段 key 列表，多个 ; 分隔")
    private String fieldKeys;

    @Schema(description = "是否默认，0 否 1 是")
    private Integer isDefault;
}