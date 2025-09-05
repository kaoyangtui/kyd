package com.pig4cloud.pigx.admin.dto.patentMonitor;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利转化监控分页查询请求")
public class PatentMonitorTransformPageRequest extends BasePageQuery {
    @Schema(description = "转化项目名称/编号")
    private String transformKeyword;
    @Schema(description = "专利号/名称/申请人")
    private String patentKeyword;
    // 其他可加条件：签订/到期时间区间、状态等
}
