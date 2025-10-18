package com.pig4cloud.pigx.admin.dto.demandIn;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 响应对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "校内需求响应")
public class DemandInResponse extends BaseResponse {

    public static final String BIZ_CODE = "DEMAND_IN";
    @Schema(description = "主键 ID")
    private Long id;
    @Schema(description = "业务编码")
    private String code;
    @Schema(description = "流程实例 ID")
    private String flowInstId;
    @Schema(description = "流程KEY")
    private String flowKey;
    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;
    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;
    @Schema(description = "需求名称")
    private String name;
    @Schema(description = "需求类型")
    private String type;
    /**
     * 上下架状态，0下架1上架
     */
    @Schema(description = "上下架状态，0下架1上架")
    private Integer shelfStatus;
    /**
     * 上下架时间
     */
    @Schema(description = "上下架时间")
    private LocalDateTime shelfTime;
    @Schema(description = "所属领域")
    private List<String> field;
    @Schema(description = "有效期开始时间")
    private LocalDate validStart;
    @Schema(description = "有效期结束时间")
    private LocalDate validEnd;
    @Schema(description = "需求描述")
    private String description;
    @Schema(description = "需求标签")
    private List<String> tags;
    @Schema(description = "需求附件URL")
    private List<String> attachFileUrl;
}