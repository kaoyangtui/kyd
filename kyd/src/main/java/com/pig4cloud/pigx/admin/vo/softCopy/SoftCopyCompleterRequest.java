package com.pig4cloud.pigx.admin.vo.softCopy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 软著提案完成人信息
 */
@Data
@Schema(description = "软著提案完成人信息")
public class SoftCopyCompleterRequest {

    @Schema(description = "学工号")
    private String completerNo;

    @Schema(description = "姓名")
    private String completerName;

    @Schema(description = "人员类型")
    private String completerType;

    @Schema(description = "院系")
    private String completerDept;

    @Schema(description = "电话")
    private String completerPhone;

    @Schema(description = "邮箱")
    private String completerEmail;

    @Schema(description = "是否负责人")
    private Integer completerLeader;
}