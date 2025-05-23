package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 专利提案—申请人
 *
 * @author pigx
 * @date 2025-05-23 11:33:18
 */
@Data
@TenantTable
@TableName("t_patent_proposal_owner")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利提案—申请人")
public class PatentProposalOwnerEntity extends Model<PatentProposalOwnerEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 专利提案ID
	*/
    @Schema(description="专利提案ID")
    private Long patentProposalId;

	/**
	* 申请人名称
	*/
    @Schema(description="申请人名称")
    private String ownerName;

	/**
	* 申请人类型 0其他1第一
	*/
    @Schema(description="申请人类型 0其他1第一")
    private Integer ownerType;

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