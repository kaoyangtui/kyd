package com.pig4cloud.pigx.admin.dto.assetNews;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "资产资讯新增请求")
public class AssetNewsCreateRequest {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "供稿")
    private String source;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件URL，多文件用;分隔，DTO用List")
    private List<String> fileUrl;
}
