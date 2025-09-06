package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 专利认领表
 *
 * @author pigx
 * @date 2025-07-14 08:58:50
 */
@Data
@TenantTable
@TableName("t_patent_claim")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利认领表")
public class PatentClaimEntity extends Model<PatentClaimEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

    /**
     * 专利唯一ID
     */
    @Schema(description = "专利唯一ID")
    private String pid;

	/**
	* 认领编码
	*/
    @Schema(description="认领编码")
    private String code;

	/**
	* 申请号
	*/
    @Schema(description="申请号")
    private String appNumber;

	/**
	* 名称
	*/
    @Schema(description="名称")
    private String title;

	/**
	* 发明（设计）人
	*/
    @Schema(description="发明（设计）人")
    private String inventorName;

	/**
	* 流程实例 ID
	*/
    @Schema(description="流程实例 ID")
    private String flowInstId;

	/**
	* 流程KEY
	*/
    @Schema(description="流程KEY")
    private String flowKey;

	/**
	* 流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止
	*/
    @Schema(description="流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

	/**
	* 当前流程节点名称
	*/
    @Schema(description="当前流程节点名称")
    private String currentNodeName;

    /**
     * 所属组织ID
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "所属组织ID")
    private Long deptId;

    /**
     * 组织名称
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "组织名称")
    private String deptName;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人ID")
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人姓名")
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