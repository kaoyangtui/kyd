package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 专利扩展信息缓存表
 *
 * @author pigx
 * @date 2025-07-08 18:21:14
 */
@Data
@TenantTable
@TableName("t_patent_detail_cache")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利扩展信息缓存表")
public class PatentDetailCacheEntity extends Model<PatentDetailCacheEntity> {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 专利唯一ID
     */
    @Schema(description = "专利唯一ID")
    private String pid;

    /**
     * 状态，0初始化1完整缓存
     */
    @Schema(description = "状态，0初始化1完整缓存")
    private Integer status;

    /**
     * 摘要附图URL
     */
    @Schema(description = "摘要附图URL")
    private String draws;

    /**
     * 说明书附图URL
     */
    @Schema(description = "说明书附图URL")
    private String drawsPic;

    /**
     * 外观专利图
     */
    @Schema(description = "外观专利图")
    private String tifDistributePath;

    /**
     * PDF
     */
    @Schema(description = "PDF")
    private String pdf;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID")
    private Long createUserId;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名")
    private String createUserName;

    /**
     * createTime
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "createTime")
    private LocalDateTime createTime;

    /**
     * updateTime
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "updateTime")
    private LocalDateTime updateTime;

    /**
     * delFlag
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "delFlag")
    private String delFlag;

    /**
     * tenantId
     */
    @Schema(description = "tenantId")
    private Long tenantId;
}