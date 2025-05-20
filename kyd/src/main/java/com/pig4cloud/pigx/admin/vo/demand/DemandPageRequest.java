package com.pig4cloud.pigx.admin.vo.demand;

import com.pig4cloud.pigx.admin.vo.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@Schema(description = "企业需求分页查询请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class DemandPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持名称模糊）")
    private String keyword;

    @Schema(description = "需求分类，1 企业需求 2专项需求")
    private Integer category;

    @Schema(description = "需求类型")
    private String type;

    @Schema(description = "所属领域")
    private String field;

    @Schema(description = "提交人所在院系")
    private String createByDept;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点")
    private String currentNodeName;

    @Schema(description = "提交起始时间")
    private String beginTime;

    @Schema(description = "提交结束时间")
    private String endTime;
}