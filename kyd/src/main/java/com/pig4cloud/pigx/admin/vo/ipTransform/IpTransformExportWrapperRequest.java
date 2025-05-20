package com.pig4cloud.pigx.admin.vo.ipTransform;

import com.pig4cloud.pigx.admin.vo.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 导出请求封装
 */
@Data
@Schema(description = "知识产权转化导出封装")
public class IpTransformExportWrapperRequest extends ExportWrapperRequest<IpTransformPageRequest> {
} 