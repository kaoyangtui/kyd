package com.pig4cloud.pigx.admin.dto.assetNews;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "资产资讯修改请求")
public class AssetNewsUpdateRequest extends AssetNewsCreateRequest {

    @Schema(description = "主键ID")
    private Long id;

}
