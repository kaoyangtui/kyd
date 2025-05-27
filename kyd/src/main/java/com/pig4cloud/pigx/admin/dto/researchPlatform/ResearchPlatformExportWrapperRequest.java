package com.pig4cloud.pigx.admin.dto.researchPlatform;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研平台导出请求包装")
public class ResearchPlatformExportWrapperRequest extends ExportWrapperRequest<ResearchPlatformPageRequest> {
}