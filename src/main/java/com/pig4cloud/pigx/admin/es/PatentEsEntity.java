package com.pig4cloud.pigx.admin.es;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexName;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@IndexName("patent_info")
@Schema(description = "专利信息")
public class PatentEsEntity {
    @IndexField("id")
    private Long id;

    @IndexField("pid")
    private String pid;

    @IndexField("title")
    private String title;

    @IndexField("draws")
    private String draws;

    @IndexField("abs")
    private String abs;

    @IndexField("pat_type")
    private String patType;

    @IndexField("lprs")
    private String lprs;

    @IndexField("status_code")
    private String statusCode;

    @IndexField("legal_status")
    private String legalStatus;

    @IndexField("app_number")
    private String appNumber;

    @IndexField("app_date")
    private LocalDate appDate;

    @IndexField("pub_number")
    private String pubNumber;

    @IndexField("pub_date")
    private LocalDate pubDate;

    @IndexField("examination_date")
    private String examinationDate;

    @IndexField("grant_date")
    private String grantDate;

    @IndexField("un_authorized_date")
    private String unAuthorizedDate;

    @IndexField("un_effective_date")
    private String unEffectiveDate;

    @IndexField("expire_date")
    private String expireDate;

    @IndexField("applicant_name")
    private String applicantName;

    @IndexField("applicant_type")
    private String applicantType;

    @IndexField("inventor_name")
    private String inventorName;

    @IndexField("patentee")
    private String patentee;

    @IndexField("nec")
    private String nec;

    @IndexField("main_ipc")
    private String mainIpc;

    @IndexField("ipc")
    private String ipc;

    @IndexField("ipc_section")
    private String ipcSection;

    @IndexField("ipc_class")
    private String ipcClass;

    @IndexField("ipc_sub_class")
    private String ipcSubClass;

    @IndexField("ipc_group")
    private String ipcGroup;

    @IndexField("ipc_sub_group")
    private String ipcSubGroup;

    @IndexField("cpc")
    private String cpc;

    @IndexField("cpc_section")
    private String cpcSection;

    @IndexField("cpc_class")
    private String cpcClass;

    @IndexField("cpc_sub_class")
    private String cpcSubClass;

    @IndexField("cpc_group")
    private String cpcGroup;

    @IndexField("cpc_sub_group")
    private String cpcSubGroup;

    @IndexField("agency_name")
    private String agencyName;

    @IndexField("agent_name")
    private String agentName;

    @IndexField("priority_country")
    private String priorityCountry;

    @IndexField("priority_no")
    private String priorityNo;

    @IndexField("priority")
    private String priority;

    @IndexField("first_priority")
    private String firstPriority;

    @IndexField("address")
    private String address;

    @IndexField("addr_province")
    private String addrProvince;

    @IndexField("addr_city")
    private String addrCity;

    @IndexField("addr_county")
    private String addrCounty;

    @IndexField("app_coun")
    private String appCoun;

    @IndexField("pub_country_code")
    private String pubCountryCode;

    @IndexField("pro_code")
    private String proCode;

    @IndexField("country_code")
    private String countryCode;

    @IndexField("country_name")
    private String countryName;

    @IndexField("province_code")
    private String provinceCode;

    @IndexField("province_name")
    private String provinceName;

    @IndexField("patent_words")
    private String patentWords;

    @IndexField("title_key")
    private String titleKey;

    @IndexField("cl_key")
    private String clKey;

    @IndexField("bg_key")
    private String bgKey;

    @IndexField("claims_quantity")
    private String claimsQuantity;

    @IndexField("independent_claims_quantity")
    private String independentClaimsQuantity;

    @IndexField("sub_claims_quantity")
    private String subClaimsQuantity;

    @IndexField("patent_right_transfer_quantity")
    private String patentRightTransferQuantity;

    @IndexField("application_right_transfer_quantity")
    private String applicationRightTransferQuantity;

    @IndexField("license_quantity")
    private String licenseQuantity;

    @IndexField("pledge_quantity")
    private String pledgeQuantity;

    @IndexField("preservation_quantity")
    private String preservationQuantity;

    @IndexField("cit_quantity")
    private String citQuantity;

    @IndexField("pat_cited_quantity")
    private String patCitedQuantity;

    @IndexField("npl_cited_quantity")
    private String nplCitedQuantity;

    @IndexField("fw_cit_quantity")
    private String fwCitQuantity;

    @IndexField("family_quantity")
    private String familyQuantity;

    @IndexField("reexamination_decision_quantity")
    private String reexaminationDecisionQuantity;

    @IndexField("reexamination_valid_quantity")
    private String reexaminationValidQuantity;

    @IndexField("court_decision_quantity")
    private String courtDecisionQuantity;

    @IndexField("same_app")
    private String sameApp;

    @IndexField("db_name")
    private String dbName;

    @IndexField("app_resource")
    private String appResource;

    @IndexField("iapp")
    private String iapp;

    @IndexField("ipub")
    private String ipub;

    @IndexField("den")
    private String den;

    @IndexField("pages")
    private String pages;

    @IndexField("declassified_publication_date")
    private String declassifiedPublicationDate;

    @IndexField("iapp_no")
    private String iappNo;

    @IndexField("iapp_date")
    private String iappDate;

    @IndexField("ipub_no")
    private String ipubNo;

    @IndexField("ipub_date")
    private String ipubDate;

    @IndexField("pct_country")
    private String pctCountry;

    @IndexField("figure")
    private String figure;

    @IndexField("patented_time")
    private String patentedTime;

    @IndexField("advanced_published_document")
    private String advancedPublishedDocument;

    @IndexField("history_patentee")
    private String historyPatentee;

    @IndexField("description")
    private String description;

    @IndexField("inventors")
    private String inventors;

    @IndexField("subclass")
    private String subclass;

    @IndexField("simple_family_no")
    private String simpleFamilyNo;

    @IndexField("simple_family")
    private String simpleFamily;

    @IndexField("simple_family_quantity")
    private String simpleFamilyQuantity;

    @IndexField("simple_family_country")
    private String simpleFamilyCountry;

    @IndexField("extend_family_no")
    private String extendFamilyNo;

    @IndexField("extend_family")
    private String extendFamily;

    @IndexField("extend_family_quantity")
    private String extendFamilyQuantity;

    @IndexField("extend_family_country")
    private String extendFamilyCountry;

    @IndexField("patentee_addr")
    private String patenteeAddr;

    @IndexField("patentee_addr_province")
    private String patenteeAddrProvince;

    @IndexField("patentee_addr_city")
    private String patenteeAddrCity;

    @IndexField("patentee_addr_county")
    private String patenteeAddrCounty;

    @IndexField("create_by")
    private String createBy;

    @IndexField("patente_type")
    private String patenteType;

    @IndexField("create_time")
    private LocalDateTime createTime;

    @IndexField("primary_applicant_name")
    private String primaryApplicantName;

    @IndexField("update_by")
    private String updateBy;

    @IndexField("primary_inventor_name")
    private String primaryInventorName;

    @IndexField("update_time")
    private LocalDateTime updateTime;

    @IndexField("primary_patentee")
    private String primaryPatentee;

    @IndexField("del_flag")
    private String delFlag;

    @IndexField("divide_init_app")
    private String divideInitApp;

    @IndexField("tenant_id")
    private Long tenantId;

    @IndexField("divide_init_app_date")
    private String divideInitAppDate;

    @IndexField("divide_init_app_no")
    private String divideInitAppNo;

    @IndexField("applicant_quantity")
    private String applicantQuantity;

    @IndexField("agent_quantity")
    private String agentQuantity;

    @IndexField("patentee_quantity")
    private String patenteeQuantity;

    @IndexField("ipc_quantity")
    private String ipcQuantity;

    @IndexField("overseas_family_quantity")
    private String overseasFamilyQuantity;

    @IndexField("invalid_reason_code")
    private String invalidReasonCode;

    @IndexField("high_value_flag")
    private String highValueFlag;

    @IndexField("strategic_emerging_industry_flag")
    private String strategicEmergingIndustryFlag;

    @IndexField("overseas_family_flag")
    private String overseasFamilyFlag;

    @IndexField("maintenance_period_flag")
    private String maintenancePeriodFlag;

    @IndexField("high_financing_quota_flag")
    private String highFinancingQuotaFlag;

    @IndexField("award_flag")
    private String awardFlag;

    @IndexField("merge_flag")
    private String mergeFlag;

    @IndexField("transfer_flag")
    private String transferFlag;

    @IndexField("claim_flag")
    private String claimFlag;

    @IndexField("shelf_flag")
    private String shelfFlag;

    @IndexField("view_count")
    private Long viewCount;

    @IndexField("leader_code")
    private String leaderCode;

    @IndexField("leader_name")
    private String leaderName;

}
