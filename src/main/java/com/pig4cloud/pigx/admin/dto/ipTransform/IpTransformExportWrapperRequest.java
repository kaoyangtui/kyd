package com.pig4cloud.pigx.admin.dto.ipTransform;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出请求封装
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "知识产权转化导出封装")
public class IpTransformExportWrapperRequest extends ExportWrapperRequest<IpTransformPageRequest> {
} 