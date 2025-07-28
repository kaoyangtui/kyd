package com.pig4cloud.pigx.admin.dto.patentMonitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "专利转化监控分页响应")
public class PatentMonitorTransformResponse {
    public static final String BIZ_CODE = "PATENT_MONITOR_TRANSFORM";

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "转化编号")
    private String code;
    @Schema(description = "转化名称")
    private String name;
    @Schema(description = "专利号")
    private String appNumber;
    @Schema(description = "专利名称")
    private String title;
    @Schema(description = "专利类型")
    private String patType;
    @Schema(description = "合同签订时间")
    private LocalDate signDate;
    @Schema(description = "合同到期时间")
    private LocalDate expireDate;
    @Schema(description = "法律状态")
    private String lprs;
    @Schema(description = "权利人")
    private String patentee;
    @Schema(description = "最近变更时间")
    private LocalDate eventTime;
    @Schema(description = "最近变更内容")
    private String eventContent;
    @Schema(description = "最近变更状态")
    private String eventStatus;
}
