package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供需匹配结果表
 *
 * @author pigx
 * @date 2025-08-02 10:23:23
 */
@Data
@TableName("t_supply_demand_match_result")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "供需匹配结果表")
public class SupplyDemandMatchResultEntity extends Model<SupplyDemandMatchResultEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

    /**
     * 编码
     */
    @Schema(description="编码")
    private String code;

    /**
     * 匹配日期
     */
    @Schema(description="匹配日期")
    private LocalDate matchDate;

    /**
     * 需求ID
     */
    @Schema(description="需求ID")
    private Long demandId;

    /**
     * 需求类型
     */
    @Schema(description="需求类型")
    private String demandType;

    /**
     * 需求内容（可为JSON/文本）
     */
    @Schema(description="需求内容（可为JSON/文本）")
    private String demandContent;

    /**
     * 供给ID
     */
    @Schema(description="供给ID")
    private Long supplyId;

    /**
     * 供给类型
     */
    @Schema(description="供给类型")
    private String supplyType;

    /**
     * 供给内容（可为JSON/文本）
     */
    @Schema(description="供给内容（可为JSON/文本）")
    private String supplyContent;

    /**
     * 匹配度分数 0-100
     */
    @Schema(description="匹配度分数 0-100")
    private Integer matchScore;

    /**
     * 匹配结果说明
     */
    @Schema(description="匹配结果说明")
    private String matchResult;

    /**
     * 匹配过程说明
     */
    @Schema(description="匹配过程说明")
    private String matchReasoning;

    /**
     * 供给信息简要总结
     */
    @Schema(description="供给信息简要总结")
    private String supplySummary;

    /**
     * 需求信息简要总结
     */
    @Schema(description="需求信息简要总结")
    private String demandSummary;

    /**
     * 相关关键词，分号分隔
     */
    @Schema(description="相关关键词，分号分隔")
    private String relatedKeywords;

    /**
     * 建议/补充说明
     */
    @Schema(description="建议/补充说明")
    private String advice;

    /**
     * 匹配状态（0未处理，1已确认，2已拒绝等）
     */
    @Schema(description="匹配状态（0未处理，1已确认，2已拒绝等）")
    private String matchStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;
}