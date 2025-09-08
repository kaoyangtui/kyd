package com.pig4cloud.pigx.admin.dto.patent;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利认领分页查询请求")
public class PatentClaimPageRequest extends BasePageQuery {

    @Schema(description = "认领编码")
    private String code;

    @Schema(description = "名称")
    private String title;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "流程实例ID")
    private String flowInstId;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "申请号")
    private String appNumber;

    @Schema(description = "发明人")
    private String inventorName;

    @Schema(description = "所属组织ID")
    private Long deptId;

    @Schema(description = "组织名称")
    private String deptName;

    @Schema(description = "创建/提交时间起")
    private String beginTime;

    @Schema(description = "创建/提交时间止")
    private String endTime;
}
