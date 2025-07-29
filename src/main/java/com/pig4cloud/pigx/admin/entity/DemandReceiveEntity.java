package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业需求接收表
 *
 * @author pigx
 * @date 2025-07-29 12:33:46
 */
@Data
@TenantTable
@TableName("t_demand_receive")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求接收表")
public class DemandReceiveEntity extends Model<DemandReceiveEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

    /**
     * 关联需求ID
     */
    @Schema(description = "关联需求ID")
    private Long demandId;

	/**
	* 接收人ID
	*/
    @Schema(description="接收人ID")
    private Long receiveUserId;

	/**
	* 业务编码
	*/
    @Schema(description="业务编码")
    private String code;

	/**
	* 需求名称
	*/
    @Schema(description="需求名称")
    private String name;

	/**
	* 需求类型
	*/
    @Schema(description="需求类型")
    private String type;

	/**
	* 所属领域
	*/
    @Schema(description="所属领域")
    private String field;

	/**
	* 有效期开始时间
	*/
    @Schema(description="有效期开始时间")
    private LocalDate validStart;

	/**
	* 有效期结束时间
	*/
    @Schema(description="有效期结束时间")
    private LocalDate validEnd;

	/**
	* 预算金额（万元）
	*/
    @Schema(description="预算金额（万元）")
    private BigDecimal budget;

	/**
	* 需求摘要
	*/
    @Schema(description="需求摘要")
    private String description;

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
	* 所属院系
	*/
    @Schema(description="所属院系")
    private String deptId;

	/**
	* 组织名称
	*/
    @Schema(description="组织名称")
    private String deptName;

	/**
	* 创建人ID
	*/
    @Schema(description="创建人ID")
    private Long createUserId;

	/**
	* 创建人姓名
	*/
    @Schema(description="创建人姓名")
    private String createUserName;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

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