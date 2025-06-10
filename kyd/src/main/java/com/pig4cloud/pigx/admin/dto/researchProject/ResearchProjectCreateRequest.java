package com.pig4cloud.pigx.admin.dto.researchProject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "科研项目新增请求")
public class ResearchProjectCreateRequest {

    @NotBlank(message = "项目名称不能为空")
    @Schema(description = "项目名称")
    private String projectName;

    @NotBlank(message = "项目类型不能为空")
    @Schema(description = "项目类型")
    private String projectType;
}
