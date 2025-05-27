package com.pig4cloud.pigx.admin.dto.commonDownload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "常用下载响应")
public class CommonDownloadResponse {

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

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "所属院系ID")
    private String deptId;
}
