package com.pig4cloud.pigx.admin.dto.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "科研成果上下架请求")
public class ResultShelfRequest {

    @Schema(description = "成果ID")
    private Long id;

    @Schema(description = "目标上下架状态（0-下架 1-上架）")
    private Integer shelfStatus;

    @Schema(description = "领域技术")
    private String techArea;

    @Schema(description = "标签")
    private List<String> tags;

    @Schema(description = "合作方式")
    private List<String> transWay;

    @Schema(description = "拟交易价格(万元)")
    private BigDecimal transPrice;


}
