package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 植物新品种权校内培育人信息
 *
 * @author pigx
 * @date 2025-05-15 10:56:23
 */
@Data
@TenantTable
@TableName("t_plant_variety_breeder")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "植物新品种权校内培育人信息")
public class PlantVarietyBreederEntity extends Model<PlantVarietyBreederEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 关联品种ID
     */
    @Schema(description = "关联品种ID")
    private Long plantVarietyId;

    /**
     * 学工号
     */
    @Schema(description = "学工号")
    private String jobNo;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 人员类型 教师、学生
     */
    @Schema(description = "人员类型 教师、学生")
    private String type;

    /**
     * 院系部门
     */
    @Schema(description = "院系部门")
    private String deptName;

    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 是否负责人 0否1是
     */
    @Schema(description = "是否负责人 0否1是")
    private Integer isLeader;

    /**
     * 所属院系
     */
    @Schema(description = "所属院系")
    private String deptId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

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