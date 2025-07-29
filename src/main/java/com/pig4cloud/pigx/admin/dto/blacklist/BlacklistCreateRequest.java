package com.pig4cloud.pigx.admin.dto.blacklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增黑名单请求")
public class BlacklistCreateRequest {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description="用户名")
    private String userName;
}
