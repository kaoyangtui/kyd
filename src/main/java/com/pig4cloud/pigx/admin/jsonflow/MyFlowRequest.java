package com.pig4cloud.pigx.admin.jsonflow;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "FlowListPageRequest")
public class MyFlowRequest extends BasePageQuery {
    @Schema(description = "编码")
    private String code;

    @Schema(description = "流程业务Key")
    private String flowKey;

    @Schema(description = "工单名称")
    private String orderName;

    @Schema(description = "提交人ID")
    private Long createUser;

    @Schema(description = "提交人姓名")
    private String createUserName;

    @Schema(description = "所属院系")
    private String deptName;

    @Schema(description = "流程状态 -2撤回 -1发起 0运行中 1完结 2其他")
    private Integer status;

    @Schema(description = "提交时间开始")
    private LocalDateTime startTime;

    @Schema(description = "提交时间结束")
    private LocalDateTime endTime;
}
