package com.pig4cloud.pigx.admin.dto.researchProject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研项目新增请求")
public class ResearchProjectCreateRequest {

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "所属院系ID")
    private String deptId;
}
