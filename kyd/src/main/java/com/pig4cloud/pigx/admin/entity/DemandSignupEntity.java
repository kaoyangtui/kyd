package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 企业需求报名信息表
 *
 * @author pigx
 * @date 2025-05-20 13:03:54
 */
@Data
@TenantTable
@TableName("t_demand_signup")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求报名信息表")
public class DemandSignupEntity extends Model<DemandSignupEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 关联需求ID
	*/
    @Schema(description="关联需求ID")
    private Long demandId;

	/**
	* 报名人姓名
	*/
    @Schema(description="报名人姓名")
    private String name;

	/**
	* 学院/院系名称
	*/
    @Schema(description="学院/院系名称")
    private String deptName;

	/**
	* 联系电话
	*/
    @Schema(description="联系电话")
    private String phone;

	/**
	* 邮箱
	*/
    @Schema(description="邮箱")
    private String email;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

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