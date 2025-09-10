package com.pig4cloud.pigx.admin.dto.PatentPreEval;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(name = "PatentPreEvalRemoveRequest", description = "专利申请前评估-删除请求")
@Data
public class PatentPreEvalRemoveRequest implements Serializable {

    @Schema(description = "待删除ID列表")
    private List<Long> ids;
}