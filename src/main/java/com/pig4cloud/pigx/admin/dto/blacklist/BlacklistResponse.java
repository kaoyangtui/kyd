package com.pig4cloud.pigx.admin.dto.blacklist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "黑名单返回信息")
public class BlacklistResponse {

    public static final String BIZ_CODE = "BLACKLIST";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "删除标识")
    private String delFlag;

    @Schema(description = "租户")
    private Long tenantId;
}
