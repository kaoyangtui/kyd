package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分配结果")
public class PerfShareDTO {

    @Schema(description = "参与人")
    private PerfParticipantDTO participant;

    @Schema(description = "分配比例 0~1")
    private BigDecimal ratio;
}
