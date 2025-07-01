package com.pig4cloud.pigx.admin.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "修改科研成果请求")
public class ResultUpdateRequest extends ResultCreateRequest {
    @Schema(description = "主键")
    private Long id;
}
