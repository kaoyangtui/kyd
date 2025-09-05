package com.pig4cloud.pigx.admin.dto.blacklist;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "黑名单分页查询请求")
public class BlacklistPageRequest extends BasePageQuery {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description="用户名")
    private String userName;

}
