package com.pig4cloud.pigx.admin.dto.patentMonitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "专利用户监控分页响应")
public class PatentMonitorUserResponse {

    public static final String BIZ_CODE = "PATENT_MONITOR_USER";

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "申请号")
    private String appNumber;

    @Schema(description = "专利名称")
    private String title;

    @Schema(description = "法律状态")
    private String lprs;

    @Schema(description = "最近变更时间")
    private LocalDate eventTime;

    @Schema(description = "最近变更内容")
    private String eventContent;

    @Schema(description = "最近变更状态")
    private String eventStatus;
}
