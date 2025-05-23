package com.pig4cloud.pigx.admin.dto.patentProposal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "修改专利提案请求")
public class PatentProposalUpdateRequest extends PatentProposalCreateRequest {
    @Schema(description = "主键")
    private Long id;
}