package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "专利ES分页查询请求")
public class PatentEsPageRequest {

    @Schema(description = "关键词（名称/申请号）")
    private String keyword;

    @Schema(description = "专利类型（多选：发明申请、发明授权、实用新型、外观设计）")
    private List<String> patentTypes;

    @Schema(description = "法律状态（有效、失效、审中）")
    private List<String> legalStatuses;

    @Schema(description = "申请日-开始")
    private LocalDate appDateStart;

    @Schema(description = "申请日-结束")
    private LocalDate appDateEnd;

    @Schema(description = "公告日-开始")
    private LocalDate pubDateStart;

    @Schema(description = "公告日-结束")
    private LocalDate pubDateEnd;

    @Schema(description = "申请人（关键词搜索）")
    private String applicantName;

    @Schema(description = "发明人（关键词搜索）")
    private String inventorName;

    @Schema(description = "IPC分类（支持多选）")
    private List<String> ipc;

    @Schema(description = "国民经济行业（多选）")
    private List<String> industries;

    @Schema(description = "国家战略性新兴产业（多选）")
    private List<String> strategicIndustries;

    @Schema(description = "江苏省高新技术产业（多选）")
    private List<String> highTechIndustries;

    @Schema(description = "知识产权密集型技术（多选）")
    private List<String> ipTechTypes;

    @Schema(description = "所属区域（如：秦淮区）")
    private String region;

    @Schema(description = "区域级别（如：省、市、区）")
    private String regionLevel;

    @Schema(description = "排序字段（如：appDate、pubDate）")
    private String orderField;

    @Schema(description = "排序方向（asc / desc）")
    private String orderType;

    @Schema(description = "页码，从1开始")
    private long current = 1;

    @Schema(description = "每页条数")
    private long size = 10;
}
