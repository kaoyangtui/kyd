package com.pig4cloud.pigx.admin.dto.demand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "企业需求接收信息响应")
public class DemandReceiveResponse {

    public static final String BIZ_CODE = "DEMAND_RECEIVE";

    @Schema(description = "接收记录ID")
    private Long id;

    @Schema(description = "需求ID")
    private Long demandId;

    @Schema(description = "需求名称")
    private String name;

    @Schema(description = "需求类型")
    private String type;

    @Schema(description = "所属领域")
    private String field;

    @Schema(description = "有效期开始时间")
    private LocalDate validStart;

    @Schema(description = "有效期结束时间")
    private LocalDate validEnd;

    @Schema(description = "预算金额（万元）")
    private BigDecimal budget;

    @Schema(description = "需求摘要")
    private String description;

    @Schema(description = "提交人姓名")
    private String createUserName;

    @Schema(description = "所属院系")
    private String deptName;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    @Schema(description = "已读（0否，1是）")
    private Integer readFlag;
}
