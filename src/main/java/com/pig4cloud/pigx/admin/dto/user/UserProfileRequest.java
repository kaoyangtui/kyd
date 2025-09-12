package com.pig4cloud.pigx.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UserProfileRequest  {


    /**
     * 主键
     */
    @Schema(description="主键")
    private Long id;

    /**
     * 头像
     */
    @Schema(description="头像")
    private String avatarUrl;

    /**
     * 昵称
     */
    @Schema(description="昵称")
    private String nickname;

    /**
     * 手机号
     */
    @Schema(description="手机号")
    private String mobile;

    /**
     * 用户名
     */
    @Schema(description="用户名")
    private String username;

    /**
     * 联系方式
     */
    @Schema(description="联系方式")
    private String contactInfo;

    /**
     * 所在单位
     */
    @Schema(description="所在单位")
    private String organization;

    /**
     * 省code
     */
    @Schema(description="省code")
    private String provinceCode;

    /**
     * 省name
     */
    @Schema(description="省name")
    private String provinceName;

    /**
     * 市code
     */
    @Schema(description="市code")
    private String cityCode;

    /**
     * 市name
     */
    @Schema(description="市name")
    private String cityName;

    /**
     * 区code
     */
    @Schema(description="区code")
    private String districtCode;

    /**
     * 区name
     */
    @Schema(description="区name")
    private String districtName;

    /**
     * 详细地址
     */
    @Schema(description="详细地址")
    private String address;

    /**
     * 所属院系
     */
    @Schema(description="所属院系")
    private Long deptId;

}