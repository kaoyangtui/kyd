package com.pig4cloud.pigx.admin.vo.SoftCopyReg;

import com.pig4cloud.pigx.admin.vo.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 软著登记导出包装请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "软著登记导出包装请求")
public class SoftCopyRegExportWrapperRequest extends ExportWrapperRequest<SoftCopyRegPageRequest> {

}
