package com.pig4cloud.pigx.admin.dto.nationalPatent;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

/**
 * 我关注的全国专利 - 分页请求
 */
@Data
@Schema(description = "我关注的全国专利-分页请求")
public class NationalPatentFollowPageReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "页码（从1开始）", example = "1")
    @Min(1)
    private long pageNo = 1;

    @Schema(description = "每页条数", example = "10")
    @Min(1)
    private long pageSize = 10;

    @Schema(description = "标题关键字（可选，模糊匹配）", example = "锂电池")
    private String keyword;
}
