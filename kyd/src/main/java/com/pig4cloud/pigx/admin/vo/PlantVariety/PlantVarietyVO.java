package com.pig4cloud.pigx.admin.vo.PlantVariety;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "植物新品种权主表请求")
public class PlantVarietyVO {

    private Long id;

    @Schema(description = "品种名称")
    private String name;

    @Schema(description = "品种权号")
    private String rightNo;

    @Schema(description = "属或种")
    private String genusType;

    @Schema(description = "属或种名称")
    private String genusName;

    @Schema(description = "申请时间")
    private String applyDate;

    @Schema(description = "授权时间")
    private String authDate;

    @Schema(description = "校外培育人（多个分号分隔）")
    private String breederOutName;

    @Schema(description = "证书附件URL")
    private String certFileUrl;
}