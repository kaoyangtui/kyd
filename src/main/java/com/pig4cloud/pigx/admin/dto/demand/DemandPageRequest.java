package com.pig4cloud.pigx.admin.dto.demand;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> type;

    @Schema(description = "技术领域")
    private List<String> field;

    @Schema(description = "提交人所在院系")
    private String createByDept;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "当前流程节点")
    private String currentNodeName;

    @Schema(description = "提交起始时间")
    private String beginTime;

    @Schema(description = "提交结束时间")
    private String endTime;

    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;

    @Schema(description = "门户用户 ID")
    private Long userId;
}