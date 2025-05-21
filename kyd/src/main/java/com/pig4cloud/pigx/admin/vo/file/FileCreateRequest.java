package com.pig4cloud.pigx.admin.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文档管理 DTO
 * @author zhaoliang
 */
@Data
public class FileCreateRequest {

    @Schema(description = "编号")
    private String code;

    @Schema(description = "申请类型")
    private String applyType;

    @Schema(description = "主题名称")
    private String subjectName;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "下载文件名")
    private String downloadName;
}