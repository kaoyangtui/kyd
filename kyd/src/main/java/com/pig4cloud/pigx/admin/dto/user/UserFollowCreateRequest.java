package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "关注创建请求")
public class UserFollowCreateRequest {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private Long userId;

    @NotBlank(message = "关注类型不能为空")
    @Schema(description = "关注类型，PATENT、RESULT、DEMAND、EXPERT")
    private String followType;
}
