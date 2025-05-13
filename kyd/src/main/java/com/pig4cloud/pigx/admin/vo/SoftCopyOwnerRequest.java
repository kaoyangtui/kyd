package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 软著提案著作权人信息
 */
@Data
@Schema(description = "软著提案著作权人信息")
public class SoftCopyOwnerRequest {

    @Schema(description = "著作权人名称")
    private String ownerName;

    @Schema(description = "著作权人类型 0其他1第一")
    private Integer ownerType;
}