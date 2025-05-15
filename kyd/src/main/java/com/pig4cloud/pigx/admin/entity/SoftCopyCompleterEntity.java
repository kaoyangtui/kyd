package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 软著提案—完成人
 *
 * @author pigx
 * @date 2025-05-13 08:17:51
 */
@Data
@TenantTable
@TableName("t_soft_copy_completer")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "软著提案—完成人")
public class SoftCopyCompleterEntity extends Model<SoftCopyCompleterEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 软著提案ID
     */
    @Schema(description = "软著提案ID")
    private Long softCopyId;

    /**
     * 序号
     */
    @Schema(description = "序号")
    private Integer seq;

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
     * 院系
     */
    @Schema(description = "院系")
    private String completerDept;

    /**
     * 电话
     */
    @Schema(description = "电话")
    private String completerPhone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String completerEmail;

    /**
     * 是否负责人 0否1是
     */
    @Schema(description = "是否负责人 0否1是")
    private Integer completerLeader;

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