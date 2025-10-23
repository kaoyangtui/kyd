package com.pig4cloud.pigx.admin.dto.demand;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;
import com.pig4cloud.pigx.admin.entity.DemandSignupEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Schema(description = "企业需求响应")
@Data
public class DemandResponse extends BaseResponse {

    public static final String BIZ_CODE = "DEMAND";
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "需求名称")
    private String name;
    @Schema(description = "需求分类，1 企业需求 2专项需求")
    private Integer category;
    @Schema(description = "需求类型")
    private String type;
    @Schema(description = "所属领域")
    private List<String> field;
    @Schema(description = "所属领域名称")
    private List<String> fieldName;
    @Schema(description = "有效期开始时间")
    private LocalDate validStart;
    @Schema(description = "有效期结束时间")
    private LocalDate validEnd;
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
    private List<String> companyArea;
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
    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;
    @Schema(description = "上下架时间")
    private LocalDateTime shelfTime;
    @Schema(description = "需求报名信息")
    private List<DemandSignupEntity> demandSignupList;
    @Schema(description = "需求推送信息")
    private List<DemandReceiveEntity> demandReceiveList;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程KEY")
    private String flowKey;

    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;
}