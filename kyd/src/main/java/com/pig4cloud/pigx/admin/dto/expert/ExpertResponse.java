package com.pig4cloud.pigx.admin.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "专家返回数据")
public class ExpertResponse {

    public static final String BIZ_CODE = "EXPERT";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "专家编码")
    private String code;

    @Schema(description = "专家姓名")
    private String name;

    @Schema(description = "专家照片URL")
    private String photoUrl;

    @Schema(description = "专家职称")
    private String title;

    @Schema(description = "所在单位")
    private String orgName;

    @Schema(description = "职务")
    private String position;

    @Schema(description = "办公室电话")
    private String officePhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "个人简介")
    private String intro;

    @Schema(description = "教育经历")
    private String eduExp;

    @Schema(description = "工作经历")
    private String workExp;

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

    @Schema(description = "院系名称")
    private String deptName;
}
