package com.pig4cloud.pigx.admin.dto.researchPlatform;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ResearchPlatformResponse implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "科研平台名称")
    private String name;

    @Schema(description = "平台研究方向，多个以;分隔")
    private String direction;

    @Schema(description = "平台介绍")
    private String intro;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactPhone;

    @Schema(description = "平台负责人")
    private String principal;

    @Schema(description = "上下架状态（0下架 1上架）")
    private Integer shelfStatus;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    @Schema(description = "提交人")
    private String createBy;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "业务编码")
    private String bizCode;

    public static final String BIZ_CODE = "research_platform_list";
}