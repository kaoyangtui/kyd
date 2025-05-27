package com.pig4cloud.pigx.admin.dto.researchPlatform;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研平台导出请求封装")
public class ResearchPlatformExportWrapperRequest extends ExportWrapperRequest<ResearchPlatformPageRequest> {
}
