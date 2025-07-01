package com.pig4cloud.pigx.admin.dto.researchTeam;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研团队新增请求")
public class ResearchTeamCreateRequest {

    @NotBlank(message = "科研团队名称不能为空")
    @Schema(description = "科研团队名称")
    private String name;

    @NotEmpty(message = "团队研究方向不能为空")
    @Schema(description = "团队研究方向（多选用;分隔，DTO用List）")
    private List<String> researchTags;

    @NotBlank(message = "团队介绍不能为空")
    @Schema(description = "团队介绍")
    private String intro;

    @NotBlank(message = "联系人姓名不能为空")
    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @NotBlank(message = "负责人编码不能为空")
    @Schema(description = "负责人编码")
    private String leaderCode;

    @NotBlank(message = "负责人姓名不能为空")
    @Schema(description = "负责人姓名")
    private String leaderName;

    @NotEmpty(message = "团队成员列表不能为空")
    @Schema(description = "团队成员列表")
    private List<CompleterEntity> completers;
}
