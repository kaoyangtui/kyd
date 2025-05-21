package com.pig4cloud.pigx.admin.dto.consult;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 咨询导出封装请求
 *
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "咨询导出封装请求")
public class ConsultExportWrapperRequest extends ExportWrapperRequest<ConsultPageRequest> {
}
