package com.pig4cloud.pigx.admin.dto.blacklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改黑名单请求")
public class BlacklistUpdateRequest extends BlacklistCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}
