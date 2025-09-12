package com.pig4cloud.pigx.admin.dto.patentProposal;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 专利提案分页查询请求
 * 支持关键字、类型、流程状态、时间、按 ID、按 range 查询
 * 继承统一分页查询基类
 *
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利提案分页查询请求")
public class PatentProposalPageRequest extends BasePageQuery {

    @Schema(description = "编码或专利名称关键词")
    private String keyword;

    @Schema(description = "拟申请专利类型")
    private String type;

    @Schema(description = "所属院系")
    private Long deptId;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "当前流程节点")
    private String currentNodeName;

    @Schema(description = "起始提交时间（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "结束提交时间（yyyy-MM-dd）")
    private String endTime;
}
