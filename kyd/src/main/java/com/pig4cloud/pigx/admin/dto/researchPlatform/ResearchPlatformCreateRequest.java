package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "科研平台新增请求")
public class ResearchPlatformCreateRequest {

    @Schema(description = "科研平台名称")
    private String name;

    @Schema(description = "平台研究方向；多个用;分隔")
    private List<String> direction;

    @Schema(description = "平台介绍")
    private String intro;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @Schema(description = "平台负责人")
    private String principal;
}
