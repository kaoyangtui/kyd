package com.pig4cloud.pigx.admin.dto.researchTeam;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研团队新增请求")
public class ResearchTeamCreateRequest {

    @Schema(description = "科研团队名称")
    private String name;

    @Schema(description = "团队负责人")
    private String leader;

    @Schema(description = "团队研究方向（多选用;分隔，DTO用List）")
    private List<String> researchTags;

    @Schema(description = "团队介绍")
    private String intro;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @Schema(description = "团队成员列表")
    private List<CompleterEntity> completers;
}
