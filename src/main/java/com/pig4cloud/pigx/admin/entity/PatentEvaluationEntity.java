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
 * 专利评估结果表
 *
 * @author pigx
 * @date 2025-08-07 09:35:50
 */
@Data
@TenantTable
@TableName("t_patent_evaluation")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利评估结果表")
public class PatentEvaluationEntity extends Model<PatentEvaluationEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 专利提案 ID
	*/
    @Schema(description="专利提案 ID")
    private Long patentProposalId;

	/**
	* 状态，0 处理中 1 处理完成
	*/
    @Schema(description="状态，0 处理中 1 处理完成")
    private Integer status;

	/**
	* 报告出具时间
	*/
    @Schema(description="报告出具时间")
    private LocalDate reportTime;

	/**
	* 整体评价，ABCD 级
	*/
    @Schema(description="整体评价，ABCD 级")
    private String level;

	/**
	* 可专利性评价
	*/
    @Schema(description="可专利性评价")
    private String viability;

	/**
	* 技术竞争
	*/
    @Schema(description="技术竞争")
    private String tech;

	/**
	* 市场前景
	*/
    @Schema(description="市场前景")
    private String market;

	/**
	* 报告文件地址
	*/
    @Schema(description="报告文件地址")
    private String reportFileUrl;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

	/**
	* 所属院系
	*/
    @Schema(description="所属院系")
    private String deptId;

	/**
	* 创建/提交人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交人")
    private String createBy;

	/**
	* 创建/提交时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交时间")
    private LocalDateTime createTime;

	/**
	* 更新人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新人")
    private String updateBy;

	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;

	/**
	* 删除标识
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标识")
    private String delFlag;

	/**
	* 租户
	*/
    @Schema(description="租户")
    private Long tenantId;
}