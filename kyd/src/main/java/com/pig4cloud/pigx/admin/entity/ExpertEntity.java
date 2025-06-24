package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 专家信息表
 *
 * @author pigx
 * @date 2025-05-27 19:51:51
 */
@Data
@TenantTable
@TableName("t_expert")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专家信息表")
public class ExpertEntity extends Model<ExpertEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 专家姓名
     */
    @Schema(description = "专家姓名")
    private String name;

    /**
     * 专家照片URL
     */
    @Schema(description = "专家照片URL")
    private String photoUrl;

    /**
     * 专家职称
     */
    @Schema(description = "专家职称")
    private String title;

    /**
     * 所在单位
     */
    @Schema(description = "所在单位")
    private String orgName;

    /**
     * 职务
     */
    @Schema(description = "职务")
    private String position;

    /**
     * 办公室电话
     */
    @Schema(description = "办公室电话")
    private String officePhone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 个人简介
     */
    @Schema(description = "个人简介")
    private String intro;

    /**
     * 教育经历
     */
    @Schema(description = "教育经历")
    private String eduExp;

    /**
     * 工作经历
     */
    @Schema(description = "工作经历")
    private String workExp;

    /**
     * 所属组织ID
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "所属组织ID")
    private Long deptId;

    /**
     * 组织名称
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "组织名称")
    private String deptName;

    /**
     * 上下架状态，0下架1上架
     */
    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;

    /**
     * 上下架时间
     */
    @Schema(description = "上下架时间")
    private LocalDateTime shelfTime;

    /**
     * 专利数量
     */
    @Schema(description = "专利数量")
    private Long patentCnt;

    /**
     * 成果数量
     */
    @Schema(description = "成果数量")
    private Long resultCnt;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人ID")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人姓名")
    private String createUserName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标识")
    private String delFlag;

    /**
     * 租户
     */
    @Schema(description = "租户")
    private Long tenantId;
}