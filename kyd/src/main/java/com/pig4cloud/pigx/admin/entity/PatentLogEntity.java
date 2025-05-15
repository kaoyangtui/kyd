package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 专利数据拉取日志表
 *
 * @author zl
 * @date 2025-04-15 17:10:58
 */
@Data
@TenantTable
@TableName("t_patent_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利数据拉取日志表")
public class PatentLogEntity extends Model<PatentLogEntity> {


    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    /**
     * 专利唯一ID
     */
    @Schema(description = "专利唯一ID")
    private String pid;

    /**
     * requestParam
     */
    @Schema(description = "requestParam")
    private String requestParam;

    /**
     * responseBody
     */
    @Schema(description = "responseBody")
    private String responseBody;

    /**
     * appDate
     */
    @Schema(description = "appDate")
    private String appDate;

    /**
     * status
     */
    @Schema(description = "status")
    private Integer status;

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