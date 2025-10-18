package com.pig4cloud.pigx.admin.dto.demandIn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 新增请求
 */
@Data
@Schema(description = "新增校内需求")
public class DemandInCreateRequest {

    @Schema(description = "需求名称")
    private String name;

    @Schema(description = "需求类型")
    private String type;

    @Schema(description = "上下架状态（0 下架，1 上架）")
    private Integer shelfStatus;

    @Schema(description = "所属领域")
    private List<String> field;

    @Schema(description = "有效期开始时间")
    private LocalDate validStart;

    @Schema(description = "有效期结束时间")
    private LocalDate validEnd;

    @Schema(description = "需求描述")
    private String description;

    @Schema(description = "需求标签，多值用分号分隔")
    private List<String> tags;

    @Schema(description = "需求附件URL列表")
    private List<String> attachFileUrl;
}