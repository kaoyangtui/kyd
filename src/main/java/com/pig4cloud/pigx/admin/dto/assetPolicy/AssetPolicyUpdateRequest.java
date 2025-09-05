package com.pig4cloud.pigx.admin.dto.assetPolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "资产政策修改请求")
public class AssetPolicyUpdateRequest extends AssetPolicyCreateRequest {

    @Schema(description = "主键ID")
    private Long id;

}
