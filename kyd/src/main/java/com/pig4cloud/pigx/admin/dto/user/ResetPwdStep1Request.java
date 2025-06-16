package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "重置密码第一步请求")
public class ResetPwdStep1Request {
    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
