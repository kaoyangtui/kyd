package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 专利数据拉取日志表
 *
 * @author zl
 * @date 2025-04-18 20:08:15
 */
@Data
@TenantTable
@TableName("t_patent_fetch_checkpoint")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利数据拉取日志表")
public class PatentFetchCheckpointEntity extends Model<PatentFetchCheckpointEntity> {


    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    /**
     * 任务标识，如 cnipr_all_patents
     */
    @Schema(description = "任务标识，如 cnipr_all_patents")
    private String taskKey;

    /**
     * 下次拉取的 offset
     */
    @Schema(description = "下次拉取的 offset")
    private Integer offset;

    /**
     * 总记录数（可选）
     */
    @Schema(description = "总记录数（可选）")
    private Long total;

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
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 删除标记，0未删除，1已删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标记，0未删除，1已删除")
    private String delFlag;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;
}