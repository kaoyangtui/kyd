package com.pig4cloud.pigx.admin.dto.patent;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利认领导出请求包装")
public class PatentClaimExportWrapperRequest extends ExportWrapperRequest<PatentClaimPageRequest> {
}
