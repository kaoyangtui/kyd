package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标准类型
 *
 * @author pigx
 * @date 2025-10-11 08:59:58
 */
@Data
@TableName("t_dim_standard_type")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "标准类型")
public class DimStandardTypeEntity extends Model<DimStandardTypeEntity> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Integer id;
 
	/**
	* parentId
	*/
    @Schema(description="parentId")
    private Integer parentId;
 
	/**
	* code
	*/
    @Schema(description="code")
    private String code;
 
	/**
	* name
	*/
    @Schema(description="name")
    private String name;
 
	/**
	* level
	*/
    @Schema(description="level")
    private Integer level;

	/**
	* 创建人ID
	*/
    @Schema(description="创建人ID")
    private Long createUserId;

	/**
	* 创建人姓名
	*/
    @Schema(description="创建人姓名")
    private String createUserName;
}