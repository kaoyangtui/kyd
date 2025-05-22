package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 成果披露—完成人
 *
 * @author pigx
 * @date 2025-05-22 10:44:25
 */
@Data
@TenantTable
@TableName("t_result_completer")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "成果披露—完成人")
public class ResultCompleterEntity extends Model<ResultCompleterEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 成果提案ID
	*/
    @Schema(description="成果提案ID")
    private Long resultId;

	/**
	* 学工号
	*/
    @Schema(description="学工号")
    private String completerNo;

	/**
	* 姓名
	*/
    @Schema(description="姓名")
    private String completerName;

	/**
	* 人员类型
	*/
    @Schema(description="人员类型")
    private String completerType;

	/**
	* 院系ID
	*/
    @Schema(description="院系ID")
    private Long completerDeptId;

	/**
	* 院系名称
	*/
    @Schema(description="院系名称")
    private String completerDeptName;

	/**
	* 联系电话
	*/
    @Schema(description="联系电话")
    private String completerPhone;

	/**
	* 电子邮箱
	*/
    @Schema(description="电子邮箱")
    private String completerEmail;

	/**
	* 是否负责人 0否1是
	*/
    @Schema(description="是否负责人 0否1是")
    private Integer completerLeader;

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