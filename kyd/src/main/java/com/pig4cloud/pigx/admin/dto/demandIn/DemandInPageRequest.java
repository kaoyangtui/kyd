package com.pig4cloud.pigx.admin.dto.demandIn;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "分页查询校内需求")
public class DemandInPageRequest extends BasePageQuery {

    @Schema(description = "关键词（名称）")
    private String keyword;

    @Schema(description = "所属领域")
    private String field;

    @Schema(description = "提交人所属院系")
    private String createByDept;

    @Schema(description = "上下架状态（0 下架，1 上架）")
    private Integer shelfStatus;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点名称")
    private String currentNodeName;

    @Schema(description = "提交时间-起")
    private String beginTime;

    @Schema(description = "提交时间-止")
    private String endTime;

}