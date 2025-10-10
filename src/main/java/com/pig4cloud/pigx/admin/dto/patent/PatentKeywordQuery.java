package com.pig4cloud.pigx.admin.dto.patent;

import com.pig4cloud.pigx.admin.dto.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// 1) 定义包装请求体（新建一个类）
@Data
@Schema(description = "专利查询请求")
public class PatentKeywordQuery {
    @Schema(description = "分页参数")
    private PageRequest pageRequest;

    @Schema(description = "检索条件")
    private PatentSearchRequest request;
}
