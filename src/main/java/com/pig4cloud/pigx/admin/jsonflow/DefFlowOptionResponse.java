package com.pig4cloud.pigx.admin.jsonflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "流程定义下拉选项")
public class DefFlowOptionResponse {

    @Schema(description = "流程业务KEY")
    private String flowKey;

    @Schema(description = "流程名称")
    private String flowName;
}
