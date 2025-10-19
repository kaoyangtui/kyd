package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 科研成果表
 *
 * @author pigx
 * @date 2025-05-23 12:54:14
 */
@Data
@TenantTable
@TableName("t_result")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研成果表")
public class ResultEntity extends Model<ResultEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 编码
     */
    @Schema(description = "编码")
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
     * 成果名称
     */
    @Schema(description = "成果名称")
    private String name;

    /**
     * 所属学科
     */
    @Schema(description = "所属学科")
    private String subject;

    /**
     * 领域技术
     */
    @Schema(description = "领域技术")
    private String techArea;

    /**
     * 研究方向
     */
    @Schema(description = "研究方向")
    private String direction;

    /**
     * 是否依托项目，0否1是
     */
    @Schema(description = "是否依托项目，0否1是")
    private Integer fromProj;

    /**
     * 项目ID
     */
    @Schema(description = "项目ID")
    private Long researchProjectId;

    /**
     * 成果简介
     */
    @Schema(description = "成果简介")
    private String intro;

    /**
     * 技术成熟度
     */
    @Schema(description = "技术成熟度")
    private String maturity;

    /**
     * 是否有实物，0否1是
     */
    @Schema(description = "是否有实物，0否1是")
    private Integer hasObj;

    /**
     * 合作方式
     */
    @Schema(description = "合作方式")
    private String transWay;

    /**
     * 拟交易价格
     */
    @Schema(description = "拟交易价格")
    private BigDecimal transPrice;

    /**
     * 评价或获奖情况
     */
    @Schema(description = "评价或获奖情况")
    private String award;

    /**
     * 成果图片URL
     */
    @Schema(description = "成果图片URL")
    private String imgUrl;

    /**
     * 附件URL，多个用;分隔
     */
    @Schema(description = "附件URL，多个用;分隔")
    private String fileUrl;

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
     * 标签
     */
    @Schema(description = "标签")
    private String tags;

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
     * 创建/提交人
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
     * 供需匹配的最高得分，用于排序查询
     */
    @Schema(description = "供需匹配的最高得分，用于排序查询")
    private Long maxMatchScore;

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