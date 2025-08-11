package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 专利费用报销表
 *
 * @author pigx
 * @date 2025-07-31 10:05:30
 */
@Data
@TenantTable
@TableName("t_patent_fee_reimburse")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利费用报销表")
public class PatentFeeReimburseEntity extends Model<PatentFeeReimburseEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

    /**
     * 流程实例 ID
     */
    @Schema(description = "流程实例 ID")
    private String flowInstId;

    /**
     * 流程KEY
     */
    @Schema(description = "流程KEY")
    private String flowKey;

    /**
     * 流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止
     */
    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

	/**
	* 知识产权类型
	*/
    @Schema(description="知识产权类型")
    private String ipType;

	/**
	* 知识产权ID
	*/
    @Schema(description="知识产权ID")
    private String ipCode;

	/**
	* 专利申请号
	*/
    @Schema(description="专利申请号")
    private String appNumber;

	/**
	* 专利名称
	*/
    @Schema(description="专利名称")
    private String title;

	/**
	* 专利类型
	*/
    @Schema(description="专利类型")
    private String patType;

	/**
	* 关联提案编码
	*/
    @Schema(description="关联提案编码")
    private String proposalCode;

	/**
	* 项目 ID
	*/
    @Schema(description="项目 ID")
    private Long researchProjectId;

	/**
	* 评估等级
	*/
    @Schema(description="评估等级")
    private String evalLevel;

	/**
	* 专利受理通知书URL，多个用;分隔
	*/
    @Schema(description="专利受理通知书URL，多个用;分隔")
    private String acceptFileUrl;

	/**
	* 委托代理合同URL，多个用;分隔
	*/
    @Schema(description="委托代理合同URL，多个用;分隔")
    private String agentContractUrl;

	/**
	* 专利申请说明书URL，多个用;分隔
	*/
    @Schema(description="专利申请说明书URL，多个用;分隔")
    private String appBookUrl;

	/**
	* 项目合同或任务书URL，多个用;分隔
	*/
    @Schema(description="项目合同或任务书URL，多个用;分隔")
    private String contractUrl;

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