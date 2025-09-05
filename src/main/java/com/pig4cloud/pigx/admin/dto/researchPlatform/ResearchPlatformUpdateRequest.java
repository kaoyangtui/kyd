package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "科研平台更新请求")
public class ResearchPlatformUpdateRequest extends ResearchPlatformCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;
}
