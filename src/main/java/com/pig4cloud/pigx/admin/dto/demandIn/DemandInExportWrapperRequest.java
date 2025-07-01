package com.pig4cloud.pigx.admin.dto.demandIn;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 导出封装请求
 */
@Data
@Schema(description = "校内需求导出封装")
public class DemandInExportWrapperRequest extends ExportWrapperRequest<DemandInPageRequest> {
} 