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
 * 成果需求信息表
 *
 * @author pigx
 * @date 2025-05-20 11:19:30
 */
@Data
@TenantTable
@TableName("t_demand_in")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "成果需求信息表")
public class DemandInEntity extends Model<DemandInEntity> {


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
     * 流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止
     */
    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    /**
     * 需求名称
     */
    @Schema(description = "需求名称")
    private String name;

    /**
     * 需求类型
     */
    @Schema(description = "需求类型")
    private String type;

    /**
     * 上下架状态，0下架1上架
     */
    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;

    /**
     * 上下架时间
     */
    @Schema(description = "上下架时间")
    private LocalDateTime shelfTime;

    /**
     * 所属领域
     */
    @Schema(description = "所属领域")
    private String field;

    /**
     * 需求有效期
     */
    @Schema(description = "需求有效期")
    private LocalDate validUntil;

    /**
     * 需求描述
     */
    @Schema(description = "需求描述")
    private String description;

    /**
     * 需求标签，多个以;分隔
     */
    @Schema(description = "需求标签，多个以;分隔")
    private String tags;

    /**
     * 需求附件URL
     */
    @Schema(description = "需求附件URL")
    private String attachFileUrl;

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

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人ID")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人姓名")
    private String createUserName;

    /**
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long viewCount;

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