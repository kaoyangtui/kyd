package com.pig4cloud.pigx.admin.dto.icLayout;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "集成电路布图登记分页请求")
public class IcLayoutPageRequest extends BasePageQuery {

    @Schema(description = "关键字：名称或登记号")
    private String keyword;

    @Schema(description = "开始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点")
    private String currentNodeName;

    @Schema(description = "院系 ID")
    private String deptId;

}
