package com.pig4cloud.pigx.admin.dto.patentFee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "专利费用报销明细VO")
public class PatentFeeItemVO {

    @Schema(description = "明细ID")
    private Long id;

    @Schema(description = "费用类型")
    private String itemType;

    @Schema(description = "金额（元）")
    private BigDecimal amount;

    @Schema(description = "备注")
    private String remark;
}
