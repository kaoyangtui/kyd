package com.pig4cloud.pigx.admin.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "专家创建请求")
public class ExpertCreateRequest {

    @Schema(description = "专家编码")
    private String code;

    @NotBlank(message = "专家姓名不能为空")
    @Schema(description = "专家姓名")
    private String name;

    @NotBlank(message = "专家照片URL不能为空")
    @Schema(description = "专家照片URL")
    private String photoUrl;

    @NotBlank(message = "专家职称不能为空")
    @Schema(description = "专家职称")
    private String title;

    @NotBlank(message = "所在单位不能为空")
    @Schema(description = "所在单位")
    private String orgName;

    @NotBlank(message = "职务不能为空")
    @Schema(description = "职务")
    private String position;

    @NotBlank(message = "办公室电话不能为空")
    @Schema(description = "办公室电话")
    private String officePhone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @NotBlank(message = "个人简介不能为空")
    @Schema(description = "个人简介")
    private String intro;

    @Schema(description = "教育经历")
    private String eduExp;

    @Schema(description = "工作经历")
    private String workExp;

    @Schema(description = "专利数量")
    private Long patentCnt;

    @Schema(description = "成果数量")
    private Long resultCnt;
}
