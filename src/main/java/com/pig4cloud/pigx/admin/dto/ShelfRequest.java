package com.pig4cloud.pigx.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用上下架请求结构
 */
@Data
@Schema(description = "上下架请求")
public class ShelfRequest {

    @Schema(description = "主键 ID")
    private Long id;

    @Schema(description = "上下架状态（0下架，1上架）")
    private Integer shelfStatus;
}
