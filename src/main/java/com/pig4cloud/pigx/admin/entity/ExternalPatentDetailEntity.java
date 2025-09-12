package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 外部专利扩展信息表
 *
 * @author pigx
 * @date 2025-09-11 13:40:09
 */
@Data
@TenantTable
@TableName("t_external_patent_detail")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "外部专利扩展信息表")
public class ExternalPatentDetailEntity extends Model<ExternalPatentDetailEntity> {

 
	/**
	* id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Long id;

	/**
	* 专利唯一ID
	*/
    @Schema(description="专利唯一ID")
    private String pid;

	/**
	* 权利要求书
	*/
    @Schema(description="权利要求书")
    private String claimsPath;

	/**
	* 权利要求书类型
	*/
    @Schema(description="权利要求书类型")
    private String claimsType;

	/**
	* 独权项
	*/
    @Schema(description="独权项")
    private String independentClaims;

	/**
	* 说明书
	*/
    @Schema(description="说明书")
    private String instrPath;

	/**
	* 说明书附图
	*/
    @Schema(description="说明书附图")
    private String instrTif;

	/**
	* 主权项
	*/
    @Schema(description="主权项")
    private String cl;

	/**
	* 摘要附图URL (URL有效时间为10分钟)
	*/
    @Schema(description="摘要附图URL (URL有效时间为10分钟)")
    private String draws;

	/**
	* 外观专利图URL (URL有效时间为10分钟) (List<String>)
	*/
    @Schema(description="外观专利图URL (URL有效时间为10分钟) (List<String>)")
    private String tifDistributePath;

	/**
	* 说明书附图URL (URL有效时间为10分钟) (List<String>)
	*/
    @Schema(description="说明书附图URL (URL有效时间为10分钟) (List<String>)")
    private String drawsPic;

	/**
	* 摘要附图的文件名
	*/
    @Schema(description="摘要附图的文件名")
    private String absPic;

	/**
	* 外观专利图的文件名 (List<String>)
	*/
    @Schema(description="外观专利图的文件名 (List<String>)")
    private String designPic;

	/**
	* 说明书附图的文件名 (List<String>)
	*/
    @Schema(description="说明书附图的文件名 (List<String>)")
    private String incPic;

	/**
	* 简单同族信息 (List<FamilyInfoPojo>)
	*/
    @Schema(description="简单同族信息 (List<FamilyInfoPojo>)")
    private String simpleFamilyList;

	/**
	* 扩展同族信息 (List<FamilyInfoPojo>)
	*/
    @Schema(description="扩展同族信息 (List<FamilyInfoPojo>)")
    private String extendFamilyList;

	/**
	* 申请人信息 (List<ApplicantInfoPojo>)
	*/
    @Schema(description="申请人信息 (List<ApplicantInfoPojo>)")
    private String applicantInfo;

	/**
	* 引证分类号 (List<String>)
	*/
    @Schema(description="引证分类号 (List<String>)")
    private String citationIpc;

	/**
	* 引证号 (二维数组)
	*/
    @Schema(description="引证号 (二维数组)")
    private String citationNo;

	/**
	* 引证申请人 (List<String>)
	*/
    @Schema(description="引证申请人 (List<String>)")
    private String citationApplicant;

	/**
	* 引证国家 (Set<String>)
	*/
    @Schema(description="引证国家 (Set<String>)")
    private String citationCountry;

	/**
	* 非专利引证 (List<String>)
	*/
    @Schema(description="非专利引证 (List<String>)")
    private String citationOther;

	/**
	* 被引证号 (二维数组)
	*/
    @Schema(description="被引证号 (二维数组)")
    private String citationForward;

	/**
	* 被引证国家 (Set<String>)
	*/
    @Schema(description="被引证国家 (Set<String>)")
    private String citationForwardCountry;

	/**
	* 引证信息 (List<CitationInfoPojo>)
	*/
    @Schema(description="引证信息 (List<CitationInfoPojo>)")
    private String citationInfo;

	/**
	* 法律状态
	*/
    @Schema(description="法律状态")
    private String legalStatus;

	/**
	* 法律信息 (List<LegalPojo>)
	*/
    @Schema(description="法律信息 (List<LegalPojo>)")
    private String legalList;

	/**
	* 转移转让 (List<TransferPojo>)
	*/
    @Schema(description="转移转让 (List<TransferPojo>)")
    private String transferList;

	/**
	* 质押保全 (List<PreservationPojo>)
	*/
    @Schema(description="质押保全 (List<PreservationPojo>)")
    private String preservationList;

	/**
	* 实施许可 (List<ExploitationPojo>)
	*/
    @Schema(description="实施许可 (List<ExploitationPojo>)")
    private String exploitationList;

	/**
	* 优先权信息 (List<PriorityInfoPojo>)
	*/
    @Schema(description="优先权信息 (List<PriorityInfoPojo>)")
    private String priorityInfo;

	/**
	* 无效请求 (List<InvalidAndReexamPojo>)
	*/
    @Schema(description="无效请求 (List<InvalidAndReexamPojo>)")
    private String invalidList;

	/**
	* 复审请求 (List<InvalidAndReexamPojo>)
	*/
    @Schema(description="复审请求 (List<InvalidAndReexamPojo>)")
    private String reexamList;

	/**
	* 相关判例 (List<JudgmentPojo>)
	*/
    @Schema(description="相关判例 (List<JudgmentPojo>)")
    private String judgmentList;

	/**
	* 海关备案 (List<CustomsPojo>)
	*/
    @Schema(description="海关备案 (List<CustomsPojo>)")
    private String customList;

	/**
	* 海外同族信息 (List<FamilyCountryPojo>)
	*/
    @Schema(description="海外同族信息 (List<FamilyCountryPojo>)")
    private String overseasFamilyInfo;

	/**
	* 一审判决 (List<FirstJudgmentPojo>)
	*/
    @Schema(description="一审判决 (List<FirstJudgmentPojo>)")
    private String firstJudgmentList;

	/**
	* 二审判决 (List<SecondJudgmentPojo>)
	*/
    @Schema(description="二审判决 (List<SecondJudgmentPojo>)")
    private String secondJudgmentList;

	/**
	* 战略新兴产业明细
	*/
    @Schema(description="战略新兴产业明细")
    private String emergingIndustriesClassifications;

	/**
	* 创建人ID
	*/
    @Schema(description="创建人ID")
    private Long createUserId;

	/**
	* 创建人姓名
	*/
    @Schema(description="创建人姓名")
    private String createUserName;
 
	/**
	* createTime
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="createTime")
    private LocalDateTime createTime;
 
	/**
	* updateTime
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="updateTime")
    private LocalDateTime updateTime;
 
	/**
	* delFlag
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="delFlag")
    private String delFlag;
 
	/**
	* tenantId
	*/
    @Schema(description="tenantId")
    private Long tenantId;
}