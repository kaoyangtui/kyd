package com.pig4cloud.pigx.admin.vo.demandIn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 响应对象
 */
@Data
@Schema(description = "校内需求响应")
public class DemandInResponse {

    @Schema(description = "主键 ID")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点名称")
    private String currentNodeName;

    @Schema(description = "需求名称")
    private String name;

    @Schema(description = "需求类型")
    private String type;

    @Schema(description = "上下架状态（0 下架，1 上架）")
    private Integer shelfStatus;

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

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "所属院系")
    private String deptId;

    public static final String BIZ_CODE = "demand_in_list";
}