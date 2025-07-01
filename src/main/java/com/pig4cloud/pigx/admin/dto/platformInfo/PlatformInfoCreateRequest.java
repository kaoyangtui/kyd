package com.pig4cloud.pigx.admin.dto.platformInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "关于平台内容新增请求")
public class PlatformInfoCreateRequest {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;
}
