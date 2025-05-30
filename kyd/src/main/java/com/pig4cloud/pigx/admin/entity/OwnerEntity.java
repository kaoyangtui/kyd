package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

/**
 * 申请人
 *
 * @author pigx
 * @date 2025-05-23 14:32:10
 */
@Data
@TenantTable
@TableName("t_owner")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "申请人")
public class OwnerEntity extends Model<OwnerEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 主表 code
     */
    @Schema(description = "主表 code")
    private String code;

    /**
     * 申请人名称
     */
    @Schema(description = "申请人名称")
    private String ownerName;

    /**
     * 申请人类型 0其他1第一
     */
    @Schema(description = "申请人类型 0其他1第一")
    private Integer ownerType;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标识")
    private String delFlag;

    /**
     * 租户
     */
    @Schema(description = "租户")
    private Long tenantId;
}