package com.pig4cloud.pigx.admin.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileResponse {

    public static final String BIZ_CODE = "FILE";
    @Schema(description = "主键")
    private Long id;
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
    @Schema(description = "提交人")
    private String createBy;
    @Schema(description = "所属院系")
    private String deptId;
    @Schema(description = "提交时间")
    private LocalDateTime createTime;
}