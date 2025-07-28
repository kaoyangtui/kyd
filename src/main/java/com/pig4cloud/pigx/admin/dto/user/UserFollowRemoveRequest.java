package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFollowRemoveRequest {
    @Schema(description = "关注类型")
    private String followType;
    @Schema(description = "关注对象主键ID")
    private Long followId;
}
