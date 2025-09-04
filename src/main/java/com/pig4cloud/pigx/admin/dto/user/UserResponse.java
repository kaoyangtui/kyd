package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "user 返回信息")
public class UserResponse {

    public static final String BIZ_CODE = "USER";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "头像")
    private String avatarUrl;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "联系方式")
    private String contactInfo;

    @Schema(description = "所在单位")
    private String organization;

    @Schema(description = "省code")
    private String provinceCode;

    @Schema(description = "省name")
    private String provinceName;

    @Schema(description = "市code")
    private String cityCode;

    @Schema(description = "市name")
    private String cityName;

    @Schema(description = "区code")
    private String districtCode;

    @Schema(description = "区name")
    private String districtName;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "创建/提交时间")
    private LocalDateTime createTime;

}
