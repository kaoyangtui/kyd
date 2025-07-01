package com.pig4cloud.pigx.admin.dto.softCopyReg;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "软著登记导出封装请求")
public class SoftCopyRegExportWrapperRequest extends ExportWrapperRequest<SoftCopyRegPageRequest> {

}
