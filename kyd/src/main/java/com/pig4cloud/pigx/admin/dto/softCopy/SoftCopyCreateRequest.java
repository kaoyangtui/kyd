package com.pig4cloud.pigx.admin.dto.softCopy;

import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 软著提案新增请求 DTO
 *
 * @author zhaoliang
 */
@Data
@Schema(description = "软著提案新增请求")
public class SoftCopyCreateRequest {

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

    @Schema(description = "附件路径;分号分隔")
    private List<String> attachmentUrls;

    @Schema(description = "完成人信息列表")
    private List<CompleterEntity> completers;

    @Schema(description = "著作权人信息列表")
    private List<OwnerEntity> owners;
}