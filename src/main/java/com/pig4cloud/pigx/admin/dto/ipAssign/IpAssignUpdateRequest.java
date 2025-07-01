package com.pig4cloud.pigx.admin.dto.ipAssign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 更新赋权请求
 */
@Data
@Schema(description = "更新赋权请求")
public class IpAssignUpdateRequest extends IpAssignCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}