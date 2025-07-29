package com.pig4cloud.pigx.admin.dto.researchProject;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研项目分页查询请求")
public class ResearchProjectPageRequest extends BasePageQuery {

    @Schema(description = "关键词（项目名称模糊匹配）")
    private String keyword;

    @Schema(description = "项目类型")
    private String projectType;
}
