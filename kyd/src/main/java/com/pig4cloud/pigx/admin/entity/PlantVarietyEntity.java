package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 植物新品种权登记信息
 *
 * @author pigx
 * @date 2025-05-23 19:54:09
 */
@Data
@TenantTable
@TableName("t_plant_variety")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "植物新品种权登记信息")
public class PlantVarietyEntity extends Model<PlantVarietyEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 业务编码
     */
    @Schema(description = "业务编码")
    private String code;

    /**
     * 流程实例 ID
     */
    @Schema(description = "流程实例 ID")
    private String flowInstId;

    /**
     * 流程KEY
     */
    @Schema(description = "流程KEY")
    private String flowKey;

    /**
     * 流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回
     */
    @Schema(description = "流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    /**
     * 品种名称
     */
    @Schema(description = "品种名称")
    private String name;

    /**
     * 品种权号
     */
    @Schema(description = "品种权号")
    private String rightNo;

    /**
     * 属或者种
     */
    @Schema(description = "属或者种")
    private String genusType;

    /**
     * 属或种名称
     */
    @Schema(description = "属或种名称")
    private String genusName;

    /**
     * 申请时间
     */
    @Schema(description = "申请时间")
    private LocalDate applyDate;

    /**
     * 授权时间
     */
    @Schema(description = "授权时间")
    private LocalDate authDate;

    /**
     * 校外培育人姓名，多个用;分隔
     */
    @Schema(description = "校外培育人姓名，多个用;分隔")
    private String breederOutName;

    /**
     * 植物新品种权证书URL
     */
    @Schema(description = "植物新品种权证书URL")
    private String certFileUrl;

    /**
     * 负责人编码
     */
    @Schema(description = "负责人编码")
    private String leaderCode;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名")
    private String leaderName;

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