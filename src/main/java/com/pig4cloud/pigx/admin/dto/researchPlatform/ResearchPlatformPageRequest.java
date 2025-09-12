package com.pig4cloud.pigx.admin.dto.researchPlatform;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研平台分页查询请求")
public class ResearchPlatformPageRequest extends BasePageQuery {

    @Schema(description = "关键词（名称/研究方向/介绍等模糊匹配）")
    private String keyword;

    @Schema(description = "负责人编码")
    private String leaderCode;

    @Schema(description = "所属院系 ID")
    private Long deptId;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;

    @Schema(description = "研究方向；多个用;分隔")
    private List<String> direction;

    @Schema(description = "提交时间起（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "提交时间止（yyyy-MM-dd）")
    private String endTime;

}
