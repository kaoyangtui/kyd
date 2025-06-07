package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 专利监控表
 *
 * @author pigx
 * @date 2025-05-31 10:47:14
 */
@Data
@TenantTable
@TableName("t_patent_monitor")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利监控表")
public class PatentMonitorEntity extends Model<PatentMonitorEntity> {


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 事件日期
     */
    @Schema(description = "事件日期")
    private LocalDate eventTime;

    /**
     * 事件内容
     */
    @Schema(description = "事件内容")
    private String eventContent;

    /**
     * 专利唯一ID
     */
    @Schema(description = "专利唯一ID")
    private String pid;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String title;

    /**
     * 专利类型
     */
    @Schema(description = "专利类型")
    private String patType;

    /**
     * 专利权状态
     */
    @Schema(description = "专利权状态")
    private String lprs;

    /**
     * 专利权状态代码
     */
    @Schema(description = "专利权状态代码")
    private String statusCode;

    /**
     * 申请号 (数组)
     */
    @Schema(description = "申请号 (数组)")
    private String appNumber;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    /**
     * createTime
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private String updateBy;

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