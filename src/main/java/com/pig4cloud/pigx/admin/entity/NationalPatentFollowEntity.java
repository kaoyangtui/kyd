package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 全国专利关注表
 *
 * @author pigx
 * @date 2025-09-26 21:55:44
 */
@Data
@TenantTable
@TableName("t_national_patent_follow")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "全国专利关注表")
public class NationalPatentFollowEntity extends Model<NationalPatentFollowEntity> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Long id;

	/**
	* 用户ID
	*/
    @Schema(description="用户ID")
    private Long userId;

	/**
	* 专利唯一ID，对应 t_national_patent_info.pid
	*/
    @Schema(description="专利唯一ID，对应 t_national_patent_info.pid")
    private String pid;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String note;

	/**
	* 标签;分号分隔
	*/
    @Schema(description="标签;分号分隔")
    private String tags;
 
	/**
	* createTime
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="createTime")
    private LocalDateTime createTime;
 
	/**
	* updateTime
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="updateTime")
    private LocalDateTime updateTime;

	/**
	* 租户ID
	*/
    @Schema(description="租户ID")
    private Long tenantId;
}