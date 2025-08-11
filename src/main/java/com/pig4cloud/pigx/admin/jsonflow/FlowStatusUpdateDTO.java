package com.pig4cloud.pigx.admin.jsonflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class FlowStatusUpdateDTO {

    /**
     * 流程实例 ID
     */
    @Schema(description = "流程实例 ID")
    private String flowInstId;

    /**
     * 流程KEY
     */
    @Schema(description = "流程KEY")
    private String flowKey;

    /**
     * 流程状态：-2暂存 -1运行中 0完成 1作废 2撤回
     */
    @Schema(description = "流程状态：-2暂存 -1运行中 0完成 1作废 2撤回")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;
}