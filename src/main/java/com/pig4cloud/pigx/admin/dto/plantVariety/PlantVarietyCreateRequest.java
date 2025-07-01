package com.pig4cloud.pigx.admin.dto.plantVariety;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "植物新品种权登记新增请求")
public class PlantVarietyCreateRequest {

    @Schema(description = "品种名称")
    private String name;

    @Schema(description = "品种权号")
    private String rightNo;

    @Schema(description = "属或种")
    private String genusType;

    @Schema(description = "属或种名称")
    private String genusName;

    @Schema(description = "申请时间")
    private Date applyDate;

    @Schema(description = "授权时间")
    private Date authDate;

    @Schema(description = "校外培育人姓名，多个用;分隔")
    private String breederOutName;

    @Schema(description = "证书附件URL")
    private List<String> certFileUrl;

    @Schema(description = "权利人")
    private List<OwnerEntity> owners;

    @Schema(description = "完成人")
    private List<CompleterEntity> completers;
}
