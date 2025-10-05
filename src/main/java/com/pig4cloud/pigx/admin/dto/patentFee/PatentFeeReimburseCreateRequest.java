package com.pig4cloud.pigx.admin.dto.patentFee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "新增专利费用报销请求")
public class  PatentFeeReimburseCreateRequest {

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

    @Schema(description="关联提案编码")
    private String proposalCode;

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
}
