package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户个人信息修改请求")
public class UserProfileUpdateRequest {

    @Schema(description = "主键ID")
    private Long id;

    @NotBlank(message = "联系方式不能为空")
    @Schema(description = "联系方式")
    private String contactInfo;

    @NotBlank(message = "所在单位不能为空")
    @Schema(description = "所在单位")
    private String organization;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区")
    private String district;

    @Schema(description = "详细地址")
    private String address;
}
