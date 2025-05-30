package com.pig4cloud.pigx.admin.dto.transformCase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "转化案例新增请求")
public class TransformCaseCreateRequest {

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "供稿方")
    private String provider;

    @Schema(description = "附件URL，多文件用;分隔，DTO用List")
    private List<String> fileUrl;
}
