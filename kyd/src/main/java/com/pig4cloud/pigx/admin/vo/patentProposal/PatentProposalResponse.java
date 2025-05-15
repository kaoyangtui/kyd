package com.pig4cloud.pigx.admin.vo.patentProposal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "专利提案响应对象")
public class PatentProposalResponse {

    public static final String BIZ_CODE = "patent_list";

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
     * 流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回
     */
    @Schema(description = "流程状态：-1未开始 0办理中 1结束 2驳回中 3跳过 9被驳回")
    private Integer flowStatus;

    /**
     * 当前流程节点名称
     */
    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    /**
     * 拟申请专利名称
     */
    @Schema(description = "拟申请专利名称")
    private String title;

    /**
     * 拟申请专利类型（如：发明专利、实用新型等）
     */
    @Schema(description = "拟申请专利类型（如：发明专利、实用新型等）")
    private String type;

    /**
     * 技术所属领域
     */
    @Schema(description = "技术所属领域")
    private String techField;

    /**
     * 专利计划维持年限
     */
    @Schema(description = "专利计划维持年限")
    private Integer planYears;

    /**
     * 是否来源于校内高价值专利培育，0否1是
     */
    @Schema(description = "是否来源于校内高价值专利培育，0否1是")
    private Integer isHighValueInner;

    /**
     * 是否有意成果转化，0否1是
     */
    @Schema(description = "是否有意成果转化，0否1是")
    private Integer isTransform;

    /**
     * 是否依托项目，0否1是
     */
    @Schema(description = "是否依托项目，0否1是")
    private Integer isFromProject;

    /**
     * 是否快速预审，0否1是
     */
    @Schema(description = "是否快速预审，0否1是")
    private Integer isFastTrack;

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
     * 申请人信息，多个机构用分号隔开，每个结构为“名称-类型”格式
     */
    @Schema(description = "申请人信息，多个机构用分号隔开，每个结构为“名称-类型”格式")
    private String applicantOrgs;

    /**
     * 发明人信息（校内），格式为结构化文本或分号分隔结构字段
     */
    @Schema(description = "发明人信息（校内），格式为结构化文本或分号分隔结构字段")
    private String inventorsInner;

    /**
     * 发明人（校外），最多10个，分号隔开
     */
    @Schema(description = "发明人（校外），最多10个，分号隔开")
    private String inventorsOuter;

    /**
     * 摘要
     */
    @Schema(description = "摘要")
    private String abstractText;

    /**
     * 权利要求
     */
    @Schema(description = "权利要求")
    private String claimsText;

    /**
     * 说明书
     */
    @Schema(description = "说明书")
    private String descriptionText;

    /**
     * 是否承诺发明人承诺书，0否1是
     */
    @Schema(description = "是否承诺发明人承诺书，0否1是")
    private Integer isPromise;

    /**
     * 是否代理，0否1是
     */
    @Schema(description = "是否代理，0否1是")
    private Integer isAgency;

    /**
     * 代理机构名称
     */
    @Schema(description = "代理机构名称")
    private String agencyName;

    /**
     * 快速预审附件路径
     */
    @Schema(description = "快速预审附件路径")
    private String fastTrackFile;

    /**
     * 权利要求书附件路径
     */
    @Schema(description = "权利要求书附件路径")
    private String claimsFile;

    /**
     * 说明书附件路径
     */
    @Schema(description = "说明书附件路径")
    private String descriptionFile;

    /**
     * 说明书附图路径
     */
    @Schema(description = "说明书附图路径")
    private String descFigureFile;

    /**
     * 摘要附图路径
     */
    @Schema(description = "摘要附图路径")
    private String abstractFigureFile;

    /**
     * 所属院系
     */
    @Schema(description = "所属院系")
    private String deptId;

    /**
     * 创建/提交人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建/提交人")
    private String createBy;

    /**
     * 创建/提交时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建/提交时间")
    private LocalDateTime createTime;
}