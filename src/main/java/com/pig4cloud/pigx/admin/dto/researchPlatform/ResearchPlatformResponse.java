package com.pig4cloud.pigx.admin.dto.researchPlatform;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "科研平台响应")
public class ResearchPlatformResponse extends BaseResponse {

    public static final String BIZ_CODE = "RESEARCH_PLATFORM";

    @Schema(description = "主键ID")
    private Long id;

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

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;

}
