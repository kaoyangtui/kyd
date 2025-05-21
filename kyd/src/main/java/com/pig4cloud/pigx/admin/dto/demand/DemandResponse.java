package com.pig4cloud.pigx.admin.dto.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "企业需求响应")
@Data
public class DemandResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "需求名称")
    private String name;

    @Schema(description = "需求分类，1 企业需求 2专项需求")
    private Integer category;

    @Schema(description = "需求类型")
    private String type;

    @Schema(description = "所属领域")
    private String field;

    @Schema(description = "需求有效期")
    private LocalDate validUntil;

    @Schema(description = "预算金额（万元）")
    private BigDecimal budget;

    @Schema(description = "需求摘要")
    private String description;

    @Schema(description = "需求标签；多个用分号分隔")
    private List<String> tags;

    @Schema(description = "报名开始时间")
    private LocalDateTime signUpStart;

    @Schema(description = "报名截止时间")
    private LocalDateTime signUpEnd;

    @Schema(description = "企业名称")
    private String companyName;

    @Schema(description = "企业类别")
    private String companyType;

    @Schema(description = "企业所属地区")
    private String companyArea;

    @Schema(description = "企业地址")
    private String companyAddr;

    @Schema(description = "企业简介")
    private String companyIntro;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系人电话")
    private String contactPhone;

    @Schema(description = "联系人职务")
    private String contactTitle;

    @Schema(description = "联系人邮箱")
    private String contactEmail;

    @Schema(description = "企业需求附件")
    private List<String> attachFileUrl;

    public static final String BIZ_CODE = "demand_list";
}