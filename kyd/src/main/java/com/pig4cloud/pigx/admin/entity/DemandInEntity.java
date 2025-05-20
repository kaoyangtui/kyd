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
 * 成果需求信息表
 *
 * @author pigx
 * @date 2025-05-20 11:19:30
 */
@Data
@TenantTable
@TableName("t_demand_in")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "成果需求信息表")
public class DemandInEntity extends Model<DemandInEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 业务编码
	*/
    @Schema(description="业务编码")
    private String code;

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
	* 流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回
	*/
    @Schema(description="流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回")
    private Integer flowStatus;

	/**
	* 当前流程节点名称
	*/
    @Schema(description="当前流程节点名称")
    private String currentNodeName;

	/**
	* 需求名称
	*/
    @Schema(description="需求名称")
    private String name;

	/**
	* 需求类型
	*/
    @Schema(description="需求类型")
    private String type;

	@Schema(description = "上下架状态（0 下架，1 上架）")
	private Integer shelfStatus;

	/**
	* 所属领域
	*/
    @Schema(description="所属领域")
    private String field;

	/**
	* 需求有效期
	*/
    @Schema(description="需求有效期")
    private LocalDate validUntil;

	/**
	* 需求描述
	*/
    @Schema(description="需求描述")
    private String description;

	/**
	* 需求标签，多个以;分隔
	*/
    @Schema(description="需求标签，多个以;分隔")
    private String tags;

	/**
	* 需求附件URL
	*/
    @Schema(description="需求附件URL")
    private String attachFileUrl;

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