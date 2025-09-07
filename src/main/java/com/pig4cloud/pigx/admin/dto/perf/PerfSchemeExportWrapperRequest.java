package com.pig4cloud.pigx.admin.dto.perf;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "业绩点方案-导出请求封装")
public class PerfSchemeExportWrapperRequest extends ExportWrapperRequest<PerfSchemePageRequest> {
}
