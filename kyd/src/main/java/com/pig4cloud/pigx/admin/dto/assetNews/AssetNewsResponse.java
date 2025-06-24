package com.pig4cloud.pigx.admin.dto.assetNews;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "资产资讯响应")
public class AssetNewsResponse extends BaseResponse {

    public static final String BIZ_CODE = "ASSET_NEWS";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "供稿")
    private String source;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件URL（List）")
    private List<String> fileUrl;

}
