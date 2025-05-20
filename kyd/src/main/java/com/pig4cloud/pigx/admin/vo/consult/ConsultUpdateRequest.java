package com.pig4cloud.pigx.admin.vo.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "咨询更新请求")
public class ConsultUpdateRequest extends ConsultCreateRequest {

    @Schema(description = "主键")
    private Long id;
}