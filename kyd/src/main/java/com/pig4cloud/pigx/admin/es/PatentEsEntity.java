package com.pig4cloud.pigx.admin.es;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.FieldType;

import java.time.LocalDateTime;

@Data
@IndexName("patent_info")
@Schema(description = "专利信息")
public class PatentEsEntity {

    @IndexField(value = "id", fieldType = FieldType.LONG)
    private Long id;

    @IndexField(value = "pid", fieldType = FieldType.KEYWORD)
    private String pid;

    @IndexField(value = "title", fieldType = FieldType.TEXT)
    private String title;

    @IndexField(value = "draws", fieldType = FieldType.KEYWORD)
    private String draws;

    @IndexField(value = "abs", fieldType = FieldType.TEXT)
    private String abs;

    @IndexField(value = "pat_type", fieldType = FieldType.KEYWORD)
    private String patType;

    @IndexField(value = "lprs", fieldType = FieldType.KEYWORD)
    private String lprs;

    @IndexField(value = "status_code", fieldType = FieldType.KEYWORD)
    private String statusCode;

    @IndexField(value = "legal_status", fieldType = FieldType.KEYWORD)
    private String legalStatus;

    @IndexField(value = "app_number", fieldType = FieldType.KEYWORD)
    private String appNumber;

    @IndexField(value = "app_date", fieldType = FieldType.DATE)
    private String appDate;

    @IndexField(value = "pub_number", fieldType = FieldType.KEYWORD)
    private String pubNumber;

    @IndexField(value = "pub_date", fieldType = FieldType.DATE)
    private String pubDate;

    @IndexField(value = "examination_date", fieldType = FieldType.KEYWORD)
    private String examinationDate;

    @IndexField(value = "grant_date", fieldType = FieldType.KEYWORD)
    private String grantDate;

    @IndexField(value = "un_authorized_date", fieldType = FieldType.KEYWORD)
    private String unAuthorizedDate;

    @IndexField(value = "un_effective_date", fieldType = FieldType.KEYWORD)
    private String unEffectiveDate;

    @IndexField(value = "expire_date", fieldType = FieldType.KEYWORD)
    private String expireDate;

    @IndexField(value = "applicant_name", fieldType = FieldType.TEXT)
    private String applicantName;

    @IndexField(value = "applicant_type", fieldType = FieldType.KEYWORD)
    private String applicantType;

    @IndexField(value = "inventor_name", fieldType = FieldType.TEXT)
    private String inventorName;

    @IndexField(value = "patentee", fieldType = FieldType.TEXT)
    private String patentee;

    @IndexField(value = "nec", fieldType = FieldType.KEYWORD)
    private String nec;

    @IndexField(value = "main_ipc", fieldType = FieldType.KEYWORD)
    private String mainIpc;

    @IndexField(value = "ipc", fieldType = FieldType.KEYWORD)
    private String ipc;

    @IndexField(value = "ipc_section", fieldType = FieldType.KEYWORD)
    private String ipcSection;

    @IndexField(value = "ipc_class", fieldType = FieldType.KEYWORD)
    private String ipcClass;

    @IndexField(value = "ipc_sub_class", fieldType = FieldType.KEYWORD)
    private String ipcSubClass;

    @IndexField(value = "ipc_group", fieldType = FieldType.KEYWORD)
    private String ipcGroup;

    @IndexField(value = "ipc_sub_group", fieldType = FieldType.KEYWORD)
    private String ipcSubGroup;

    @IndexField(value = "cpc", fieldType = FieldType.KEYWORD)
    private String cpc;

    @IndexField(value = "cpc_section", fieldType = FieldType.KEYWORD)
    private String cpcSection;

    @IndexField(value = "cpc_class", fieldType = FieldType.KEYWORD)
    private String cpcClass;

    @IndexField(value = "cpc_sub_class", fieldType = FieldType.KEYWORD)
    private String cpcSubClass;

    @IndexField(value = "cpc_group", fieldType = FieldType.KEYWORD)
    private String cpcGroup;

    @IndexField(value = "cpc_sub_group", fieldType = FieldType.KEYWORD)
    private String cpcSubGroup;

    @IndexField(value = "agency_name", fieldType = FieldType.KEYWORD)
    private String agencyName;

    @IndexField(value = "agent_name", fieldType = FieldType.KEYWORD)
    private String agentName;

    @IndexField(value = "priority_country", fieldType = FieldType.KEYWORD)
    private String priorityCountry;

    @IndexField(value = "priority_no", fieldType = FieldType.KEYWORD)
    private String priorityNo;

    @IndexField(value = "priority", fieldType = FieldType.KEYWORD)
    private String priority;

    @IndexField(value = "first_priority", fieldType = FieldType.KEYWORD)
    private String firstPriority;

    @IndexField(value = "address", fieldType = FieldType.KEYWORD)
    private String address;

    @IndexField(value = "addr_province", fieldType = FieldType.KEYWORD)
    private String addrProvince;

    @IndexField(value = "addr_city", fieldType = FieldType.KEYWORD)
    private String addrCity;

    @IndexField(value = "addr_county", fieldType = FieldType.KEYWORD)
    private String addrCounty;

    @IndexField(value = "app_coun", fieldType = FieldType.KEYWORD)
    private String appCoun;

    @IndexField(value = "pub_country_code", fieldType = FieldType.KEYWORD)
    private String pubCountryCode;

    @IndexField(value = "pro_code", fieldType = FieldType.KEYWORD)
    private String proCode;

    @IndexField(value = "country_code", fieldType = FieldType.KEYWORD)
    private String countryCode;

    @IndexField(value = "country_name", fieldType = FieldType.KEYWORD)
    private String countryName;

    @IndexField(value = "province_code", fieldType = FieldType.KEYWORD)
    private String provinceCode;

    @IndexField(value = "province_name", fieldType = FieldType.KEYWORD)
    private String provinceName;

    @IndexField(value = "patent_words", fieldType = FieldType.TEXT)
    private String patentWords;

    @IndexField(value = "title_key", fieldType = FieldType.TEXT)
    private String titleKey;

    @IndexField(value = "cl_key", fieldType = FieldType.TEXT)
    private String clKey;

    @IndexField(value = "bg_key", fieldType = FieldType.TEXT)
    private String bgKey;

    @IndexField(value = "claims_quantity", fieldType = FieldType.INTEGER)
    private String claimsQuantity;

    @IndexField(value = "independent_claims_quantity", fieldType = FieldType.INTEGER)
    private String independentClaimsQuantity;

    @IndexField(value = "sub_claims_quantity", fieldType = FieldType.INTEGER)
    private String subClaimsQuantity;

    @IndexField(value = "patent_right_transfer_quantity", fieldType = FieldType.INTEGER)
    private String patentRightTransferQuantity;

    @IndexField(value = "application_right_transfer_quantity", fieldType = FieldType.INTEGER)
    private String applicationRightTransferQuantity;

    @IndexField(value = "license_quantity", fieldType = FieldType.INTEGER)
    private String licenseQuantity;

    @IndexField(value = "pledge_quantity", fieldType = FieldType.INTEGER)
    private String pledgeQuantity;

    @IndexField(value = "preservation_quantity", fieldType = FieldType.INTEGER)
    private String preservationQuantity;

    @IndexField(value = "cit_quantity", fieldType = FieldType.INTEGER)
    private String citQuantity;

    @IndexField(value = "pat_cited_quantity", fieldType = FieldType.INTEGER)
    private String patCitedQuantity;

    @IndexField(value = "npl_cited_quantity", fieldType = FieldType.INTEGER)
    private String nplCitedQuantity;

    @IndexField(value = "fw_cit_quantity", fieldType = FieldType.INTEGER)
    private String fwCitQuantity;

    @IndexField(value = "family_quantity", fieldType = FieldType.INTEGER)
    private String familyQuantity;

    @IndexField(value = "reexamination_decision_quantity", fieldType = FieldType.INTEGER)
    private String reexaminationDecisionQuantity;

    @IndexField(value = "reexamination_valid_quantity", fieldType = FieldType.INTEGER)
    private String reexaminationValidQuantity;

    @IndexField(value = "court_decision_quantity", fieldType = FieldType.INTEGER)
    private String courtDecisionQuantity;

    @IndexField(value = "same_app", fieldType = FieldType.KEYWORD)
    private String sameApp;

    @IndexField(value = "db_name", fieldType = FieldType.KEYWORD)
    private String dbName;

    @IndexField(value = "app_resource", fieldType = FieldType.KEYWORD)
    private String appResource;

    @IndexField(value = "iapp", fieldType = FieldType.KEYWORD)
    private String iapp;

    @IndexField(value = "ipub", fieldType = FieldType.KEYWORD)
    private String ipub;

    @IndexField(value = "den", fieldType = FieldType.KEYWORD)
    private String den;

    @IndexField(value = "pages", fieldType = FieldType.INTEGER)
    private String pages;

    @IndexField(value = "declassified_publication_date", fieldType = FieldType.KEYWORD)
    private String declassifiedPublicationDate;

    @IndexField(value = "iapp_no", fieldType = FieldType.KEYWORD)
    private String iappNo;

    @IndexField(value = "iapp_date", fieldType = FieldType.KEYWORD)
    private String iappDate;

    @IndexField(value = "ipub_no", fieldType = FieldType.KEYWORD)
    private String ipubNo;

    @IndexField(value = "ipub_date", fieldType = FieldType.KEYWORD)
    private String ipubDate;

    @IndexField(value = "pct_country", fieldType = FieldType.KEYWORD)
    private String pctCountry;

    @IndexField(value = "figure", fieldType = FieldType.INTEGER)
    private String figure;

    @IndexField(value = "patented_time", fieldType = FieldType.KEYWORD)
    private String patentedTime;

    @IndexField(value = "advanced_published_document", fieldType = FieldType.KEYWORD)
    private String advancedPublishedDocument;

    @IndexField(value = "history_patentee", fieldType = FieldType.KEYWORD)
    private String historyPatentee;

    @IndexField(value = "description", fieldType = FieldType.KEYWORD)
    private String description;

    @IndexField(value = "inventors", fieldType = FieldType.INTEGER)
    private String inventors;

    @IndexField(value = "subclass", fieldType = FieldType.KEYWORD)
    private String subclass;

    @IndexField(value = "simple_family_no", fieldType = FieldType.KEYWORD)
    private String simpleFamilyNo;

    @IndexField(value = "simple_family", fieldType = FieldType.KEYWORD)
    private String simpleFamily;

    @IndexField(value = "simple_family_quantity", fieldType = FieldType.INTEGER)
    private String simpleFamilyQuantity;

    @IndexField(value = "simple_family_country", fieldType = FieldType.KEYWORD)
    private String simpleFamilyCountry;

    @IndexField(value = "extend_family_no", fieldType = FieldType.KEYWORD)
    private String extendFamilyNo;

    @IndexField(value = "extend_family", fieldType = FieldType.KEYWORD)
    private String extendFamily;

    @IndexField(value = "extend_family_quantity", fieldType = FieldType.INTEGER)
    private String extendFamilyQuantity;

    @IndexField(value = "extend_family_country", fieldType = FieldType.KEYWORD)
    private String extendFamilyCountry;

    @IndexField(value = "patentee_addr", fieldType = FieldType.KEYWORD)
    private String patenteeAddr;

    @IndexField(value = "patentee_addr_province", fieldType = FieldType.KEYWORD)
    private String patenteeAddrProvince;

    @IndexField(value = "patentee_addr_city", fieldType = FieldType.KEYWORD)
    private String patenteeAddrCity;

    @IndexField(value = "patentee_addr_county", fieldType = FieldType.KEYWORD)
    private String patenteeAddrCounty;

    @IndexField(value = "create_by", fieldType = FieldType.KEYWORD)
    private String createBy;

    @IndexField(value = "patente_type", fieldType = FieldType.KEYWORD)
    private String patenteType;

    @IndexField(value = "create_time", fieldType = FieldType.DATE)
    private LocalDateTime createTime;

    @IndexField(value = "primary_applicant_name", fieldType = FieldType.KEYWORD)
    private String primaryApplicantName;

    @IndexField(value = "update_by", fieldType = FieldType.KEYWORD)
    private String updateBy;

    @IndexField(value = "primary_inventor_name", fieldType = FieldType.KEYWORD)
    private String primaryInventorName;

    @IndexField(value = "update_time", fieldType = FieldType.DATE)
    private LocalDateTime updateTime;

    @IndexField(value = "primary_patentee", fieldType = FieldType.KEYWORD)
    private String primaryPatentee;

    @IndexField(value = "del_flag", fieldType = FieldType.KEYWORD)
    private String delFlag;

    @IndexField(value = "divide_init_app", fieldType = FieldType.KEYWORD)
    private String divideInitApp;

    @IndexField(value = "tenant_id", fieldType = FieldType.LONG)
    private Long tenantId;

    @IndexField(value = "divide_init_app_date", fieldType = FieldType.KEYWORD)
    private String divideInitAppDate;

    @IndexField(value = "divide_init_app_no", fieldType = FieldType.KEYWORD)
    private String divideInitAppNo;

    @IndexField(value = "applicant_quantity", fieldType = FieldType.INTEGER)
    private String applicantQuantity;

    @IndexField(value = "agent_quantity", fieldType = FieldType.INTEGER)
    private String agentQuantity;

    @IndexField(value = "patentee_quantity", fieldType = FieldType.INTEGER)
    private String patenteeQuantity;

    @IndexField(value = "ipc_quantity", fieldType = FieldType.INTEGER)
    private String ipcQuantity;

    @IndexField(value = "overseas_family_quantity", fieldType = FieldType.INTEGER)
    private String overseasFamilyQuantity;

    @IndexField(value = "invalid_reason_code", fieldType = FieldType.KEYWORD)
    private String invalidReasonCode;

    @IndexField(value = "high_value_flag", fieldType = FieldType.KEYWORD)
    private String highValueFlag;

    @IndexField(value = "strategic_emerging_industry_flag", fieldType = FieldType.KEYWORD)
    private String strategicEmergingIndustryFlag;

    @IndexField(value = "overseas_family_flag", fieldType = FieldType.KEYWORD)
    private String overseasFamilyFlag;

    @IndexField(value = "maintenance_period_flag", fieldType = FieldType.KEYWORD)
    private String maintenancePeriodFlag;

    @IndexField(value = "high_financing_quota_flag", fieldType = FieldType.KEYWORD)
    private String highFinancingQuotaFlag;

    @IndexField(value = "award_flag", fieldType = FieldType.KEYWORD)
    private String awardFlag;

    @IndexField(value = "merge_flag", fieldType = FieldType.KEYWORD)
    private String mergeFlag;

    @IndexField(value = "transfer_flag", fieldType = FieldType.KEYWORD)
    private String transferFlag;

    @IndexField(value = "claim_flag", fieldType = FieldType.KEYWORD)
    private String claimFlag;

    @IndexField(value = "shelf_flag", fieldType = FieldType.KEYWORD)
    private String shelfFlag;

    @IndexField(value = "view_count", fieldType = FieldType.LONG)
    private Long viewCount;

    @IndexField(value = "leader_code", fieldType = FieldType.KEYWORD)
    private String leaderCode;

    @IndexField(value = "leader_name", fieldType = FieldType.KEYWORD)
    private String leaderName;
}
