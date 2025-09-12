package com.pig4cloud.pigx.admin.dto.researchTeam;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研团队分页查询请求")
public class ResearchTeamPageRequest extends BasePageQuery {

    @Schema(description = "关键词（名称/研究方向/介绍模糊匹配）")
    private String keyword;

    /**
     * 负责人编码
     */
    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "团队研究方向")
    private String researchTag;

    @Schema(description = "上下架状态")
    private Integer shelfStatus;

    @Schema(description = "所属院系")
    private Long deptId;

    @Schema(description = "提交时间起（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "提交时间止（yyyy-MM-dd）")
    private String endTime;
}
