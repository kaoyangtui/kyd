package com.pig4cloud.pigx.admin.dto.ipAssign;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 响应结构：赋权主信息封装
 */
@Data
@Schema(description = "赋权响应")
public class IpAssignResponse extends BaseResponse {

    public static final String BIZ_CODE = "IP_ASSIGN";
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "业务编码")
    private String code;
    @Schema(description = "流程状态")
    private Integer flowStatus;
    @Schema(description = "当前流程节点")
    private String currentNodeName;
    @Schema(description = "知识产权类型")
    private String ipType;
    @Schema(description = "知识产权编码")
    private String ipCode;
    @Schema(description = "被赋权人学号")
    private String assignToCode;
    @Schema(description = "赋权类型")
    private String assignMode;
    @Schema(description = "其它发明人同意证明附件URL（已分号分隔）")
    private List<String> proofFileUrl;
    @Schema(description = "赋权申请附件URL（已分号分隔）")
    private List<String> attachFileUrl;

}