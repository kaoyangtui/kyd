package com.pig4cloud.pigx.admin.vo.SoftCopyReg;

import com.pig4cloud.pigx.admin.vo.ExportExecute.ExportExecuteRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 软著登记导出包装请求
 */
@Data
@Schema(description = "软著登记导出包装请求")
public class SoftCopyRegExportWrapperRequest {

    @Schema(description = "分页查询请求")
    private SoftCopyRegPageRequest query;

    @Schema(description = "导出执行请求")
    private ExportExecuteRequest export;
}
