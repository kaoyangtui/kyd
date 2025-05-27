package com.pig4cloud.pigx.admin.dto.assetPolicy;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产政策导出封装请求")
public class AssetPolicyExportWrapperRequest extends ExportWrapperRequest<AssetPolicyPageRequest> {

}
