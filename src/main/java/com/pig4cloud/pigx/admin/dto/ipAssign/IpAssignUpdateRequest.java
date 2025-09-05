package com.pig4cloud.pigx.admin.dto.ipAssign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新赋权请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "更新赋权请求")
public class IpAssignUpdateRequest extends IpAssignCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}