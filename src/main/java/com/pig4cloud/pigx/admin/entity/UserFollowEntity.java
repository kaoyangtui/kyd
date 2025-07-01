package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户关注表
 *
 * @author pigx
 * @date 2025-06-16 14:25:59
 */
@Data
@TableName("t_user_follow")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户关注表")
public class UserFollowEntity extends Model<UserFollowEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 用户ID
	*/
    @Schema(description="用户ID")
    private Long userId;

	/**
	* 关注类型，如USER、PROJECT、ARTICLE等
	*/
    @Schema(description="关注类型，如USER、PROJECT、ARTICLE等")
    private String followType;

	/**
	* 关注类型ID（如关注对象主键ID）
	*/
    @Schema(description="关注类型ID（如关注对象主键ID）")
    private Long followId;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}