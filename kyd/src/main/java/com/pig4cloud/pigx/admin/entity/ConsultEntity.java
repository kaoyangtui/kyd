package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 咨询信息表
 *
 * @author pigx
 * @date 2025-05-20 17:42:00
 */
@Data
@TenantTable
@TableName("t_consult")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "咨询信息表")
public class ConsultEntity extends Model<ConsultEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 咨询类型
     */
    @Schema(description = "咨询类型")
    private String type;

    /**
     * 咨询目标编码
     */
    @Schema(description = "咨询目标编码")
    private String targetCode;

    /**
     * 咨询目标名称
     */
    @Schema(description = "咨询目标名称")
    private String targetName;

    /**
     * 咨询内容
     */
    @Schema(description = "咨询内容")
    private String content;

    /**
     * 门户用户 ID
     */
    @Schema(description = "门户用户 ID")
    private Long userId;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    private String contactName;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactPhone;

    /**
     * 咨询状态（0未读 1已读）
     */
    @Schema(description = "咨询状态（0未读 1已读）")
    private Integer status;

    /**
     * 咨询回复人
     */
    @Schema(description = "咨询回复人")
    private String replyBy;

    /**
     * 咨询回复时间
     */
    @Schema(description = "咨询回复时间")
    private LocalDateTime replyTime;

    /**
     * 咨询回复内容
     */
    @Schema(description = "咨询回复内容")
    private String replyContent;

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