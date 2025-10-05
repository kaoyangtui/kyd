package com.pig4cloud.pigx.admin.dto.patentFee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "专利费用报销响应")
public class PatentFeeReimburseResponse {

    public static final String BIZ_CODE = "PATENT_FEE_REIMBURSE";

    @Schema(description = "主键ID")
    private Long id;

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

    @Schema(description = "知识产权类型")
    private String ipType;

    @Schema(description = "知识产权编码，多个以分号分隔")
    private List<String> ipCode;

    @Schema(description = "专利申请号")
    private String appNumber;

    @Schema(description = "专利名称")
    private String title;

    @Schema(description = "专利类型")
    private String patType;

    @Schema(description = "专利类型名称")
    private String patTypeName;

    @Schema(description="关联提案编码")
    private String proposalCode;

    @Schema(description = "项目ID")
    private Long researchProjectId;

    @Schema(description = "项目类型")
    private String researchProjectType;

    @Schema(description = "项目名称")
    private String researchProjectName;

    @Schema(description = "报销经费项目名称")
    private String fundItemName;

    @Schema(description = "评估等级")
    private String evalLevel;

    @Schema(description = "专利受理通知书URL")
    private List<String> acceptFileUrl;

    @Schema(description = "委托代理合同URL")
    private List<String> agentContractUrl;

    @Schema(description = "专利申请说明书URL")
    private List<String> appBookUrl;

    @Schema(description = "项目合同或任务书URL")
    private List<String> contractUrl;

    @Schema(description = "费用明细")
    private List<PatentFeeItemVO> feeItems;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "所属院系")
    private String deptName;
}
