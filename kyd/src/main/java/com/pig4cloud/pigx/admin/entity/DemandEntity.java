package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业需求信息表
 *
 * @author pigx
 * @date 2025-05-20 13:02:56
 */
@Data
@TenantTable
@TableName("t_demand")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求信息表")
public class DemandEntity extends Model<DemandEntity> {


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
     * 需求分类，1 企业需求 2专项需求
     */
    @Schema(description = "需求分类，1 企业需求 2专项需求")
    private Integer category;
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
     * 所属领域
     */
    @Schema(description = "所属领域")
    private String field;

    /**
     * 有效期开始时间
     */
    @Schema(description = "有效期开始时间")
    private LocalDate validStart;

    /**
     * 有效期结束时间
     */
    @Schema(description = "有效期结束时间")
    private LocalDate validEnd;

    /**
     * 预算金额（万元）
     */
    @Schema(description = "预算金额（万元）")
    private BigDecimal budget;

    /**
     * 需求摘要
     */
    @Schema(description = "需求摘要")
    private String description;

    /**
     * 需求标签，多个以;分隔
     */
    @Schema(description = "需求标签，多个以;分隔")
    private String tags;

    /**
     * 报名开始时间
     */
    @Schema(description = "报名开始时间")
    private LocalDateTime signUpStart;

    /**
     * 报名截止时间
     */
    @Schema(description = "报名截止时间")
    private LocalDateTime signUpEnd;

    /**
     * 企业名称
     */
    @Schema(description = "企业名称")
    private String companyName;

    /**
     * 企业类别
     */
    @Schema(description = "企业类别")
    private String companyType;

    /**
     * 企业所属地区
     */
    @Schema(description = "企业所属地区")
    private String companyArea;

    /**
     * 企业地址
     */
    @Schema(description = "企业地址")
    private String companyAddr;

    /**
     * 企业简介
     */
    @Schema(description = "企业简介")
    private String companyIntro;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    private String contactName;

    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    private String contactPhone;

    /**
     * 联系人职务
     */
    @Schema(description = "联系人职务")
    private String contactTitle;

    /**
     * 联系人邮箱
     */
    @Schema(description = "联系人邮箱")
    private String contactEmail;

    /**
     * 企业需求附件URL
     */
    @Schema(description = "企业需求附件URL")
    private String attachFileUrl;

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
     * 所属院系
     */
    @Schema(description = "所属院系")
    private String deptId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

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