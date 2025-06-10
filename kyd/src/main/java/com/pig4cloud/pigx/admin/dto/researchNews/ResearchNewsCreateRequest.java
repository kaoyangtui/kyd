package com.pig4cloud.pigx.admin.dto.researchNews;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研动态新增请求")
public class ResearchNewsCreateRequest {

    @NotBlank(message = "图片地址不能为空")
    @Schema(description = "图片地址")
    private String imgUrl;

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Schema(description = "内容")
    private String content;

    @NotBlank(message = "供稿方不能为空")
    @Schema(description = "供稿方")
    private String provider;

    @Schema(description = "附件URL，多文件用;分隔，DTO用List")
    private List<String> fileUrl;
}
