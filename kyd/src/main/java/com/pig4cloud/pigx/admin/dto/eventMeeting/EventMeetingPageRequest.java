package com.pig4cloud.pigx.admin.dto.eventMeeting;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动会议分页查询请求")
public class EventMeetingPageRequest extends BasePageQuery {

    @Schema(description = "活动名称关键词")
    private String keyword;

    @Schema(description = "举办方")
    private String organizer;

    @Schema(description = "活动提报人")
    private String createBy;

    @Schema(description = "活动时间起")
    private String beginTime;

    @Schema(description = "活动时间止")
    private String endTime;
}
