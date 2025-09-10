package com.pig4cloud.pigx.admin.dto.PatentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "专利申请前评估-修改基础信息请求")
@Data
public class PatentPreEvalUpdateRequest extends PatentPreEvalCreateRequest {

    @Schema(description = "主键ID")
    private Long id;

}