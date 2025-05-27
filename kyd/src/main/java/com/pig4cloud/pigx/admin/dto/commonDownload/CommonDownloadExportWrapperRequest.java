package com.pig4cloud.pigx.admin.dto.commonDownload;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "常用下载导出封装请求")
public class CommonDownloadExportWrapperRequest extends ExportWrapperRequest<CommonDownloadPageRequest> {
}
