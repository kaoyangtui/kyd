package com.pig4cloud.pigx.admin.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 成果披露—完成人
 *
 * @author pigx
 * @date 2025-05-22 10:44:25
 */
@Data
@Schema(description = "成果披露—完成人")
public class ResultCompleterRequest {


    /**
     * 主键
     */
    @Schema(description = "主键")
    private Long id;

    /**
     * 学工号
     */
    @Schema(description = "学工号")
    private String completerNo;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String completerName;

    /**
     * 人员类型
     */
    @Schema(description = "人员类型")
    private String completerType;

    /**
     * 院系ID
     */
    @Schema(description = "院系ID")
    private Long completerDeptId;

    /**
     * 院系名称
     */
    @Schema(description = "院系名称")
    private String completerDeptName;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String completerPhone;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱")
    private String completerEmail;

    /**
     * 是否负责人 0否1是
     */
    @Schema(description = "是否负责人 0否1是")
    private Integer completerLeader;

}