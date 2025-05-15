package com.pig4cloud.pigx.admin.vo.softCopy;

import com.pig4cloud.pigx.admin.vo.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "软著导出请求包装")
public class SoftCopyExportWrapperRequest  extends ExportWrapperRequest<SoftCopyPageRequest> {

}
