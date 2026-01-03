package com.pig4cloud.pigx.admin.dto.patent;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "专利信息分页查询请求")
public class PatentPageRequest extends BasePageQuery {

    @Schema(description = "关键词（名称/专利申请号）")
    private String keyword;

    @Schema(description = "专利类型")
    private List<String> patType;

    @Schema(description = "法律状态")
    private String legalStatus;

    @Schema(description = "申请人")
    private String applicantName;

    @Schema(description = "发明人")
    private String inventorName;

    @Schema(description = "所属院系")
    private Long deptId;

    @Schema(description = "申请日起")
    private LocalDate beginAppDate;

    @Schema(description = "申请日止")
    private LocalDate endAppDate;

    @Schema(description = "公开日起")
    private LocalDate beginPubDate;

    @Schema(description = "公开日止")
    private LocalDate endPubDate;


    @Schema(description = "授权日起")
    private LocalDate beginGrantDate;

    @Schema(description = "授权日止")
    private LocalDate endGrantDate;

    @Schema(description = "代理机构")
    private String agencyName;

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "申请号合并标识,1合并查询")
    private String mergeFlag;

    @Schema(description = "转移标识,1已转移")
    private String transferFlag;

    @Schema(description = "认领标识,1已认领")
    private String claimFlag;

    @Schema(description = "上架标识,1已上架")
    private String shelfFlag;

}
