package com.pig4cloud.pigx.admin.dto.assetPolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "资产政策响应")
public class AssetPolicyResponse {

    public static final String BIZ_CODE = "ASSET_POLICY";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "供稿")
    private String source;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件URL，多个用;分隔，DTO用List")
    private List<String> fileUrl;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "所属院系ID")
    private String deptId;
}
