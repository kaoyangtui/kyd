package com.pig4cloud.pigx.admin.dto.patentProposal;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 专利提案—申请人
 *
 * @author pigx
 * @date 2025-05-23 11:33:18
 */
@Data
@Schema(description = "专利提案—申请人")
public class PatentProposalOwnerVO {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 专利提案ID
     */
    @Schema(description = "专利提案ID")
    private Long patentProposalId;

    /**
     * 申请人名称
     */
    @Schema(description = "申请人名称")
    private String ownerName;

    /**
     * 申请人类型 0其他1第一
     */
    @Schema(description = "申请人类型 0其他1第一")
    private Integer ownerType;

}