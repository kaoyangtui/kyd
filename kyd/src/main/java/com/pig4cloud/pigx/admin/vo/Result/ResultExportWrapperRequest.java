package com.pig4cloud.pigx.admin.vo.Result;

import com.pig4cloud.pigx.admin.vo.ExportExecute.ExportExecuteRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "科研成果导出封装请求")
public class ResultExportWrapperRequest {
    @Schema(description = "查询请求")
    private ResultPageRequest query;

    @Schema(description = "导出配置")
    private ExportExecuteRequest export;
}