package com.pig4cloud.pigx.admin.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "通知公告新增请求")
public class NoticeCreateRequest {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件路径，多个用;分隔，DTO用List")
    private List<String> fileUrl;

    @Schema(description = "所属院系ID")
    private String deptId;
}
