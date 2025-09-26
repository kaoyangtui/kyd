package com.pig4cloud.pigx.admin.dto.patentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(name = "PatentPreEvalDetailRequest", description = "专利申请前评估-详情请求")
@Data
public class PatentPreEvalDetailRequest implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "业务编码，可选，与id二选一")
    private String code;
}