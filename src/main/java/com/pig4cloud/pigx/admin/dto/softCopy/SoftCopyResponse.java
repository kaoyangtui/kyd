package com.pig4cloud.pigx.admin.dto.softCopy;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pig4cloud.pigx.admin.dto.BaseResponse;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "软著提案返回信息")
public class SoftCopyResponse extends BaseResponse {
    public static final String BIZ_CODE = "SOFT_COPY";


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 业务编码
     */
    @Schema(description = "业务编码")
    private String code;

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

    /**
     * 所属组织ID
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "所属组织ID")
    private Long deptId;

    /**
     * 组织名称
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "组织名称")
    private String deptName;

    /**
     * 软著名称
     */
    @Schema(description = "软著名称")
    private String softName;

    /**
     * 技术领域
     */
    @Schema(description = "技术领域")
    private List<String> techField;

    /**
     * 依托项目 0否1是
     */
    @Schema(description = "依托项目 0否1是")
    private Integer relyProject;

    /**
     * 项目类型
     */
    @Schema(description = "项目类型")
    private String projectType;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String projectName;

    /**
     * 经办人姓名
     */
    @Schema(description = "经办人姓名")
    private String agentName;

    /**
     * 经办人联系方式
     */
    @Schema(description = "经办人联系方式")
    private String agentContact;

    /**
     * 负责人承诺 0否1是
     */
    @Schema(description = "负责人承诺 0否1是")
    private Integer pledge;

    /**
     * 附件路径;分号分隔
     */
    @Schema(description = "附件路径;分号分隔")
    private List<String> attachmentUrls;

    /**
     * 负责人 ID
     */
    @Schema(description = "负责人 ID")
    private String leaderCode;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名")
    private String leaderName;

    @Schema(description = "完成人信息列表")
    private List<CompleterEntity> completers;

    @Schema(description = "著作权人信息列表")
    private List<OwnerEntity> owners;

}
