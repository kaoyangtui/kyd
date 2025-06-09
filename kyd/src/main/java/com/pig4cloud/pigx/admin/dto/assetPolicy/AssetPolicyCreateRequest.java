package com.pig4cloud.pigx.admin.dto.assetPolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "资产政策新增请求")
public class AssetPolicyCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题")
    private String title;

    @NotBlank(message = "供稿不能为空")
    @Schema(description = "供稿")
    private String source;

    @NotBlank(message = "内容不能为空")
    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件URL，多个用;分隔")
    private List<String> fileUrl;
}
