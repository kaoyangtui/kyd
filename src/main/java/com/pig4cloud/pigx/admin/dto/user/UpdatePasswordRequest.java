package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    @Schema(description = "原密码")
    private String oldPassword;

    @Schema(description = "新密码")
    private String newPassword;

    @Schema(description = "确认新密码")
    private String confirmPassword;
}
