package com.pig4cloud.pigx.admin.dto.perf;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "业绩事件")
public class PerfEventDTO {

    @Schema(description = "业务主键")
    private String pid;

    @Schema(description = "事件时间")
    private LocalDateTime eventTime;

    @Schema(description = "知识产权类型编码")
    private String ipTypeCode;

    @Schema(description = "知识产权类型名称")
    private String ipTypeName;

    @Schema(description = "规则事件编码")
    private String ruleEventCode;

    @Schema(description = "规则事件名称")
    private String ruleEventName;

    @Schema(description = "基础分，为空则使用规则分值")
    private BigDecimal baseScore;

    @Schema(description = "参与人列表")
    private List<PerfParticipantDTO> participants;
}
