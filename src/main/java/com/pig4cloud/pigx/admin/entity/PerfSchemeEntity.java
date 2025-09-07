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
 * 业绩点-方案
 *
 * @author pigx
 * @date 2025-09-06 23:38:26
 */
@Data
@TenantTable
@TableName("t_perf_scheme")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "业绩点-方案")
public class PerfSchemeEntity extends Model<PerfSchemeEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 业绩分计算名称
	*/
    @Schema(description="业绩分计算名称")
    private String schemeName;

	/**
	* 计算周期-开始
	*/
    @Schema(description="计算周期-开始")
    private LocalDate periodStart;

	/**
	* 计算周期-结束
	*/
    @Schema(description="计算周期-结束")
    private LocalDate periodEnd;

	/**
	* 状态 0停用 1启用
	*/
    @Schema(description="状态 0停用 1启用")
    private Integer status;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

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