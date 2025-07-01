package com.pig4cloud.pigx.admin.dto.commonDownload;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "常用下载响应")
public class CommonDownloadResponse extends BaseResponse {

    public static final String BIZ_CODE = "COMMON_DOWNLOAD";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件地址")
    private String filePath;

    @Schema(description = "内容说明")
    private String content;

    @Schema(description = "附件URL，多个用;分隔")
    private List<String> fileUrl;

}
