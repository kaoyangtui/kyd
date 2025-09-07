package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "业绩点方案-更新请求")
public class PerfSchemeUpdateRequest extends PerfSchemeCreateRequest {
    @Schema(description = "主键ID")
    private Long id;
}