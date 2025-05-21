package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResearchPlatformUpdateRequest extends ResearchPlatformCreateRequest {

    @Schema(description = "主键")
    private Long id;
}