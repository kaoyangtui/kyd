package com.pig4cloud.pigx.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 专利搜索列表DTO
 * @author: XX
 * @time: 2023/5/8
 */
@Data
@Schema(description = "专利搜索列表DTO")
public class PatentSearchListRes {

    @Schema(description = "专利图片")
    private String patentImage;

    @Schema(description = "专利名称")
    private String patentName;

    @Schema(description = "专利类型")
    private String patentType;

    @Schema(description = "法律状态")
    private String legalStatus;

    @Schema(description = "申请号")
    private String applicationNumber;

    @Schema(description = "申请日")
    private String applicationDate;

    @Schema(description = "公开号")
    private String publicationNumber;

    @Schema(description = "公开日")
    private String publicationDate;

    @Schema(description="授权日")
    private String grantDate;

    @Schema(description = "IPC分类")
    private String ipc;

    @Schema(description = "国民经济分类")
    private String nationalEconomy;

    @Schema(description = "申请人")
    private String currentApplicant;

    @Schema(description = "权利人")
    private String currentOwner;

    @Schema(description = "发明人")
    private String inventor;

    @Schema(description = "代理机构")
    private String agency;

    @Schema(description = "代理人")
    private String agent;

}
