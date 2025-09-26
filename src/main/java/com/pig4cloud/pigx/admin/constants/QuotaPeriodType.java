package com.pig4cloud.pigx.admin.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "额度统计周期类型")
public enum QuotaPeriodType {

    @Schema(description = "总额度（不区分周期，永久累计）")
    TOTAL,

    @Schema(description = "按日限额（自然日 00:00~23:59）")
    DAILY,

    @Schema(description = "按月限额（自然月 1 日至月末）")
    MONTHLY
}
