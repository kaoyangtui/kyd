package com.pig4cloud.pigx.admin.dto.icLayout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "集成电路布图登记修改请求")
public class IcLayoutUpdateRequest extends IcLayoutCreateRequest {

    @Schema(description = "主键")
    private Long id;
}