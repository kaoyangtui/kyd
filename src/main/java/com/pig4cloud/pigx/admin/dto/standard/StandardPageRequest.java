package com.pig4cloud.pigx.admin.dto.standard;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "标准分页查询请求")
public class StandardPageRequest extends BasePageQuery {

    @Schema(description = "关键字（标准号或名称）")
    private String keyword;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点")
    private String currentNodeName;

    @Schema(description = "院系 ID")
    private String deptId;

    @Schema(description = "开始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

}