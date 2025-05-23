package com.pig4cloud.pigx.admin.dto.standard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "标准信息更新请求")
public class StandardUpdateRequest extends StandardCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;
}