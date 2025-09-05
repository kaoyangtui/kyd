package com.pig4cloud.pigx.admin.dto.ipAssign;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出封装结构
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "赋权导出请求封装")
public class IpAssignExportWrapperRequest extends ExportWrapperRequest<IpAssignPageRequest> {
} 