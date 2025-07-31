package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.math.BigDecimal;

/**
 * 专利费用报销明细表
 *
 * @author pigx
 * @date 2025-07-31 10:06:09
 */
@Data
@TenantTable
@TableName("t_patent_fee_item")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利费用报销明细表")
public class PatentFeeItemEntity extends Model<PatentFeeItemEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 关联报销主表ID
	*/
    @Schema(description="关联报销主表ID")
    private Long reimburseId;

	/**
	* 费用类型
	*/
    @Schema(description="费用类型")
    private String itemType;

	/**
	* 金额（元）
	*/
    @Schema(description="金额（元）")
    private BigDecimal amount;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

	/**
	* 租户
	*/
    @Schema(description="租户")
    private Long tenantId;
}