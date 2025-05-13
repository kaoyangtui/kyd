package com.pig4cloud.pigx.admin.vo.SoftCopyReg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 软著登记完成人信息
 */
@Data
@Schema(description = "软著登记完成人信息")
public class SoftCopyRegCompleterRequest {

    @Schema(description = "学工号")
    private String jobNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "人员类型（教师、学生、校外人员）")
    private String type;

    @Schema(description = "院系或单位")
    private String deptName;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否负责人 0否1是")
    private Integer isLeader;
}
