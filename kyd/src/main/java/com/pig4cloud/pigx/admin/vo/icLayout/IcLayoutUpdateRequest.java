package com.pig4cloud.pigx.admin.vo.icLayout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "集成电路布图更新请求")
public class IcLayoutUpdateRequest {

    @Schema(description = "主表信息")
    private IcLayoutMainVO main;

    @Schema(description = "校内创作人列表")
    private List<IcLayoutCreatorInVO> creators;

    @Schema(description = "权利人列表")
    private List<IcLayoutOwnerVO> owners;
}
