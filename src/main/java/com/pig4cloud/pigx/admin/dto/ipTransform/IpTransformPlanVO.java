package com.pig4cloud.pigx.admin.dto.ipTransform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 知识产权转化分配方案表
 *
 * @author pigx
 * @date 2025-07-30 12:02:09
 */
@Data
@Schema(description = "知识产权转化分配方案")
public class IpTransformPlanVO {

    /**
     * 项目代码
     */
    @Schema(description = "项目代码")
    private String transformCode;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String projectName;

    /**
     * 分配比例（%）
     */
    @Schema(description = "分配比例（%）")
    private BigDecimal ratio;

    /**
     * 发放金额（万元）
     */
    @Schema(description = "发放金额（万元）")
    private BigDecimal amount;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}