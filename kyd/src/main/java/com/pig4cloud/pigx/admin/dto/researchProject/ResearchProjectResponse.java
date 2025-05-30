package com.pig4cloud.pigx.admin.dto.researchProject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研项目响应")
public class ResearchProjectResponse {

    public static final String BIZ_CODE = "RESEARCH_PROJECT";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "关联提案数")
    private Integer proposalCount;

    @Schema(description = "关联成果数")
    private Integer resultCount;

    @Schema(description = "创建/提交时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建/提交人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "所属院系")
    private String deptId;
}
