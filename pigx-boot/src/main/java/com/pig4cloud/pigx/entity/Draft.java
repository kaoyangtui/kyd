package com.pig4cloud.pigx.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 草稿管理表，用于管理用户草稿数据
 *
 * @author zl
 * @date 2024-12-26 14:33:33
 */
@Data
@TenantTable
@TableName("t_draft")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "草稿管理表，用于管理用户草稿数据")
public class Draft extends Model<Draft> {


    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

    /**
     * 草稿类型，区分不同类型的草稿
     */
    @Schema(description = "草稿类型，区分不同类型的草稿")
    private String draftType;

    /**
     * 草稿内容，保存JSON格式的草稿数据
     */
    @Schema(description = "草稿内容，保存JSON格式的草稿数据")
    private String draftContent;

    /**
     * 草稿状态：0-草稿，1-已提交
     */
    @Schema(description = "草稿状态：0-草稿，1-已提交")
    private Integer status;

    /**
     * 创建用户ID
     */
    @Schema(description = "创建用户ID")
    private Long createUser;

    /**
     * 修改用户ID
     */
    @Schema(description = "修改用户ID")
    private Long updateUser;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 删除标识，0-未删除，1-已删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标识，0-未删除，1-已删除")
    private String delFlag;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;
}