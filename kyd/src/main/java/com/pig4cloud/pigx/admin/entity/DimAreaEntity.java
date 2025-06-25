package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 行政区
 *
 * @author pigx
 * @date 2025-06-16 18:32:36
 */
@Data
@TableName("t_dim_area")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "行政区")
public class DimAreaEntity extends Model<DimAreaEntity> {


	/**
	* 区划代码
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="区划代码")
    private Long code;

	/**
	* 名称
	*/
    @Schema(description="名称")
    private String name;

	/**
	* 级别1-5,省市县镇村
	*/
    @Schema(description="级别1-5,省市县镇村")
    private Integer level;

	/**
	* 父级区划代码
	*/
    @Schema(description="父级区划代码")
    private Long pcode;

	/**
	* 城乡分类
	*/
    @Schema(description="城乡分类")
    private Integer category;
}