package com.pig4cloud.pigx.admin.dto.researchTeam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改科研团队请求")
public class ResearchTeamUpdateRequest extends ResearchTeamCreateRequest {

    @Schema(description = "科研团队ID")
    private Long id;
}
