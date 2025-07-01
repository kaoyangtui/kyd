package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 专利上下架
 *
 * @author pigx
 * @date 2025-06-03 22:04:46
 */
@Data
@Schema(description = "专利上下架")
public class PatentShelfRequest {


    /**
     * 专利PID
     */
    @Schema(description = "专利PID")
    private String pid;

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

    /**
     * 拟合作方式
     */
    @Schema(description = "拟合作方式")
    private String cooperationMode;

    /**
     * 拟交易金额，-1为面议
     */
    @Schema(description = "拟交易金额，-1为面议")
    private BigDecimal cooperationAmount;
}