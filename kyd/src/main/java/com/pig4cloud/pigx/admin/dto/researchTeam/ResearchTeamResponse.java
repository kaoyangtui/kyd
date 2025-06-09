package com.pig4cloud.pigx.admin.dto.researchTeam;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科研团队返回信息")
public class ResearchTeamResponse {

    public static final String BIZ_CODE = "RESEARCH_TEAM";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "科研团队编码")
    private String code;

    @Schema(description = "科研团队名称")
    private String name;

    @Schema(description = "团队研究方向")
    private List<String> researchTags;

    @Schema(description = "团队介绍")
    private String intro;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    /**
     * 负责人编码
     */
    @Schema(description = "负责人编码")
    private String leaderCode;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "团队成员列表")
    private List<CompleterEntity> completers;
}
