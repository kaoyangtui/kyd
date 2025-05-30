package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 用户导出字段模板配置表
 *
 * @author pigx
 * @date 2025-05-12 09:07:01
 */
@Data
@TenantTable
@TableName("t_export_template")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户导出字段模板配置表")
public class ExportTemplateEntity extends Model<ExportTemplateEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

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
     * 业务编码（如 patent_list / result_list）
     */
    @Schema(description = "业务编码（如 patent_list / result_list）")
    private String bizCode;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    private String templateName;

    /**
     * 所属用户（为空代表全局）
     */
    @Schema(description = "所属用户（为空代表全局）")
    private Long userId;

    /**
     * 是否默认模板：0 否，1 是
     */
    @Schema(description = "是否默认模板：0 否，1 是")
    private Integer isDefault;

    /**
     * 导出字段的 key 列表，多个以 ; 分隔
     */
    @Schema(description = "导出字段的 key 列表，多个以 ; 分隔")
    private String fieldKeys;

    /**
     * 创建/提交人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建/提交人")
    private String createBy;

    /**
     * 创建/提交时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建/提交时间")
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
     * 删除标识 0-未删除 1-已删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标识 0-未删除 1-已删除")
    private String delFlag;

    /**
     * 租户
     */
    @Schema(description = "租户")
    private Long tenantId;
}