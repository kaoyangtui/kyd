package com.pig4cloud.pigx.admin.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "通知公告新增请求")
public class NoticeCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件路径，多个用;分隔，DTO用List")
    private List<String> fileUrl;
}
