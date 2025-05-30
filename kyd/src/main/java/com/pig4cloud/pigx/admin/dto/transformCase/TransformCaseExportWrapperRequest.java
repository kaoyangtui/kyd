package com.pig4cloud.pigx.admin.dto.transformCase;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "转化案例导出封装请求")
public class TransformCaseExportWrapperRequest extends ExportWrapperRequest<TransformCasePageRequest> {
}
