package com.pig4cloud.pigx.admin.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "通知公告修改请求")
public class NoticeUpdateRequest extends NoticeCreateRequest {

    @Schema(description = "主键ID")
    private Long id;
}
