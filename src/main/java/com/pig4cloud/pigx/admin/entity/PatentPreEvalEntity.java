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
 * 专利申请前评估表
 *
 * @author pigx
 * @date 2025-09-08 17:17:30
 */
@Data
@TenantTable
@TableName("t_patent_pre_eval")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利申请前评估表")
public class PatentPreEvalEntity extends Model<PatentPreEvalEntity> {


	/**
	* 主键ID
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键ID")
    private Long id;

	/**
	* 业务编码，供外部关联
	*/
    @Schema(description="业务编码，供外部关联")
    private String code;

	/**
	* 标题
	*/
    @Schema(description="标题")
    private String title;

	/**
	* 拟申请日
	*/
    @Schema(description="拟申请日")
    private LocalDate appDate;

	/**
	* 申请人
	*/
    @Schema(description="申请人")
    private String applicant;

	/**
	* 申请日范围类型：ANY/BEFORE_EXCLUSIVE/BEFORE_INCLUSIVE
	*/
    @Schema(description="申请日范围类型：ANY/BEFORE_EXCLUSIVE/BEFORE_INCLUSIVE")
    private String applyDateRangeType;

	/**
	* 摘要
	*/
    @Schema(description="摘要")
    private String abstractText;

	/**
	* 权利要求（每一个句号结束）
	*/
    @Schema(description="权利要求（每一个句号结束）")
    private String claimText;

	/**
	* 说明书
	*/
    @Schema(description="说明书")
    private String descriptionText;

	/**
	* 结果状态：0处理中 1成功 2失败
	*/
    @Schema(description="结果状态：0处理中 1成功 2失败")
    private Integer status;

	/**
	* 评估等级：A/B/C/D
	*/
    @Schema(description="评估等级：A/B/C/D")
    private String level;

	/**
	* 评估报告日
	*/
    @Schema(description="评估报告日")
    private LocalDate reportDate;

	/**
	* 可专利性（星级1-5）
	*/
    @Schema(description="可专利性（星级1-5）")
    private String viability;

	/**
	* 技术竞争（星级1-5）
	*/
    @Schema(description="技术竞争（星级1-5））")
    private String tech;

	/**
	* 市场前景（星级1-5）
	*/
    @Schema(description="市场前景（星级1-5）")
    private String market;

	/**
	* 报告地址
	*/
    @Schema(description="报告地址")
    private String reportUrl;

	/**
	* 部门ID
	*/
    @Schema(description="部门ID")
    private Long deptId;

	/**
	* 创建人
	*/
    @Schema(description="创建人")
    private Long createUser;

	/**
	* 更新人
	*/
    @Schema(description="更新人")
    private Long updateUser;

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

	/**
	* 删除标记：0未删 1已删
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标记：0未删 1已删")
    private String delFlag;

	/**
	* 租户ID
	*/
    @Schema(description="租户ID")
    private Long tenantId;
}