package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教职工基本信息
 *
 * @author pigx
 * @date 2025-06-18 20:17:07
 */
@Data
@TableName("v_jzgjbxx_to_zscq")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "教职工基本信息")
public class JzgjbxxToZscqEntity extends Model<JzgjbxxToZscqEntity> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Long id;

	/**
	* 工号
	*/
    @Schema(description="工号")
    private String gh;

	/**
	* 姓名
	*/
    @Schema(description="姓名")
    private String xm;

	/**
	* 性别：M/F
	*/
    @Schema(description="性别：M/F")
    private String xb;

	/**
	* 移动电话
	*/
    @Schema(description="移动电话")
    private String yddh;

	/**
	* 职称
	*/
    @Schema(description="职称")
    private String zc;

	/**
	* 部门编号
	*/
    @Schema(description="部门编号")
    private String bmbh;

	/**
	* 部门名称
	*/
    @Schema(description="部门名称")
    private String bmmc;

	/**
	* 在岗类型
	*/
    @Schema(description="在岗类型")
    private String zglx;
}