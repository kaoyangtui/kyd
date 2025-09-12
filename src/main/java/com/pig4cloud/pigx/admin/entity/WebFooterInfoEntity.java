package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 网站底部信息
 *
 * @author pigx
 * @date 2025-07-20 13:59:51
 */
@Data
@TenantTable
@TableName("t_web_footer_info")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "网站底部信息")
public class WebFooterInfoEntity extends Model<WebFooterInfoEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 网站名称
	*/
    @Schema(description="网站名称")
    private String siteName;

	/**
	* Logo地址
	*/
    @Schema(description="Logo地址")
    private String logoUrl;

	/**
	* 版权信息
	*/
    @Schema(description="版权信息")
    private String copyright;

	/**
	* ICP备案号
	*/
    @Schema(description="ICP备案号")
    private String icp;

	/**
	* 公安备案号
	*/
    @Schema(description="公安备案号")
    private String publicSecurity;

	/**
	* 地址
	*/
    @Schema(description="地址")
    private String address;

	/**
	* 电话
	*/
    @Schema(description="电话")
    private String phone;

	/**
	* 邮箱
	*/
    @Schema(description="邮箱")
    private String email;

	/**
	* 所属院系
	*/
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="所属院系")
    private Long deptId;

	/**
	* 创建/提交人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交人")
    private String createBy;

	/**
	* 创建/提交时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建/提交时间")
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