package com.pig4cloud.pigx.admin.dto.standard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 标准校内起草人信息 VO
 */
@Data
@Schema(description = "标准校内起草人信息")
public class StandardDrafterInVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "学工号")
    private String jobNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "人员类型 教师、学生")
    private String type;

    @Schema(description = "院系部门")
    private String deptName;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否负责人 0否1是")
    private Integer isLeader;
}
