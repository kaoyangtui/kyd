package com.pig4cloud.pigx.admin.dto.commonDownload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "常用下载修改请求")
public class CommonDownloadUpdateRequest extends CommonDownloadCreateRequest {

    @Schema(description = "主键ID")
    private Long id;

}
