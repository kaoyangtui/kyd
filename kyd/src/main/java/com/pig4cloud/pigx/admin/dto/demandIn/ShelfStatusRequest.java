package com.pig4cloud.pigx.admin.dto.demandIn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "上下架状态修改请求")
public class ShelfStatusRequest {
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "上下架状态（0 下架，1 上架）")
    private Integer shelfStatus;
}
