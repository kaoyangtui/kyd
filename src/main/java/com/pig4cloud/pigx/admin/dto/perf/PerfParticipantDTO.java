package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "参与人信息")
public class PerfParticipantDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "工号")
    private String userCode;

    @Schema(description = "姓名")
    private String userName;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "顺位")
    private Integer priority;

    @Schema(description = "是否负责人 0否 1是")
    private Integer isLeader;
}
