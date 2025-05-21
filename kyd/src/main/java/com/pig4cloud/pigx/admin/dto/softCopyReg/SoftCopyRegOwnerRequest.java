package com.pig4cloud.pigx.admin.dto.softCopyReg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 软著登记著作权人信息
 *
 * @author zhaoliang
 */
@Data
@Schema(description = "软著登记著作权人信息")
public class SoftCopyRegOwnerRequest {

    @Schema(description = "著作权人名称")
    private String name;

    @Schema(description = "著作权人类型（第一著作权人、其他著作权人）")
    private String type;
}
