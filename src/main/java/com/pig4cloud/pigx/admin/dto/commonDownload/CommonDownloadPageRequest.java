package com.pig4cloud.pigx.admin.dto.commonDownload;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "常用下载分页查询请求")
public class CommonDownloadPageRequest extends BasePageQuery {

    @Schema(description = "关键词（标题/内容/供稿）")
    private String keyword;

    @Schema(description = "提交人")
    private String createBy;

    @Schema(description = "所属院系ID")
    private String deptId;

    @Schema(description = "创建时间起（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "创建时间止（yyyy-MM-dd）")
    private String endTime;
}
