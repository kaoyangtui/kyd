package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校内数据-部门
 *
 * @author pigx
 * @date 2025-06-18 18:13:25
 */
@Data
@TableName("v_bmxx")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "校内数据-部门")
public class BmxxEntity extends Model<BmxxEntity> {

 
	/**
	* bmdm
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="bmdm")
    private String bmdm;
 
	/**
	* bmmc
	*/
    @Schema(description="bmmc")
    private String bmmc;
 
	/**
	* fbmbh
	*/
    @Schema(description="fbmbh")
    private String fbmbh;
}