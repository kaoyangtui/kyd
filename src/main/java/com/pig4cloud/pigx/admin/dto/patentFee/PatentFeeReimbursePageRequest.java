package com.pig4cloud.pigx.admin.dto.patentFee;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利费用报销分页查询请求")
public class PatentFeeReimbursePageRequest extends BasePageQuery {

    @Schema(description = "专利申请号")
    private String appNumber;

    @Schema(description = "专利名称")
    private String title;

    @Schema(description="关联提案编码")
    private String proposalCode;

    @Schema(description = "专利类型")
    private String patType;

    @Schema(description = "提交人姓名")
    private String createBy;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "提交时间起")
    private String startTime;

    @Schema(description = "提交时间止")
    private String endTime;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;
}
