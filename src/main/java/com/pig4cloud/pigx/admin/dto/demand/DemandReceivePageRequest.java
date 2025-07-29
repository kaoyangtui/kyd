package com.pig4cloud.pigx.admin.dto.demand;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "企业需求接收分页查询请求")
public class DemandReceivePageRequest extends BasePageQuery {

    @Schema(description = "需求名称")
    private String name;

    @Schema(description = "需求类型")
    private String type;

    @Schema(description = "所属领域")
    private String field;

    @Schema(description = "创建人ID")
    private String createUserId;

    @Schema(description = "提交时间起")
    private String startTime;

    @Schema(description = "提交时间止")
    private String endTime;
}
