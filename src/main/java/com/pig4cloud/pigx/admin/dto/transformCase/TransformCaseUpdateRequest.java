package com.pig4cloud.pigx.admin.dto.transformCase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "转化案例修改请求")
public class TransformCaseUpdateRequest extends TransformCaseCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;

}
