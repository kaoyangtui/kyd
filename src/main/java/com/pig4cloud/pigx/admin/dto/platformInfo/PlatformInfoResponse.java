package com.pig4cloud.pigx.admin.dto.platformInfo;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "关于平台内容响应")
public class PlatformInfoResponse extends BaseResponse {

    public static final String BIZ_CODE = "PLATFORM_INFO";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

}
