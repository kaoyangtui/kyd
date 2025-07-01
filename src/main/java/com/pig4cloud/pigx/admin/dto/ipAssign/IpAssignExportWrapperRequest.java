package com.pig4cloud.pigx.admin.dto.ipAssign;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 导出封装结构
 */
@Data
@Schema(description = "赋权导出请求封装")
public class IpAssignExportWrapperRequest extends ExportWrapperRequest<IpAssignPageRequest> {
} 