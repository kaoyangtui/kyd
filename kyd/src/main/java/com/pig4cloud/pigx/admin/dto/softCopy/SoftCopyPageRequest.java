package com.pig4cloud.pigx.admin.dto.softCopy;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaoliang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "软著提案分页查询请求")
public class SoftCopyPageRequest extends BasePageQuery {

    @Schema(description = "关键词（编码、名称模糊匹配）")
    private String keyword;

    @Schema(description = "流程实例ID")
    private String flowInstId;

    @Schema(description = "流程Key")
    private String flowKey;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "软著名称")
    private String softName;

    @Schema(description = "技术领域")
    private String techField;

    @Schema(description = "是否依托项目 0否1是")
    private Integer relyProject;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "所属院系 ID")
    private String deptId;

    @Schema(description = "提交时间起（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "提交时间止（yyyy-MM-dd）")
    private String endTime;
}
