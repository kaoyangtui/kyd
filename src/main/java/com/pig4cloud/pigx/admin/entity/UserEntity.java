package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 客户表
 *
 * @author pigx
 * @date 2025-06-16 18:58:54
 */
@Data
@TenantTable
@TableName("t_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "客户表")
public class UserEntity extends Model<UserEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 密码
     */
    @Schema(description="密码")
    private String password;

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
    private String deptId;

    /**
     * 创建/提交人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交人")
    private String createBy;

    /**
     * 创建/提交时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标识")
    private String delFlag;

    /**
     * 租户
     */
    @Schema(description="租户")
    private Long tenantId;
}