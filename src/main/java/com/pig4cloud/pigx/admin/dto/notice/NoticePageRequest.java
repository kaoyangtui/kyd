package com.pig4cloud.pigx.admin.dto.notice;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "通知公告分页查询请求")
public class NoticePageRequest extends BasePageQuery {

    @Schema(description = "关键词（标题/内容）")
    private String keyword;

    @Schema(description = "提交人")
    private String createBy;

    @Schema(description = "所属院系ID")
    private Long deptId;

    @Schema(description = "创建时间起")
    private String beginTime;

    @Schema(description = "创建时间止")
    private String endTime;
}
