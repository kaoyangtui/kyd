package com.pig4cloud.pigx.admin.dto.researchTeam;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研团队分页查询请求")
public class ResearchTeamPageRequest extends BasePageQuery {

    @Schema(description = "科研团队名称")
    private String name;

    @Schema(description = "负责人姓名")
    private String leader;

    @Schema(description = "上下架状态")
    private Integer shelfStatus;
}
