package com.pig4cloud.pigx.admin.dto.researchTeam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 科研团队返回信息
 */
@Data
@Schema(description = "科研团队返回信息")
public class ResearchTeamResponse {

    public static final String BIZ_CODE = "RESEARCH_TEAM";

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "科研团队名称")
    private String name;

    @Schema(description = "团队负责人")
    private String leader;

    @Schema(description = "研究方向，多个以;分隔")
    private String researchTags;

    @Schema(description = "团队介绍")
    private String intro;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;

    @Schema(description = "所属院系")
    private Long deptId;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
