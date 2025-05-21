package com.pig4cloud.pigx.admin.dto.softCopy;

import com.pig4cloud.pigx.admin.entity.SoftCopyCompleterEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyOwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "软著提案返回信息")
public class SoftCopyResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程 KEY")
    private String flowKey;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "软著名称")
    private String softName;

    @Schema(description = "技术领域")
    private String techField;

    @Schema(description = "是否依托项目（0否1是）")
    private Integer relyProject;

    @Schema(description = "项目类型")
    private String projectType;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "经办人姓名")
    private String agentName;

    @Schema(description = "经办人联系方式")
    private String agentContact;

    @Schema(description = "负责人承诺（0否1是）")
    private Integer pledge;

    @Schema(description = "附件路径，分号分隔")
    private String attachmentUrls;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "完成人列表")
    private List<SoftCopyCompleterEntity> completers;

    @Schema(description = "著作权人列表")
    private List<SoftCopyOwnerEntity> owners;

    public static final String BIZ_CODE = "soft_copy_list";
}
