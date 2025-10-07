package com.pig4cloud.pigx.admin.dto.patentProposal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pig4cloud.pigx.admin.dto.BaseResponse;
import com.pig4cloud.pigx.admin.dto.patentPreEval.PatentPreEvalResponse;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "专利提案响应对象")
public class PatentProposalResponse extends BaseResponse {

    public static final String BIZ_CODE = "PATENT_PROPOSAL";

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务编码")
    private String code;

    @Schema(description = "流程实例 ID")
    private String flowInstId;

    @Schema(description = "流程KEY")
    private String flowKey;

    @Schema(description = "流程状态：-2撤回 -1发起 0运行中 1完结 2作废 3终止")
    private Integer flowStatus;

    @Schema(description = "流程状态触发时间")
    private LocalDateTime flowStatusTime;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "拟申请专利名称")
    private String title;

    @Schema(description = "拟申请专利类型")
    private String type;

    @Schema(description = "技术所属领域")
    private List<String> techField;

    @Schema(description = "计划维持年限")
    private Integer planYears;

    @Schema(description = "是否高价值校内专利")
    private Integer isHighValueInner;

    @Schema(description = "是否转化")
    private Integer isTransform;

    @Schema(description = "是否依托项目")
    private Integer isFromProject;

    @Schema(description = "是否快速预审")
    private Integer isFastTrack;

    @Schema(description = "项目ID")
    private Long researchProjectId;

    @Schema(description = "项目类型")
    private String researchProjectType;

    @Schema(description = "项目名称")
    private String researchProjectName;

    @Schema(description = "申请人信息")
    private List<OwnerEntity> owners;

    @Schema(description = "发明人信息（校内）")
    private List<CompleterEntity> completers;

    @Schema(description = "发明人（校外）")
    private String inventorsOuter;

    @Schema(description = "摘要")
    private String abstractText;

    @Schema(description = "权利要求")
    private String claimsText;

    @Schema(description = "说明书")
    private String descriptionText;

    @Schema(description = "是否承诺")
    private Integer isPromise;

    @Schema(description = "是否代理")
    private Integer isAgency;

    @Schema(description = "代理机构名称")
    private String agencyName;

    /**
     * 权利要求书附件
     */
    @Schema(description = "权利要求书附件")
    private List<String> claimsFile;

    /**
     * 说明书附件
     */
    @Schema(description = "说明书附件")
    private List<String> descriptionFile;

    /**
     * 说明书附图
     */
    @Schema(description = "说明书附图")
    private List<String> descFigureFile;

    /**
     * 说明书摘要附图
     */
    @Schema(description = "说明书摘要附图")
    private List<String> abstractFigureFile;

    /**
     * 说明书摘要附件
     */
    @Schema(description = "说明书摘要附件")
    private List<String> abstractFile;

    /**
     * 负责人编码
     */
    @Schema(description = "负责人编码")
    private String leaderCode;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名")
    private String leaderName;

    /**
     * 专利申请前评估信息
     */
    @Schema(description = "专利申请前评估信息")
    private PatentPreEvalResponse patentPreEval;

    @Schema(description = "专利 PID")
    private String pid;

    @Schema(description = "是否可以申请费用报销")
    private Integer isReimburse;

}