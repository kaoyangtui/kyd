package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研平台上下架请求")
public class ResearchPlatformShelfRequest {

    @Schema(description = "主键ID列表")
    private List<Long> ids;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;
}
