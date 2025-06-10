package com.pig4cloud.pigx.admin.dto.commonDownload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "常用下载新增请求")
public class CommonDownloadCreateRequest {

    @NotBlank(message = "文件名不能为空")
    @Schema(description = "文件名")
    private String fileName;

    @NotBlank(message = "文件地址不能为空")
    @Schema(description = "文件地址")
    private String filePath;

    @NotBlank(message = "内容说明不能为空")
    @Schema(description = "内容说明")
    private String content;

    @Schema(description = "附件URL，多个用;分隔")
    private List<String> fileUrl;

}
