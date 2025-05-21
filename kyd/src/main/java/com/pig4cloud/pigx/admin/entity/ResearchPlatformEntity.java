package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 科研平台信息表
 *
 * @author pigx
 * @date 2025-05-21 16:23:21
 */
@Data
@TenantTable
@TableName("t_research_platform")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研平台信息表")
public class ResearchPlatformEntity extends Model<ResearchPlatformEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 科研平台名称
	*/
    @Schema(description="科研平台名称")
    private String name;

	/**
	* 平台研究方向
	*/
    @Schema(description="平台研究方向")
    private String direction;

	/**
	* 平台介绍
	*/
    @Schema(description="平台介绍")
    private String intro;

	/**
	* 联系人姓名
	*/
    @Schema(description="联系人姓名")
    private String contactName;

	/**
	* 联系人手机
	*/
    @Schema(description="联系人手机")
    private String contactPhone;

	/**
	* 平台负责人
	*/
    @Schema(description="平台负责人")
    private String principal;

	/**
	* 上下架状态（0下架 1上架）
	*/
    @Schema(description="上下架状态（0下架 1上架）")
    private Integer shelfStatus;

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