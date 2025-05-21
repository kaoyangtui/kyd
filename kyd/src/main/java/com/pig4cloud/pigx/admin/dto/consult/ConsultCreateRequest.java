package com.pig4cloud.pigx.admin.dto.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "咨询信息创建请求")
public class ConsultCreateRequest {

    @Schema(description = "咨询类型")
    private String type;

    @Schema(description = "咨询目标编码")
    private String targetCode;

    @Schema(description = "咨询目标名称")
    private String targetName;

    @Schema(description = "咨询内容")
    private String content;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "前台用户 ID")
    private Long appUserId;
}
