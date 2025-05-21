package com.pig4cloud.pigx.admin.dto.researchTeam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "科研团队成员信息")
public class ResearchTeamMemberVO {

    @Schema(description = "成员ID，更新时用")
    private Long id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "学工号")
    private String jobNo;

    @Schema(description = "院系部门")
    private String deptName;

    @Schema(description = "职称")
    private String title;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "研究方向")
    private String direction;
}
