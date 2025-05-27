package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "上下架状态变更请求")
public class ResearchPlatformShelfRequest {
    @Schema(description = "主键ID", required = true)
    private Long id;
    
    @Schema(description = "目标状态（0下架 1上架）", required = true)
    private Integer shelfStatus;
}