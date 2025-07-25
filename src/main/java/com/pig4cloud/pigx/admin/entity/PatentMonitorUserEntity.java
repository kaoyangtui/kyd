package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 专利用户监控表
 *
 * @author pigx
 * @date 2025-07-25 08:57:20
 */
@Data
@TenantTable
@TableName("t_patent_monitor_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利用户监控表")
public class PatentMonitorUserEntity extends Model<PatentMonitorUserEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 专利唯一ID
	*/
    @Schema(description="专利唯一ID")
    private String pid;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String title;

    /**
     * 专利类型
     */
    @Schema(description = "专利类型")
    private String patType;

    /**
     * 申请号 (数组)
     */
    @Schema(description = "申请号 (数组)")
    private String appNumber;

	/**
	* 所属院系
	*/
    @Schema(description="所属院系")
    private String deptId;

	/**
	* 创建/提交人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交人")
    private String createBy;

	/**
	* 创建/提交时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交时间")
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