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
 * 标准信息
 *
 * @author pigx
 * @date 2025-05-23 20:51:24
 */
@Data
@TenantTable
@TableName("t_standard")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "标准信息")
public class StandardEntity extends Model<StandardEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 标准名称
     */
    @Schema(description = "标准名称")
    private String name;

    /**
     * 标准号
     */
    @Schema(description = "标准号")
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
     * 类型
     */
    @Schema(description = "类型")
    private String type;

    /**
     * 主管部门
     */
    @Schema(description = "主管部门")
    private String dept;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间")
    private LocalDate pubDate;

    /**
     * 实施时间
     */
    @Schema(description = "实施时间")
    private LocalDate implDate;

    /**
     * 校外起草人姓名，多个用;分隔
     */
    @Schema(description = "校外起草人姓名，多个用;分隔")
    private String drafterOutName;

    /**
     * 标准文本URL
     */
    @Schema(description = "标准文本URL")
    private String fileUrl;

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