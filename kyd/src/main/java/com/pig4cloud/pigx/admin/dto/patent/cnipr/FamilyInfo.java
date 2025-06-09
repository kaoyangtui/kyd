package com.pig4cloud.pigx.admin.dto.patent.cnipr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "同族信息 DTO")
public class FamilyInfo {

    @Schema(description = "申请号")
    private List<String> appNumber;

    @Schema(description = "申请日")
    private String appDate;

    @Schema(description = "公开公告号")
    private List<String> pubNumber;

    @Schema(description = "公开公告日")
    private String pubDate;
}
