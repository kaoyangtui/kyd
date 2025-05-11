package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "科研成果分页查询参数")
public class ResultPageRequest {

    @Schema(description = "成果编号或名称（模糊匹配）")
    private String keyword;

    @Schema(description = "所属学科")
    private String subject;

    @Schema(description = "所属院系")
    private String createByDept;

    @Schema(description = "上下架状态：0-下架 1-上架")
    private Integer shelfStatus;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程节点")
    private String currentNodeName;

    @Schema(description = "开始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "页码")
    private long current = 1;

    @Schema(description = "每页大小")
    private long size = 10;
}
