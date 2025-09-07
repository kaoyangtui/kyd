package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业绩点-规则
 *
 * @author pigx
 * @date 2025-09-06 23:39:00
 */
@Data
@TenantTable
@TableName("t_perf_rule")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "业绩点-规则")
public class PerfRuleEntity extends Model<PerfRuleEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 方案ID t_perf_scheme.id
	*/
    @Schema(description="方案ID t_perf_scheme.id")
    private Long schemeId;

	/**
	* 序号(显示排序)
	*/
    @Schema(description="序号(显示排序)")
    private Integer ordNo;

	/**
	* 知识产权类型编码 如: PATENT_INVENTION/UTILITY/ DESIGN/SOFT/STD/PLANT/IC/ASSIGN
	*/
    @Schema(description="知识产权类型编码 如: PATENT_INVENTION/UTILITY/ DESIGN/SOFT/STD/PLANT/IC/ASSIGN")
    private String ipTypeCode;

	/**
	* 知识产权类型名称 例如: 发明专利/实用新型/外观设计/软著/标准/植物新品种/集成电路/赋权
	*/
    @Schema(description="知识产权类型名称 例如: 发明专利/实用新型/外观设计/软著/标准/植物新品种/集成电路/赋权")
    private String ipTypeName;

	/**
	* 规则事件编码 如: APPLY_PUB(申请公示), GRANT_PUB(授权公示), PRE_EXAM(预审)…
	*/
    @Schema(description="规则事件编码 如: APPLY_PUB(申请公示), GRANT_PUB(授权公示), PRE_EXAM(预审)…")
    private String ruleEventCode;

	/**
	* 规则事件名称 例如: 申请公示/授权公示/预审
	*/
    @Schema(description="规则事件名称 例如: 申请公示/授权公示/预审")
    private String ruleEventName;

	/**
	* 业绩分
	*/
    @Schema(description="业绩分")
    private BigDecimal score;

	/**
	* 状态 0停用 1启用
	*/
    @Schema(description="状态 0停用 1启用")
    private Integer status;

	/**
	* 可选：附加条件(JSON)，如限定学院、学科、等级、是否高价值等
	*/
    @Schema(description="可选：附加条件(JSON)，如限定学院、学科、等级、是否高价值等")
    private String extraCondJson;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "所属组织ID")
    private Long deptId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "组织名称")
    private String deptName;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人ID")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人姓名")
    private String createUserName;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private Long createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 更新人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新人")
    private Long updateBy;

	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;

	/**
	* 逻辑删除 0未删 1已删
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="逻辑删除 0未删 1已删")
    private String delFlag;

	/**
	* 租户
	*/
    @Schema(description="租户")
    private Long tenantId;
}