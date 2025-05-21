package com.pig4cloud.pigx.admin.dto.researchTeam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研团队上下架请求")
public class ResearchTeamShelfRequest {

    @Schema(description = "科研团队ID")
    private Long id;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;
}
