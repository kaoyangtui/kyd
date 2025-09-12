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
 * 专利转化监控表
 *
 * @author pigx
 * @date 2025-07-28 16:09:48
 */
@Data
@TenantTable
@TableName("t_patent_monitor_transform")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利转化监控表")
public class PatentMonitorTransformEntity extends Model<PatentMonitorTransformEntity> {


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
	* 申请号
	*/
    @Schema(description="申请号")
    private String appNumber;

	/**
	* 专利名称
	*/
    @Schema(description="专利名称")
    private String title;

	/**
	* 转化code
	*/
    @Schema(description="转化code")
    private String code;

	/**
	* 转化名称
	*/
    @Schema(description="转化名称")
    private String name;

	/**
	* 专利类型
	*/
    @Schema(description="专利类型")
    private String patType;

	/**
	* 事件日期
	*/
    @Schema(description="事件日期")
    private LocalDate eventTime;

	/**
	* 合同签订时间
	*/
    @Schema(description="合同签订时间")
    private LocalDate signDate;

	/**
	* 合同到期时间
	*/
    @Schema(description="合同到期时间")
    private LocalDate expireDate;

	/**
	* 所属院系
	*/
    @TableField(fill = FieldFill.INSERT)
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

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人ID")
    private Long createUserId;
}