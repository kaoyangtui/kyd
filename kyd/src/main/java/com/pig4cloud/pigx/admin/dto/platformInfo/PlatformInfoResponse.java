package com.pig4cloud.pigx.admin.dto.platformInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "关于平台内容响应")
public class PlatformInfoResponse {

    public static final String BIZ_CODE = "PLATFORM_INFO";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "更新人")
    private String updateBy;
}
