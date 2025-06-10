package com.pig4cloud.pigx.admin.dto.researchNews;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "科研动态分页请求")
public class ResearchNewsPageRequest extends BasePageQuery {
    @Schema(description = "关键词（标题/内容/供稿）")
    private String keyword;

    @Schema(description = "提交人")
    private String createBy;

}
