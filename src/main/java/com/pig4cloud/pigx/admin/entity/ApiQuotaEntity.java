package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 第三方API额度统计表
 *
 * @author pigx
 * @date 2025-09-26 10:27:27
 */
@Data
@TenantTable
@TableName("t_api_quota")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "第三方API额度统计表")
public class ApiQuotaEntity extends Model<ApiQuotaEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* API编码，如 YT_SF1V1
	*/
    @Schema(description="API编码，如 YT_SF1V1")
    private String apiCode;

	/**
	* 用户ID，NULL 表示不限用户
	*/
    @Schema(description="用户ID，NULL 表示不限用户")
    private Long userId;

	/**
	* 统计周期 TOTAL/DAILY/MONTHLY
	*/
    @Schema(description="统计周期 TOTAL/DAILY/MONTHLY")
    private String periodType;

	/**
	* 周期开始时间
	*/
    @Schema(description="周期开始时间")
    private LocalDateTime periodStart;

	/**
	* 周期结束时间
	*/
    @Schema(description="周期结束时间")
    private LocalDateTime periodEnd;

	/**
	* 本周期额度上限
	*/
    @Schema(description="本周期额度上限")
    private Integer limitTotal;

	/**
	* 本周期已使用次数
	*/
    @Schema(description="本周期已使用次数")
    private Integer usedCount;

	/**
	* 状态 1=启用 0=停用
	*/
    @Schema(description="状态 1=启用 0=停用")
    private Integer status;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

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
	* 租户ID，NULL 表示全局
	*/
    @Schema(description="租户ID，NULL 表示全局")
    private Long tenantId;
}