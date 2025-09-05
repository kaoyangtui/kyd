package com.pig4cloud.pigx.admin.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专家修改请求")
public class ExpertUpdateRequest extends ExpertCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}
