package com.pig4cloud.pigx.admin.dto.patentFee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "修改专利费用报销请求")
public class PatentFeeReimburseUpdateRequest extends PatentFeeReimburseCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}
