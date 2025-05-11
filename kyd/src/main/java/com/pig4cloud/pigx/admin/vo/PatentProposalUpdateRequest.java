package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改专利提案请求")
public class PatentProposalUpdateRequest extends PatentProposalCreateRequest {
    @Schema(description = "主键")
    private Long id;
}