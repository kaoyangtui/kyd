package com.pig4cloud.pigx.admin.dto.eventMeeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "活动会议修改请求")
public class EventMeetingUpdateRequest extends EventMeetingCreateRequest {

    @Schema(description = "主键ID")
    private Long id;

}
