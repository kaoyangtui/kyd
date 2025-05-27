package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研平台响应")
public class ResearchPlatformResponse {

    public static final String BIZ_CODE = "RESEARCH_PLATFORM";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "平台名称")
    private String name;

    @Schema(description = "研究方向标签列表")
    private List<String> directions;

    @Schema(description = "平台介绍")
    private String intro;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @Schema(description = "平台负责人（格式：姓名(工号)）")
    private String principal;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;
}
