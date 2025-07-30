package com.pig4cloud.pigx.admin.dto.ipTransform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 更新请求
 */
@Data
@Schema(description = "更新知识产权转化申请")
public class IpTransformUpdateRequest extends IpTransformCreateRequest {

    @Schema(description = "主键 ID")
    private Long id;

}