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
    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;
    @Schema(description = "流程节点名称")
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
    private String field;
    @Schema(description = "需求有效期")
    private LocalDate validUntil;
    @Schema(description = "需求描述")
    private String description;
    @Schema(description = "需求标签")
    private List<String> tags;
    @Schema(description = "需求附件URL")
    private List<String> attachFileUrl;
}