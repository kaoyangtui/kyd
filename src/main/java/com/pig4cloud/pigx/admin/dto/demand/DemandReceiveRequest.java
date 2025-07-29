package com.pig4cloud.pigx.admin.dto.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "企业需求推送请求")
public class DemandReceiveRequest {

    @Schema(description = "ID")
    private Long demandId;
    @Schema(description = "用户 ID")
    private Long userId;
}
