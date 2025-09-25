package com.pig4cloud.pigx.admin.dto.demandReceive;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import com.pig4cloud.pigx.admin.dto.demand.DemandPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求接收导出封装请求")
@Data
public class DemandReceiveExportWrapperRequest extends ExportWrapperRequest<DemandReceivePageRequest> {
}