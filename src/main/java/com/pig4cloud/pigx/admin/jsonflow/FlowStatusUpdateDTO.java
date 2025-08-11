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
     * 流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止
     */
    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;
}