package com.pig4cloud.pigx.admin.dto.ipAssign;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 分页查询请求（继承通用分页）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "分页查询赋权请求")
public class IpAssignPageRequest extends BasePageQuery {

    @Schema(description = "关键词：匹配编码、被赋权人")
    private String keyword;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "当前流程节点")
    private String currentNodeName;

    @Schema(description = "创建人所属院系")
    private String createByDept;

    @Schema(description = "起始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

}