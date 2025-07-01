package com.pig4cloud.pigx.admin.dto.researchNews;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研动态修改请求")
public class ResearchNewsUpdateRequest extends ResearchNewsCreateRequest {
    @Schema(description = "主键ID")
    private Long id;
}
