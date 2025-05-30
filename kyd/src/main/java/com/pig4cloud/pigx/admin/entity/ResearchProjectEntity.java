package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 科研项目信息表
 *
 * @author pigx
 * @date 2025-05-30 08:30:40
 */
@Data
@TenantTable
@TableName("t_research_project")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研项目信息表")
public class ResearchProjectEntity extends Model<ResearchProjectEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 项目名称
	*/
    @Schema(description="项目名称")
    private String projectName;

	/**
	* 项目类型
	*/
    @Schema(description="项目类型")
    private String projectType;

	/**
	* 关联提案数
	*/
    @Schema(description="关联提案数")
    private Integer proposalCount;

	/**
	* 关联成果数
	*/
    @Schema(description="关联成果数")
    private Integer resultCount;

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