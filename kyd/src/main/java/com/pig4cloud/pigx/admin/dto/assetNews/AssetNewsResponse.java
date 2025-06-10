package com.pig4cloud.pigx.admin.dto.assetNews;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "资产资讯响应")
public class AssetNewsResponse {

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "所属院系ID")
    private String deptId;
}
