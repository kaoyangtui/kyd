package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 软著提案主表信息请求
 */
@Data
@Schema(description = "软著提案主表信息")
public class SoftCopyMainRequest {

    @Schema(description = "软著名称")
    private String softName;

    @Schema(description = "技术领域")
    private String techField;

    @Schema(description = "是否依托项目")
    private Integer relyProject;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "经办人姓名")
    private String agentName;

    @Schema(description = "经办人联系方式")
    private String agentContact;

    @Schema(description = "负责人承诺")
    private Integer pledge;

    @Schema(description = "附件路径，分号分隔")
    private String attachmentUrls;
}