package com.pig4cloud.pigx.admin.dto.researchProject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研项目修改请求")
public class ResearchProjectUpdateRequest extends ResearchProjectCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}
