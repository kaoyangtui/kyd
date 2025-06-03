package com.pig4cloud.pigx.admin.dto.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "资讯动态统一响应")
public class NewsResponse {
    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容摘要")
    private String summary;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "来源类型")
    private String sourceType;

    @Schema(description = "业务主键ID")
    private Long id;
}
