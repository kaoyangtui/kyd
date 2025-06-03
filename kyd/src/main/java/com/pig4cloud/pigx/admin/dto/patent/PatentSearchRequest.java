package com.pig4cloud.pigx.admin.dto.patent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "专利检索请求")
public class PatentSearchRequest {

    @Schema(description = "检索关键词")
    private String keyword;

    @Schema(description = "页码")
    private Integer pageNo = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
