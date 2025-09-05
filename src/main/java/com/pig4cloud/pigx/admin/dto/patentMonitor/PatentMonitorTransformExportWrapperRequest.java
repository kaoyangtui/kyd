package com.pig4cloud.pigx.admin.dto.patentMonitor;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利转化监控导出请求")
public class PatentMonitorTransformExportWrapperRequest extends ExportWrapperRequest<PatentMonitorTransformPageRequest> {
}
