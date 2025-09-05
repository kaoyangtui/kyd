package com.pig4cloud.pigx.admin.dto.researchProject;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "科研项目响应")
public class ResearchProjectResponse extends BaseResponse {

    public static final String BIZ_CODE = "RESEARCH_PROJECT";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "状态，0停用 1启用")
    private Integer status;

    @Schema(description = "关联提案数")
    private Integer proposalCount;

    @Schema(description = "关联成果数")
    private Integer resultCount;

    @Schema(description = "关联报销专利数")
    private Integer patentFeeReimburseCount;

}
