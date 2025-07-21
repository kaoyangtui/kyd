package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 专利信息
 *
 * @author pigx
 * @date 2025-05-31 10:49:31
 */
@Schema(description = "专利信息")
@Data
public class PatentEsPageResponse {

    public static final String BIZ_CODE = "PATENT";

    @Schema(description = "专利唯一ID")
    private String pid;

    // 高亮字段补充
    @Schema(description = "名称")
    private String title;
    @Schema(description = "名称高亮")
    private String titleHighlight;

    @Schema(description = "摘要附图URL")
    private String draws;

    @Schema(description = "摘要")
    private String abs;
    @Schema(description = "摘要高亮")
    private String absHighlight;

    @Schema(description = "专利类型")
    private String patType;

    @Schema(description = "专利类型名称")
    private String patTypeName;

    @Schema(description = "专利权状态")
    private String lprs;

    @Schema(description = "专利权状态代码")
    private String statusCode;

    @Schema(description = "当前法律状态")
    private String legalStatus;

    @Schema(description = "申请号")
    private String appNumber;

    @Schema(description = "申请日")
    private String appDate;

    @Schema(description = "公开（公告）号")
    private String pubNumber;

    @Schema(description = "公开（公告）日")
    private String pubDate;

    @Schema(description = "实审日期")
    private String examinationDate;

    @Schema(description = "授权日")
    private String grantDate;

    @Schema(description = "未授权时间")
    private String unAuthorizedDate;

    @Schema(description = "失效日期")
    private String unEffectiveDate;

    @Schema(description = "失效日")
    private String expireDate;

    @Schema(description = "申请（专利权）人")
    private String applicantName;
    @Schema(description = "申请（专利权）人高亮")
    private String applicantNameHighlight;

    @Schema(description = "申请人类型 ")
    private String applicantType;

    @Schema(description = "发明（设计）人")
    private String inventorName;
    @Schema(description = "发明（设计）人高亮")
    private String inventorNameHighlight;

    @Schema(description = "专利权人")
    private String patentee;
    @Schema(description = "专利权人高亮")
    private String patenteeHighlight;

    @Schema(description = "国民经济分类")
    private String nec;

    @Schema(description = "主分类号")
    private String mainIpc;

    @Schema(description = "分类号")
    private String ipc;

    @Schema(description = "分类号部 ")
    private String ipcSection;

    @Schema(description = "分类号大类 ")
    private String ipcClass;

    @Schema(description = "分类号小类 ")
    private String ipcSubClass;

    @Schema(description = "分类号大组 ")
    private String ipcGroup;

    @Schema(description = "分类号小组 ")
    private String ipcSubGroup;

    @Schema(description = "联合分类")
    private String cpc;

    @Schema(description = "联合分类部 ")
    private String cpcSection;

    @Schema(description = "联合分类大类 ")
    private String cpcClass;

    @Schema(description = "联合分类小类 ")
    private String cpcSubClass;

    @Schema(description = "联合分类大组 ")
    private String cpcGroup;

    @Schema(description = "联合分类小组 ")
    private String cpcSubGroup;

    @Schema(description = "专利代理机构")
    private String agencyName;

    @Schema(description = "代理人")
    private String agentName;

    @Schema(description = "优先权国家")
    private String priorityCountry;

    @Schema(description = "优先权号 (二维数组)")
    private String priorityNo;

    @Schema(description = "优先权 (二维数组)")
    private String priority;

    @Schema(description = "最早优先权 (二维数组)")
    private String firstPriority;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "省")
    private String addrProvince;

    @Schema(description = "市")
    private String addrCity;

    @Schema(description = "区")
    private String addrCounty;

    @Schema(description = "申请国代码")
    private String appCoun;

    @Schema(description = "公开国代码")
    private String pubCountryCode;

    @Schema(description = "国省代码")
    private String proCode;

    @Schema(description = "国家代码")
    private String countryCode;

    @Schema(description = "国家名")
    private String countryName;

    @Schema(description = "省代码")
    private String provinceCode;

    @Schema(description = "省名")
    private String provinceName;

    @Schema(description = "关键词")
    private String patentWords;
    @Schema(description = "关键词高亮")
    private String patentWordsHighlight;

    @Schema(description = "名称关键词")
    private String titleKey;
    @Schema(description = "名称关键词高亮")
    private String titleKeyHighlight;

    @Schema(description = "独权关键词")
    private String clKey;
    @Schema(description = "独权关键词高亮")
    private String clKeyHighlight;

    @Schema(description = "背景关键词")
    private String bgKey;
    @Schema(description = "背景关键词高亮")
    private String bgKeyHighlight;

    @Schema(description = "权利要求数")
    private String claimsQuantity;

    @Schema(description = "独权数")
    private String independentClaimsQuantity;

    @Schema(description = "从权数")
    private String subClaimsQuantity;

    @Schema(description = "专利权转移次数")
    private String patentRightTransferQuantity;

    @Schema(description = "申请权转移次数")
    private String applicationRightTransferQuantity;

    @Schema(description = "许可次数")
    private String licenseQuantity;

    @Schema(description = "质押次数")
    private String pledgeQuantity;

    @Schema(description = "保全次数")
    private String preservationQuantity;

    @Schema(description = "引证总次数")
    private String citQuantity;

    @Schema(description = "专利引证次数")
    private String patCitedQuantity;

    @Schema(description = "非专利引证次数")
    private String nplCitedQuantity;

    @Schema(description = "被引证次数")
    private String fwCitQuantity;

    @Schema(description = "同族数")
    private String familyQuantity;

    @Schema(description = "复审次数")
    private String reexaminationDecisionQuantity;

    @Schema(description = "无效次数")
    private String reexaminationValidQuantity;

    @Schema(description = "判决次数")
    private String courtDecisionQuantity;

    @Schema(description = "同日申请")
    private String sameApp;

    @Schema(description = "申请来源")
    private String appResource;

    @Schema(description = "国际申请")
    private String iapp;

    @Schema(description = "国际公布")
    private String ipub;

    @Schema(description = "进入国家日期")
    private String den;

    @Schema(description = "全文图像页数")
    private String pages;

    @Schema(description = "解密公告日")
    private String declassifiedPublicationDate;

    @Schema(description = "国际申请号")
    private String iappNo;

    @Schema(description = "国际申请日")
    private String iappDate;

    @Schema(description = "国际公布号")
    private String ipubNo;

    @Schema(description = "国际公布日")
    private String ipubDate;

    @Schema(description = "PCT指定国家")
    private String pctCountry;

    @Schema(description = "附图数")
    private String figure;

    @Schema(description = "授权时长")
    private String patentedTime;

    @Schema(description = "是否提前公开")
    private String advancedPublishedDocument;

    @Schema(description = "历史专利权人")
    private String historyPatentee;

    @Schema(description = "说明书页数")
    private String description;

    @Schema(description = "发明人数")
    private String inventors;

    @Schema(description = "分类号小类数")
    private String subclass;

    @Schema(description = "简单同族族号")
    private String simpleFamilyNo;

    @Schema(description = "简单同族 (二维数组)")
    private String simpleFamily;

    @Schema(description = "简单同族数")
    private String simpleFamilyQuantity;

    @Schema(description = "简单同族国家")
    private String simpleFamilyCountry;

    @Schema(description = "扩展同族族号")
    private String extendFamilyNo;

    @Schema(description = "扩展同族 (二维数组)")
    private String extendFamily;

    @Schema(description = "扩展同族数")
    private String extendFamilyQuantity;

    @Schema(description = "扩展同族国家")
    private String extendFamilyCountry;

    @Schema(description = "专利权人地址")
    private String patenteeAddr;

    @Schema(description = "专利权人省")
    private String patenteeAddrProvince;

    @Schema(description = "专利权人市")
    private String patenteeAddrCity;

    @Schema(description = "专利权人区")
    private String patenteeAddrCounty;

    @Schema(description = "专利权人类型 ")
    private String patenteType;

    @Schema(description = "第一申请（专利权）人")
    private String primaryApplicantName;

    @Schema(description = "第一发明（设计）人")
    private String primaryInventorName;

    @Schema(description = "第一专利权人")
    private String primaryPatentee;

    @Schema(description = "分案原申请")
    private String divideInitApp;

    @Schema(description = "分案原申请日")
    private String divideInitAppDate;

    @Schema(description = "分案原申请号")
    private String divideInitAppNo;

    @Schema(description = "申请人数")
    private String applicantQuantity;

    @Schema(description = "代理人数")
    private String agentQuantity;

    @Schema(description = "专利权人数")
    private String patenteeQuantity;

    @Schema(description = "分类号数")
    private String ipcQuantity;

    @Schema(description = "海外同族数")
    private String overseasFamilyQuantity;

    @Schema(description = "无效原因代码")
    private String invalidReasonCode;

    @Schema(description = "高价值专利标识")
    private String highValueFlag;

    @Schema(description = "战略新兴产业标识")
    private String strategicEmergingIndustryFlag;

    @Schema(description = "海外同族标识")
    private String overseasFamilyFlag;

    @Schema(description = "维持年限超十年标识")
    private String maintenancePeriodFlag;

    @Schema(description = "质押融资标识")
    private String highFinancingQuotaFlag;

    @Schema(description = "获奖标识")
    private String awardFlag;

    @Schema(description = "申请号合并标识,1已合并")
    private String mergeFlag;

    @Schema(description = "转移标识,1已转移")
    private String transferFlag;

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "负责人姓名")
    private String leaderName;
}