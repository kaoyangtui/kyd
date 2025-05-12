package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "专利提案导出封装请求")
public class PatentProposalExportWrapperRequest {
    @Schema(description = "查询请求")
    private PatentProposalPageRequest query;

    @Schema(description = "导出配置")
    private ExportExecuteRequest export;
}
