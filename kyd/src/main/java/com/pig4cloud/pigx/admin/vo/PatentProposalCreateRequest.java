package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增专利提案请求")
public class PatentProposalCreateRequest {
    @Schema(description = "拟申请专利名称")
    private String title;

    @Schema(description = "拟申请专利类型")
    private String type;

    @Schema(description = "技术所属领域")
    private String techField;

    @Schema(description = "计划维持年限")
    private Integer planYears;

    @Schema(description = "是否高价值校内专利")
    private Integer isHighValueInner;

    @Schema(description = "是否转化")
    private Integer isTransform;

    @Schema(description = "是否依托项目")
    private Integer isFromProject;

    @Schema(description = "是否快速预审")
    private Integer isFastTrack;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "申请人信息")
    private String applicantOrgs;

    @Schema(description = "发明人信息（校内）")
    private String inventorsInner;

    @Schema(description = "发明人（校外）")
    private String inventorsOuter;

    @Schema(description = "摘要")
    private String abstractText;

    @Schema(description = "权利要求")
    private String claimsText;

    @Schema(description = "说明书")
    private String descriptionText;

    @Schema(description = "是否承诺")
    private Integer isPromise;

    @Schema(description = "是否代理")
    private Integer isAgency;

    @Schema(description = "代理机构名称")
    private String agencyName;

    @Schema(description = "快速预审附件")
    private String fastTrackFile;

    @Schema(description = "权利要求书附件")
    private String claimsFile;

    @Schema(description = "说明书附件")
    private String descriptionFile;

    @Schema(description = "说明书附图")
    private String descFigureFile;

    @Schema(description = "摘要附图")
    private String abstractFigureFile;
}