package com.pig4cloud.pigx.admin.dto.icLayout;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "集成电路布图导出请求")
public class IcLayoutExportWrapperRequest extends ExportWrapperRequest<IcLayoutPageRequest> {


}