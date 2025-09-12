package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 监控主表（企业/技术汇总去重 + 统计）
 *
 * @author pigx
 * @date 2025-09-11 13:37:13
 */
@Data
@TenantTable
@TableName("t_external_patent_monitor_subject")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "监控主表（企业/技术汇总去重 + 统计）")
public class ExternalPatentMonitorSubjectEntity extends Model<ExternalPatentMonitorSubjectEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 监控类型：1企业 2技术
	*/
    @Schema(description="监控类型：1企业 2技术")
    private Integer monitorType;

	/**
	* 企业名称或关键词（主展示值）
	*/
    @Schema(description="企业名称或关键词（主展示值）")
    private String monitorContent;

	/**
	* 监控状态：0停用 1监控中 2暂停
	*/
    @Schema(description="监控状态：0停用 1监控中 2暂停")
    private Integer monitorStatus;

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
	* 累计事件总数
	*/
    @Schema(description="累计事件总数")
    private Integer eventCountTotal;

	/**
	* 公开数
	*/
    @Schema(description="公开数")
    private Integer eventCountPub;

	/**
	* 授权数
	*/
    @Schema(description="授权数")
    private Integer eventCountGrant;

	/**
	* 转让数
	*/
    @Schema(description="转让数")
    private Integer eventCountTransfer;

	/**
	* 无效/终止数
	*/
    @Schema(description="无效/终止数")
    private Integer eventCountInvalid;

	/**
	* 质押数
	*/
    @Schema(description="质押数")
    private Integer eventCountPledge;

	/**
	* 首条事件时间
	*/
    @Schema(description="首条事件时间")
    private LocalDateTime firstEventTime;

	/**
	* 最近事件时间
	*/
    @Schema(description="最近事件时间")
    private LocalDateTime lastEventTime;

	/**
	* 最近事件类型（同上枚举）
	*/
    @Schema(description="最近事件类型（同上枚举）")
    private Integer lastEventType;

	/**
	* 最近事件关联专利PID
	*/
    @Schema(description="最近事件关联专利PID")
    private String lastPatPid;

	/**
	* 最近事件关联申请号
	*/
    @Schema(description="最近事件关联申请号")
    private String lastAppNumber;

	/**
	* 引用该条目的配置数（去重后被多少配置/用户关注）
	*/
    @Schema(description="引用该条目的配置数（去重后被多少配置/用户关注）")
    private Integer bindCount;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

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