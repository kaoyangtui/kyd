package com.pig4cloud.pigx.admin.jsonflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "FlowListResponse")
public class MyFlowResponse {

    public static final String BIZ_CODE = "MY_FLOW";

    @Schema(description = "编码")
    private String code;

    @Schema(description = "流程业务Key")
    private String flowKey;

    @Schema(description = "流程业务名称")
    private String flowName;

    @Schema(description = "工单名称")
    private String orderName;

    @Schema(description = "提交人姓名")
    private String createUserName;

    @Schema(description = "所属院系")
    private String deptName;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    @Schema(description = "流程状态")
    private Integer status;

    @Schema(description = "流程节点名")
    private String nodeName;
}
