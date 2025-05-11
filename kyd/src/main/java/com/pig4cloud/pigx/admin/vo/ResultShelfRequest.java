package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研成果上下架请求")
public class ResultShelfRequest {

    @Schema(description = "成果ID")
    private Long id;

    @Schema(description = "目标上下架状态（0-下架 1-上架）")
    private Integer status;
}
