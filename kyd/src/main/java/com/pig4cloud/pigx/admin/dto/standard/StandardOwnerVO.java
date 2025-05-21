package com.pig4cloud.pigx.admin.dto.standard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 标准起草单位 VO
 */
@Data
@Schema(description = "标准起草单位")
public class StandardOwnerVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "起草单位名称")
    private String name;
}
