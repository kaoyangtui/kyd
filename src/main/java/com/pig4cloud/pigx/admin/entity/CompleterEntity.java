package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 完成人
 *
 * @author pigx
 * @date 2025-05-23 14:31:27
 */
@Data
@TenantTable
@TableName("t_completer")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "完成人")
public class CompleterEntity extends Model<CompleterEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 主表code
     */
    @Schema(description = "主表code")
    private String code;

    /**
     * 学工号
     */
    @Schema(description = "学工号")
    private String completerNo;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String completerName;

    /**
     * 人员类型
     */
    @Schema(description = "人员类型")
    private String completerType;

    /**
     * 院系ID
     */
    @Schema(description = "院系ID")
    private Long completerDeptId;

    /**
     * 院系名称
     */
    @Schema(description = "院系名称")
    private String completerDeptName;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String completerPhone;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱")
    private String completerEmail;

    /**
     * 是否负责人 0否1是
     */
    @Schema(description = "是否负责人 0否1是")
    private Integer completerLeader;

    /**
     * 职称
     */
    @Schema(description = "职称")
    private Integer positionTitle;

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