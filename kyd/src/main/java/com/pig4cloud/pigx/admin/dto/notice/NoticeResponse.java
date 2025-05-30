package com.pig4cloud.pigx.admin.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "通知公告响应")
public class NoticeResponse {

    public static final String BIZ_CODE = "NOTICE";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "附件路径，多个用;分隔")
    private List<String> fileUrl;

    @Schema(description = "创建/提交时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建/提交人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "所属院系")
    private String deptId;
}
