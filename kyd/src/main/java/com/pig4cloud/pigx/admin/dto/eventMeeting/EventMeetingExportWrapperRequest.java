package com.pig4cloud.pigx.admin.dto.eventMeeting;

import com.pig4cloud.pigx.admin.dto.ExportWrapperRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动会议导出封装请求")
public class EventMeetingExportWrapperRequest extends ExportWrapperRequest<EventMeetingPageRequest> {
}
