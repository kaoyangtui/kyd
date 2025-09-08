package com.pig4cloud.pigx.admin.dto.plantVariety;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "植物新品种权分页查询请求")
public class PlantVarietyPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持名称/编号模糊搜索）")
    private String keyword;

    @Schema(description = "所属组织ID")
    private String deptId;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "流程节点名称")
    private String currentNodeName;

    @Schema(description = "申请时间-开始")
    private String beginTime;

    @Schema(description = "申请时间-结束")
    private String endTime;
}
