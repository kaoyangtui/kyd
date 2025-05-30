package com.pig4cloud.pigx.admin.dto.researchProject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "项目名称下拉模糊搜索请求")
public class ProjectNameSearchRequest {
    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "分页-当前页")
    private Integer pageNum = 1;

    @Schema(description = "分页-每页数量")
    private Integer pageSize = 10;
}
