package com.pig4cloud.pigx.admin.dto.ipAssign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 创建赋权请求
 */
@Data
@Schema(description = "创建赋权请求")
public class IpAssignCreateRequest {

    @Schema(description = "知识产权类型")
    private String ipType;

    @Schema(description = "知识产权编码")
    private String ipCode;

    @Schema(description = "被赋权人学号")
    private String assignToCode;

    @Schema(description = "被赋权人姓名")
    private String assignToName;

    @Schema(description = "赋权类型")
    private String assignMode;

    @Schema(description = "其它发明人同意证明附件URL，多文件用分号分隔")
    private List<String> proofFileUrl;

    @Schema(description = "赋权申请附件URL，多文件用分号分隔")
    private List<String> attachFileUrl;
}