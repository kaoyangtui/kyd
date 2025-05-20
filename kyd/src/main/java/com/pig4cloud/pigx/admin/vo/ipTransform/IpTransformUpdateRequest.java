package com.pig4cloud.pigx.admin.vo.ipTransform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 更新请求
 */
@Data
@Schema(description = "更新知识产权转化申请")
public class IpTransformUpdateRequest extends IpTransformCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;
}