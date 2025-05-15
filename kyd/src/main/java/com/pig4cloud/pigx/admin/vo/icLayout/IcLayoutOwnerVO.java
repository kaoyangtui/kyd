package com.pig4cloud.pigx.admin.vo.icLayout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "集成电路布图权利人信息")
public class IcLayoutOwnerVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "权利人名称")
    private String name;
}
