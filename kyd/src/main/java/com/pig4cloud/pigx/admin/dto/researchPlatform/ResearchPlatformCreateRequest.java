package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研平台创建请求")
public class ResearchPlatformCreateRequest {

    @Schema(description = "平台名称", required = true)
    private String name;

    @Schema(description = "研究方向标签列表", required = true)
    private List<String> directions;

    @Schema(description = "平台介绍", required = true)
    private String intro;

    @Schema(description = "联系人姓名", required = true)
    private String contactName;

    @Schema(description = "联系人手机", required = true)
    private String contactPhone;

    @Schema(description = "平台负责人（格式：姓名(工号)）", required = true)
    private String principal;
}
