package com.pig4cloud.pigx.admin.dto.standard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 标准信息响应
 */
@Data
@Schema(description = "标准信息响应")
public class StandardResponse {

    @Schema(description = "主表信息")
    private StandardMainVO main;

    @Schema(description = "起草单位列表")
    private List<StandardOwnerVO> owners;

    @Schema(description = "校内起草人列表")
    private List<StandardDrafterInVO> drafters;

    public static final String BIZ_CODE = "standard_list";
}
