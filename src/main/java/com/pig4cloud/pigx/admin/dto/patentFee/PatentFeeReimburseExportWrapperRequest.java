package com.pig4cloud.pigx.admin.dto.patentFee;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专利费用报销导出封装请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利费用报销导出封装请求")
public class PatentFeeReimburseExportWrapperRequest extends ExportWrapperRequest<PatentFeeReimbursePageRequest> {
}
