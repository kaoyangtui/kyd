package com.pig4cloud.pigx.admin.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "修改科研成果请求")
public class ResultUpdateRequest extends ResultCreateRequest {
    @Schema(description = "主键")
    private Long id;
}
