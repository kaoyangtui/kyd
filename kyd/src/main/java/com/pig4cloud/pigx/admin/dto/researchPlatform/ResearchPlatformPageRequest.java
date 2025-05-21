package com.pig4cloud.pigx.admin.dto.researchPlatform;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResearchPlatformPageRequest extends BasePageQuery {

    @Schema(description = "科研平台名称")
    private String name;

    @Schema(description = "平台负责人")
    private String principal;

    @Schema(description = "状态")
    private Integer shelfStatus;

    @Schema(description = "提交人")
    private String createBy;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "提交开始时间")
    private String beginTime;

    @Schema(description = "提交结束时间")
    private String endTime;

}