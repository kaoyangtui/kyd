package com.pig4cloud.pigx.admin.dto.demand;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求导出封装请求")
@Data
public class DemandExportWrapperRequest extends ExportWrapperRequest<DemandPageRequest> {
}