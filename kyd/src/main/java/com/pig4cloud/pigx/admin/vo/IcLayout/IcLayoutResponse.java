package com.pig4cloud.pigx.admin.vo.IcLayout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "集成电路布图响应")
public class IcLayoutResponse {

    @Schema(description = "主表信息")
    private IcLayoutMainVO main;

    @Schema(description = "校内创作人列表")
    private List<IcLayoutCreatorInVO> creators;

    @Schema(description = "权利人列表")
    private List<IcLayoutOwnerVO> owners;

    public static final String BIZ_CODE = "ic_layout_list";
}
