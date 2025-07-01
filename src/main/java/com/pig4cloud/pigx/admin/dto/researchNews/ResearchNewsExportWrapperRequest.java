package com.pig4cloud.pigx.admin.dto.researchNews;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研动态导出请求包装")
public class ResearchNewsExportWrapperRequest extends ExportWrapperRequest<ResearchNewsPageRequest> {
}
