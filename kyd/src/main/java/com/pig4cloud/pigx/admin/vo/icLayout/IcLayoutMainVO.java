package com.pig4cloud.pigx.admin.vo.icLayout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "集成电路布图主表请求")
public class IcLayoutMainVO {

    @Schema(description = "主键 ID")
    private Long id;

    @Schema(description = "集成电路布图名称")
    private String name;

    @Schema(description = "登记号")
    private String regNo;

    @Schema(description = "申请时间")
    private String applyDate;

    @Schema(description = "公告时间")
    private String publishDate;

    @Schema(description = "集成电路布图证书URL")
    private String certFileUrl;

    @Schema(description = "校外创作人姓名，多个以 ; 分隔")
    private String creatorOutName;
}
