package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 科研团队成员信息表
 *
 * @author pigx
 * @date 2025-05-21 16:53:05
 */
@Data
@TenantTable
@TableName("t_research_team_member")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研团队成员信息表")
public class ResearchTeamMemberEntity extends Model<ResearchTeamMemberEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 所属团队ID
	*/
    @Schema(description="所属团队ID")
    private Long teamId;

	/**
	* 姓名
	*/
    @Schema(description="姓名")
    private String name;

	/**
	* 学工号
	*/
    @Schema(description="学工号")
    private String jobNo;

	/**
	* 院系部门
	*/
    @Schema(description="院系部门")
    private String deptName;

	/**
	* 职称
	*/
    @Schema(description="职称")
    private String title;

	/**
	* 联系电话
	*/
    @Schema(description="联系电话")
    private String phone;

	/**
	* 电子邮箱
	*/
    @Schema(description="电子邮箱")
    private String email;

	/**
	* 研究方向
	*/
    @Schema(description="研究方向")
    private String direction;

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