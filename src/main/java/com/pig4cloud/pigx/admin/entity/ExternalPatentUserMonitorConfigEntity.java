package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 用户监控配置（企业/技术，单值）
 *
 * @author pigx
 * @date 2025-09-11 13:36:17
 */
@Data
@TenantTable
@TableName("t_external_patent_user_monitor_config")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户监控配置（企业/技术，单值）")
public class ExternalPatentUserMonitorConfigEntity extends Model<ExternalPatentUserMonitorConfigEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 所属用户ID
	*/
    @Schema(description="所属用户ID")
    private Long userId;

	/**
	* 监控类型：1企业 2技术
	*/
    @Schema(description="监控类型：1企业 2技术")
    private Integer monitorType;

	/**
	* 企业名称或关键词（单值）
	*/
    @Schema(description="企业名称或关键词（单值）")
    private String monitorContent;

	/**
	* 监控状态：0已取消 1监控中 2暂停
	*/
    @Schema(description="监控状态：0已取消 1监控中 2暂停")
    private Integer monitorStatus;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

	/**
	* 企业ID（如有企业库）
	*/
    @Schema(description="企业ID（如有企业库）")
    private Long enterpriseId;

	/**
	* 企业名称（快照）
	*/
    @Schema(description="企业名称（快照）")
    private String enterpriseName;

	/**
	* 统一社会信用代码
	*/
    @Schema(description="统一社会信用代码")
    private String creditCode;

	/**
	* 所属组织ID
	*/
    @Schema(description="所属组织ID")
    private Long deptId;

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