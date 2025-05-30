package com.pig4cloud.pigx.admin.dto.researchProject;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研项目导出请求包装")
public class ResearchProjectExportWrapperRequest extends ExportWrapperRequest<ResearchProjectPageRequest> {

}
