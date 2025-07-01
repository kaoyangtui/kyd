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

    @Schema(description="省code")
    private String provinceCode;

    @Schema(description="省name")
    private String provinceName;

    @Schema(description="市code")
    private String cityCode;

    @Schema(description="市name")
    private String cityName;

    @Schema(description="区code")
    private String districtCode;

    @Schema(description="区name")
    private String districtName;

    @Schema(description = "详细地址")
    private String address;
}
