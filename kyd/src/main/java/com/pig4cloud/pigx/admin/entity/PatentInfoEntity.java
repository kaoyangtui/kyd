package com.pig4cloud.pigx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;

import java.time.LocalDateTime;

/**
 * 专利信息表
 *
 * @author zl
 * @date 2025-04-15 13:09:58
 */
@Data
@TenantTable
@TableName("t_patent_info")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利信息表")
public class PatentInfoEntity extends Model<PatentInfoEntity> {


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 专利唯一ID
     */
    @Schema(description = "专利唯一ID")
    private String pid;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String title;

    /**
     * 摘要
     */
    @Schema(description = "摘要")
    private String abs;

    /**
     * 专利类型
     */
    @Schema(description = "专利类型")
    private String patType;

    /**
     * 专利权状态
     */
    @Schema(description = "专利权状态")
    private String lprs;

    /**
     * 专利权状态代码
     */
    @Schema(description = "专利权状态代码")
    private String statusCode;

    /**
     * 申请号 (数组)
     */
    @Schema(description = "申请号 (数组)")
    private String appNumber;

    /**
     * 申请日
     */
    @Schema(description = "申请日")
    private String appDate;

    /**
     * 公开（公告）号 (数组)
     */
    @Schema(description = "公开（公告）号 (数组)")
    private String pubNumber;

    /**
     * 公开（公告）日
     */
    @Schema(description = "公开（公告）日")
    private String pubDate;

    /**
     * 授权日
     */
    @Schema(description = "授权日")
    private String grantDate;

    /**
     * 失效日
     */
    @Schema(description = "失效日")
    private String expireDate;

    /**
     * 申请（专利权）人 (List<String>)
     */
    @Schema(description = "申请（专利权）人 (List<String>)")
    private String applicantName;

    /**
     * 申请人类型 (Set<String>)
     */
    @Schema(description = "申请人类型 (Set<String>)")
    private String applicantType;

    /**
     * 发明（设计）人 (List<String>)
     */
    @Schema(description = "发明（设计）人 (List<String>)")
    private String inventorName;

    /**
     * 专利权人 (List<String>)
     */
    @Schema(description = "专利权人 (List<String>)")
    private String patentee;

    /**
     * 国民经济分类 (List<String>)
     */
    @Schema(description = "国民经济分类 (List<String>)")
    private String nec;

    /**
     * 主分类号
     */
    @Schema(description = "主分类号")
    private String mainIpc;

    /**
     * 分类号 (List<String>)
     */
    @Schema(description = "分类号 (List<String>)")
    private String ipc;

    /**
     * 分类号部 (Set<String>)
     */
    @Schema(description = "分类号部 (Set<String>)")
    private String ipcSection;

    /**
     * 分类号大类 (Set<String>)
     */
    @Schema(description = "分类号大类 (Set<String>)")
    private String ipcClass;

    /**
     * 分类号小类 (Set<String>)
     */
    @Schema(description = "分类号小类 (Set<String>)")
    private String ipcSubClass;

    /**
     * 分类号大组 (Set<String>)
     */
    @Schema(description = "分类号大组 (Set<String>)")
    private String ipcGroup;

    /**
     * 分类号小组 (Set<String>)
     */
    @Schema(description = "分类号小组 (Set<String>)")
    private String ipcSubGroup;

    /**
     * 联合分类 (List<String>)
     */
    @Schema(description = "联合分类 (List<String>)")
    private String cpc;

    /**
     * 联合分类部 (Set<String>)
     */
    @Schema(description = "联合分类部 (Set<String>)")
    private String cpcSection;

    /**
     * 联合分类大类 (Set<String>)
     */
    @Schema(description = "联合分类大类 (Set<String>)")
    private String cpcClass;

    /**
     * 联合分类小类 (Set<String>)
     */
    @Schema(description = "联合分类小类 (Set<String>)")
    private String cpcSubClass;

    /**
     * 联合分类大组 (Set<String>)
     */
    @Schema(description = "联合分类大组 (Set<String>)")
    private String cpcGroup;

    /**
     * 联合分类小组 (Set<String>)
     */
    @Schema(description = "联合分类小组 (Set<String>)")
    private String cpcSubGroup;

    /**
     * 专利代理机构
     */
    @Schema(description = "专利代理机构")
    private String agencyName;

    /**
     * 代理人 (List<String>)
     */
    @Schema(description = "代理人 (List<String>)")
    private String agentName;

    /**
     * 优先权国家 (List<String>)
     */
    @Schema(description = "优先权国家 (List<String>)")
    private String priorityCountry;

    /**
     * 优先权号 (二维数组)
     */
    @Schema(description = "优先权号 (二维数组)")
    private String priorityNo;

    /**
     * 优先权 (二维数组)
     */
    @Schema(description = "优先权 (二维数组)")
    private String priority;

    /**
     * 最早优先权 (二维数组)
     */
    @Schema(description = "最早优先权 (二维数组)")
    private String firstPriority;

    /**
     * 引证分类号 (List<String>)
     */
    @Schema(description = "引证分类号 (List<String>)")
    private String citationIpc;

    /**
     * 引证号 (二维数组)
     */
    @Schema(description = "引证号 (二维数组)")
    private String citationNo;

    /**
     * 引证申请人 (List<String>)
     */
    @Schema(description = "引证申请人 (List<String>)")
    private String citationApplicant;

    /**
     * 引证国家 (Set<String>)
     */
    @Schema(description = "引证国家 (Set<String>)")
    private String citationCountry;

    /**
     * 非专利引证 (List<String>)
     */
    @Schema(description = "非专利引证 (List<String>)")
    private String citationOther;

    /**
     * 被引证号 (二维数组)
     */
    @Schema(description = "被引证号 (二维数组)")
    private String citationForward;

    /**
     * 被引证国家 (Set<String>)
     */
    @Schema(description = "被引证国家 (Set<String>)")
    private String citationForwardCountry;

    /**
     * 权利要求书
     */
    @Schema(description = "权利要求书")
    private String claimsPath;

    /**
     * 权利要求书类型
     */
    @Schema(description = "权利要求书类型")
    private String claimsType;

    /**
     * 独权项
     */
    @Schema(description = "独权项")
    private String independentClaims;

    /**
     * 说明书
     */
    @Schema(description = "说明书")
    private String instrPath;

    /**
     * 说明书附图
     */
    @Schema(description = "说明书附图")
    private String instrTif;

    /**
     * 主权项
     */
    @Schema(description = "主权项")
    private String cl;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 省
     */
    @Schema(description = "省")
    private String addrProvince;

    /**
     * 市
     */
    @Schema(description = "市")
    private String addrCity;

    /**
     * 区
     */
    @Schema(description = "区")
    private String addrCounty;

    /**
     * 申请国代码
     */
    @Schema(description = "申请国代码")
    private String appCoun;

    /**
     * 公开国代码
     */
    @Schema(description = "公开国代码")
    private String pubCountryCode;

    /**
     * 国省代码
     */
    @Schema(description = "国省代码")
    private String proCode;

    /**
     * 国家代码
     */
    @Schema(description = "国家代码")
    private String countryCode;

    /**
     * 国家名
     */
    @Schema(description = "国家名")
    private String countryName;

    /**
     * 省代码
     */
    @Schema(description = "省代码")
    private String provinceCode;

    /**
     * 省名
     */
    @Schema(description = "省名")
    private String provinceName;

    /**
     * 关键词 (List<String>)
     */
    @Schema(description = "关键词 (List<String>)")
    private String patentWords;

    /**
     * 名称关键词 (List<String>)
     */
    @Schema(description = "名称关键词 (List<String>)")
    private String titleKey;

    /**
     * 独权关键词 (List<String>)
     */
    @Schema(description = "独权关键词 (List<String>)")
    private String clKey;

    /**
     * 背景关键词 (List<String>)
     */
    @Schema(description = "背景关键词 (List<String>)")
    private String bgKey;

    /**
     * 权利要求数
     */
    @Schema(description = "权利要求数")
    private String claimsQuantity;

    /**
     * 独权数
     */
    @Schema(description = "独权数")
    private String independentClaimsQuantity;

    /**
     * 从权数
     */
    @Schema(description = "从权数")
    private String subClaimsQuantity;

    /**
     * 专利权转移次数
     */
    @Schema(description = "专利权转移次数")
    private String patentRightTransferQuantity;

    /**
     * 申请权转移次数
     */
    @Schema(description = "申请权转移次数")
    private String applicationRightTransferQuantity;

    /**
     * 许可次数
     */
    @Schema(description = "许可次数")
    private String licenseQuantity;

    /**
     * 质押次数
     */
    @Schema(description = "质押次数")
    private String pledgeQuantity;

    /**
     * 保全次数
     */
    @Schema(description = "保全次数")
    private String preservationQuantity;

    /**
     * 引证总次数
     */
    @Schema(description = "引证总次数")
    private String citQuantity;

    /**
     * 专利引证次数
     */
    @Schema(description = "专利引证次数")
    private String patCitedQuantity;

    /**
     * 非专利引证次数
     */
    @Schema(description = "非专利引证次数")
    private String nplCitedQuantity;

    /**
     * 被引证次数
     */
    @Schema(description = "被引证次数")
    private String fwCitQuantity;

    /**
     * 同族数
     */
    @Schema(description = "同族数")
    private String familyQuantity;

    /**
     * 复审次数
     */
    @Schema(description = "复审次数")
    private String reexaminationDecisionQuantity;

    /**
     * 无效次数
     */
    @Schema(description = "无效次数")
    private String reexaminationValidQuantity;

    /**
     * 判决次数
     */
    @Schema(description = "判决次数")
    private String courtDecisionQuantity;

    /**
     * 同日申请 (数组)
     */
    @Schema(description = "同日申请 (数组)")
    private String sameApp;

    /**
     * DB_NAME
     */
    @Schema(description = "DB_NAME")
    private String dbName;

    /**
     * 申请来源
     */
    @Schema(description = "申请来源")
    private String appResource;

    /**
     * 国际申请
     */
    @Schema(description = "国际申请")
    private String iapp;

    /**
     * 国际公布
     */
    @Schema(description = "国际公布")
    private String ipub;

    /**
     * 进入国家日期
     */
    @Schema(description = "进入国家日期")
    private String den;

    /**
     * 摘要附图URL (URL有效时间为10分钟)
     */
    @Schema(description = "摘要附图URL (URL有效时间为10分钟)")
    private String draws;

    /**
     * 外观专利图URL (URL有效时间为10分钟) (List<String>)
     */
    @Schema(description = "外观专利图URL (URL有效时间为10分钟) (List<String>)")
    private String tifDistributePath;

    /**
     * 说明书附图URL (URL有效时间为10分钟) (List<String>)
     */
    @Schema(description = "说明书附图URL (URL有效时间为10分钟) (List<String>)")
    private String drawsPic;

    /**
     * 全文图像页数
     */
    @Schema(description = "全文图像页数")
    private String pages;

    /**
     * 解密公告日
     */
    @Schema(description = "解密公告日")
    private String declassifiedPublicationDate;

    /**
     * 国际申请号 (数组)
     */
    @Schema(description = "国际申请号 (数组)")
    private String iappNo;

    /**
     * 国际申请日
     */
    @Schema(description = "国际申请日")
    private String iappDate;

    /**
     * 国际公布号 (数组)
     */
    @Schema(description = "国际公布号 (数组)")
    private String ipubNo;

    /**
     * 国际公布日
     */
    @Schema(description = "国际公布日")
    private String ipubDate;

    /**
     * PCT指定国家 (List<String>)
     */
    @Schema(description = "PCT指定国家 (List<String>)")
    private String pctCountry;

    /**
     * 附图数
     */
    @Schema(description = "附图数")
    private String figure;

    /**
     * 授权时长
     */
    @Schema(description = "授权时长")
    private String patentedTime;

    /**
     * 是否提前公开
     */
    @Schema(description = "是否提前公开")
    private String advancedPublishedDocument;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 历史专利权人 (List<String>)
     */
    @Schema(description = "历史专利权人 (List<String>)")
    private String historyPatentee;

    /**
     * createTime
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "createTime")
    private LocalDateTime createTime;

    /**
     * 说明书页数
     */
    @Schema(description = "说明书页数")
    private String description;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 发明人数
     */
    @Schema(description = "发明人数")
    private String inventors;

    /**
     * updateTime
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "updateTime")
    private LocalDateTime updateTime;

    /**
     * 分类号小类数
     */
    @Schema(description = "分类号小类数")
    private String subclass;

    /**
     * delFlag
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "delFlag")
    private String delFlag;

    /**
     * 法律状态
     */
    @Schema(description = "法律状态")
    private String legalStatus;

    /**
     * tenantId
     */
    @Schema(description = "tenantId")
    private Long tenantId;

    /**
     * 摘要附图的文件名
     */
    @Schema(description = "摘要附图的文件名")
    private String absPic;

    /**
     * 外观专利图的文件名 (List<String>)
     */
    @Schema(description = "外观专利图的文件名 (List<String>)")
    private String designPic;

    /**
     * 说明书附图的文件名 (List<String>)
     */
    @Schema(description = "说明书附图的文件名 (List<String>)")
    private String incPic;

    /**
     * 简单同族族号
     */
    @Schema(description = "简单同族族号")
    private String simpleFamilyNo;

    /**
     * 简单同族 (二维数组)
     */
    @Schema(description = "简单同族 (二维数组)")
    private String simpleFamily;

    /**
     * 简单同族数
     */
    @Schema(description = "简单同族数")
    private String simpleFamilyQuantity;

    /**
     * 简单同族国家 (List<String>)
     */
    @Schema(description = "简单同族国家 (List<String>)")
    private String simpleFamilyCountry;

    /**
     * 简单同族信息 (List<FamilyInfoPojo>)
     */
    @Schema(description = "简单同族信息 (List<FamilyInfoPojo>)")
    private String simpleFamilyList;

    /**
     * 扩展同族族号
     */
    @Schema(description = "扩展同族族号")
    private String extendFamilyNo;

    /**
     * 扩展同族 (二维数组)
     */
    @Schema(description = "扩展同族 (二维数组)")
    private String extendFamily;

    /**
     * 扩展同族数
     */
    @Schema(description = "扩展同族数")
    private String extendFamilyQuantity;

    /**
     * 扩展同族国家 (List<String>)
     */
    @Schema(description = "扩展同族国家 (List<String>)")
    private String extendFamilyCountry;

    /**
     * 扩展同族信息 (List<FamilyInfoPojo>)
     */
    @Schema(description = "扩展同族信息 (List<FamilyInfoPojo>)")
    private String extendFamilyList;

    /**
     * 专利权人地址
     */
    @Schema(description = "专利权人地址")
    private String patenteeAddr;

    /**
     * 专利权人省
     */
    @Schema(description = "专利权人省")
    private String patenteeAddrProvince;

    /**
     * 专利权人市
     */
    @Schema(description = "专利权人市")
    private String patenteeAddrCity;

    /**
     * 专利权人区
     */
    @Schema(description = "专利权人区")
    private String patenteeAddrCounty;

    /**
     * 专利权人类型 (Set<String>)
     */
    @Schema(description = "专利权人类型 (Set<String>)")
    private String patenteType;

    /**
     * 第一申请（专利权）人
     */
    @Schema(description = "第一申请（专利权）人")
    private String primaryApplicantName;

    /**
     * 第一发明（设计）人
     */
    @Schema(description = "第一发明（设计）人")
    private String primaryInventorName;

    /**
     * 第一专利权人
     */
    @Schema(description = "第一专利权人")
    private String primaryPatentee;

    /**
     * 分案原申请
     */
    @Schema(description = "分案原申请")
    private String divideInitApp;

    /**
     * 分案原申请日
     */
    @Schema(description = "分案原申请日")
    private String divideInitAppDate;

    /**
     * 分案原申请号 (数组)
     */
    @Schema(description = "分案原申请号 (数组)")
    private String divideInitAppNo;

    /**
     * 申请人数
     */
    @Schema(description = "申请人数")
    private String applicantQuantity;

    /**
     * 代理人数
     */
    @Schema(description = "代理人数")
    private String agentQuantity;

    /**
     * 专利权人数
     */
    @Schema(description = "专利权人数")
    private String patenteeQuantity;

    /**
     * 分类号数
     */
    @Schema(description = "分类号数")
    private String ipcQuantity;

    /**
     * 海外同族数
     */
    @Schema(description = "海外同族数")
    private String overseasFamilyQuantity;

    /**
     * 无效原因代码
     */
    @Schema(description = "无效原因代码")
    private String invalidReasonCode;

    /**
     * 高价值专利标识
     */
    @Schema(description = "高价值专利标识")
    private String highValueFlag;

    /**
     * 战略新兴产业标识
     */
    @Schema(description = "战略新兴产业标识")
    private String strategicEmergingIndustryFlag;

    /**
     * 战略新兴产业明细
     */
    @Schema(description = "战略新兴产业明细")
    private String emergingIndustriesClassifications;

    /**
     * 海外同族标识
     */
    @Schema(description = "海外同族标识")
    private String overseasFamilyFlag;

    /**
     * 维持年限超十年标识
     */
    @Schema(description = "维持年限超十年标识")
    private String maintenancePeriodFlag;

    /**
     * 质押融资标识
     */
    @Schema(description = "质押融资标识")
    private String highFinancingQuotaFlag;

    /**
     * 获奖标识
     */
    @Schema(description = "获奖标识")
    private String awardFlag;

    /**
     * 申请人信息 (List<ApplicantInfoPojo>)
     */
    @Schema(description = "申请人信息 (List<ApplicantInfoPojo>)")
    private String applicantInfo;

    /**
     * 引证信息 (List<CitationInfoPojo>)
     */
    @Schema(description = "引证信息 (List<CitationInfoPojo>)")
    private String citationInfo;

    /**
     * 法律信息 (List<LegalPojo>)
     */
    @Schema(description = "法律信息 (List<LegalPojo>)")
    private String legalList;

    /**
     * 转移转让 (List<TransferPojo>)
     */
    @Schema(description = "转移转让 (List<TransferPojo>)")
    private String transferList;

    /**
     * 质押保全 (List<PreservationPojo>)
     */
    @Schema(description = "质押保全 (List<PreservationPojo>)")
    private String preservationList;

    /**
     * 实施许可 (List<ExploitationPojo>)
     */
    @Schema(description = "实施许可 (List<ExploitationPojo>)")
    private String exploitationList;

    /**
     * 优先权信息 (List<PriorityInfoPojo>)
     */
    @Schema(description = "优先权信息 (List<PriorityInfoPojo>)")
    private String priorityInfo;

    /**
     * 无效请求 (List<InvalidAndReexamPojo>)
     */
    @Schema(description = "无效请求 (List<InvalidAndReexamPojo>)")
    private String invalidList;

    /**
     * 复审请求 (List<InvalidAndReexamPojo>)
     */
    @Schema(description = "复审请求 (List<InvalidAndReexamPojo>)")
    private String reexamList;

    /**
     * 相关判例 (List<JudgmentPojo>)
     */
    @Schema(description = "相关判例 (List<JudgmentPojo>)")
    private String judgmentList;

    /**
     * 海关备案 (List<CustomsPojo>)
     */
    @Schema(description = "海关备案 (List<CustomsPojo>)")
    private String customList;

    /**
     * 海外同族信息 (List<FamilyCountryPojo>)
     */
    @Schema(description = "海外同族信息 (List<FamilyCountryPojo>)")
    private String overseasFamilyInfo;

    /**
     * 一审判决 (List<FirstJudgmentPojo>)
     */
    @Schema(description = "一审判决 (List<FirstJudgmentPojo>)")
    private String firstJudgmentList;

    /**
     * 二审判决 (List<SecondJudgmentPojo>)
     */
    @Schema(description = "二审判决 (List<SecondJudgmentPojo>)")
    private String secondJudgmentList;
}