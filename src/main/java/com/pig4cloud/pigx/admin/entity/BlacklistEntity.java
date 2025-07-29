package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

/**
 * 黑名单设置表
 *
 * @author pigx
 * @date 2025-07-29 17:59:05
 */
@Data
@TenantTable
@TableName("t_blacklist")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "黑名单设置表")
public class BlacklistEntity extends Model<BlacklistEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 用户ID
	*/
    @Schema(description="用户ID")
    private Long userId;

	/**
	* 用户名
	*/
    @Schema(description="用户名")
    private String userName;

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