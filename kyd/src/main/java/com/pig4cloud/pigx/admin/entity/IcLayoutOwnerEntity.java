package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 集成电路布图权利人信息
 *
 * @author pigx
 * @date 2025-05-15 12:13:49
 */
@Data
@TenantTable
@TableName("t_ic_layout_owner")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "集成电路布图权利人信息")
public class IcLayoutOwnerEntity extends Model<IcLayoutOwnerEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 关联集成电路ID
	*/
    @Schema(description="关联集成电路ID")
    private Long icLayoutId;

	/**
	* 权利人名称
	*/
    @Schema(description="权利人名称")
    private String name;

	/**
	* 所属院系
	*/
    @Schema(description="所属院系")
    private String deptId;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

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