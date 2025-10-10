package com.pig4cloud.pigx.admin.dto.demand;

import com.pig4cloud.pigx.admin.dto.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "企业技术需求查询请求")
public class DemandQueryRequest {
    @Schema(description = "分页参数")
    private PageRequest pageRequest;

    @Schema(description = "筛选条件")
    private DemandPageRequest request;
}
