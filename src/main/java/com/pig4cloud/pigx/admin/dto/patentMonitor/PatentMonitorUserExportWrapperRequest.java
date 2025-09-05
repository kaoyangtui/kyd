package com.pig4cloud.pigx.admin.dto.patentMonitor;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利用户监控导出请求")
public class PatentMonitorUserExportWrapperRequest extends ExportWrapperRequest<PatentMonitorUserPageRequest> {
    // 可以自定义导出字段 keys、模式等参数（如果有需要）
}
