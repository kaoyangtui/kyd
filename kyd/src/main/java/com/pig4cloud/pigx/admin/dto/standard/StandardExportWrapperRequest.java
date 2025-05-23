package com.pig4cloud.pigx.admin.dto.standard;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "标准导出封装请求")
public class StandardExportWrapperRequest extends ExportWrapperRequest<StandardPageRequest> {
}