package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "专利提案分页查询请求")
public class PatentProposalPageRequest {
    @Schema(description = "编码或专利名称关键词")
    private String keyword;

    @Schema(description = "拟申请专利类型")
    private String type;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点")
    private String currentNodeName;

    @Schema(description = "起始提交时间")
    private String beginTime;

    @Schema(description = "结束提交时间")
    private String endTime;

    @Schema(description = "页码")
    private long current = 1;

    @Schema(description = "分页大小")
    private long size = 10;
}