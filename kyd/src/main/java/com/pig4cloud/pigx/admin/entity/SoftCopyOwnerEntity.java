package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 软著提案—著作权人
 *
 * @author pigx
 * @date 2025-05-13 08:16:31
 */
@Data
@TenantTable
@TableName("t_soft_copy_owner")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "软著提案—著作权人")
public class SoftCopyOwnerEntity extends Model<SoftCopyOwnerEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 软著提案ID
	*/
    @Schema(description="软著提案ID")
    private Long softCopyId;

	/**
	* 序号
	*/
    @Schema(description="序号")
    private Integer seq;

	/**
	* 著作权人名称
	*/
    @Schema(description="著作权人名称")
    private String ownerName;

	/**
	* 著作权人类型 0其他1第一
	*/
    @Schema(description="著作权人类型 0其他1第一")
    private Integer ownerType;

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