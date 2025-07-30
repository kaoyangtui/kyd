package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 知识产权转化分配方案表
 *
 * @author pigx
 * @date 2025-07-30 12:02:09
 */
@Data
@TableName("t_ip_transform_plan")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "知识产权转化分配方案表")
public class IpTransformPlanEntity extends Model<IpTransformPlanEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 转化主表ID，关联t_ip_transform.id
	*/
    @Schema(description="转化主表ID，关联t_ip_transform.id")
    private Long transformId;

	/**
	* 项目代码
	*/
    @Schema(description="项目代码")
    private String transformCode;

	/**
	* 项目名称
	*/
    @Schema(description="项目名称")
    private String projectName;

	/**
	* 分配比例（%）
	*/
    @Schema(description="分配比例（%）")
    private BigDecimal ratio;

	/**
	* 发放金额（万元）
	*/
    @Schema(description="发放金额（万元）")
    private BigDecimal amount;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;
}