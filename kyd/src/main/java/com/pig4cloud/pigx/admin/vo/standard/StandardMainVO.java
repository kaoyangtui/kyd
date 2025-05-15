package com.pig4cloud.pigx.admin.vo.standard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 标准信息主表 VO
 */
@Data
@Schema(description = "标准信息主表")
public class StandardMainVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标准名称")
    private String name;

    @Schema(description = "标准号")
    private String code;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程KEY")
    private String flowKey;

    @Schema(description = "流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "主管部门")
    private String dept;

    @Schema(description = "发布时间")
    private String pubDate;

    @Schema(description = "实施时间")
    private String implDate;

    @Schema(description = "校外起草人姓名，多个用;分隔")
    private String drafterOutName;

    @Schema(description = "标准文本URL")
    private String fileUrl;
}
