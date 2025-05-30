package com.pig4cloud.pigx.admin.dto.researchProject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "项目类型下拉模糊搜索请求")
public class ProjectTypeSearchRequest {

    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "分页-当前页，不传为全部")
    private Integer pageNum = 1;

    @Schema(description = "分页-每页数量，不传为全部")
    private Integer pageSize = 10;
}
