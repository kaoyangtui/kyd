package com.pig4cloud.pigx.admin.dto.icLayout;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "集成电路布图登记新增请求")
public class IcLayoutCreateRequest {

    @Schema(description = "集成电路布图名称")
    private String name;

    @Schema(description = "登记号")
    private String regNo;

    @Schema(description = "申请时间")
    private LocalDate applyDate;

    @Schema(description = "公告时间")
    private LocalDate publishDate;

    @Schema(description = "证书附件")
    private List<String> certFileUrl;

    @Schema(description = "校外创作人，多个用;分隔")
    private List<String> creatorOutName;

    @Schema(description = "完成人信息")
    private List<CompleterEntity> completers;

    @Schema(description = "权利人信息")
    private List<OwnerEntity> owners;
}