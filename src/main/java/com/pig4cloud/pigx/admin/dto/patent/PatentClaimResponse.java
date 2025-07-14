package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "专利认领响应")
public class PatentClaimResponse {

    public static final String BIZ_CODE = "PATENT_CLAIM";

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "认领编码")
    private String code;

    @Schema(description = "申请号")
    private String appNumber;

    @Schema(description = "名称")
    private String title;

    @Schema(description = "发明人")
    private String inventorName;

    @Schema(description = "流程实例ID")
    private String flowInstId;

    @Schema(description = "流程KEY")
    private String flowKey;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "创建/提交时间")
    private LocalDateTime createTime;

    @Schema(description = "创建/提交人")
    private String createBy;

    @Schema(description = "所属组织ID")
    private Long deptId;

    @Schema(description = "组织名称")
    private String deptName;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "删除标识")
    private String delFlag;

    @Schema(description = "租户")
    private Long tenantId;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人姓名")
    private String createUserName;
}
