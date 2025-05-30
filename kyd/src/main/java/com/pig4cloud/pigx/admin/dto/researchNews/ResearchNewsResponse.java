package com.pig4cloud.pigx.admin.dto.researchNews;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研动态响应")
public class ResearchNewsResponse {
    public static final String BIZ_CODE = "RESEARCH_NEWS";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "供稿方")
    private String provider;

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

    @Schema(description = "附件URL，多文件用;分隔，DTO用List")
    private List<String> fileUrl;
}
