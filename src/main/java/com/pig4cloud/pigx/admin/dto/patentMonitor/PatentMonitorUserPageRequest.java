package com.pig4cloud.pigx.admin.dto.patentMonitor;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "专利用户监控分页查询请求")
public class PatentMonitorUserPageRequest extends BasePageQuery {
    @Schema(description = "专利号/名称/申请人 模糊搜索")
    private String keyword;
}
