package com.pig4cloud.pigx.admin.dto.eventMeeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "活动会议创建请求")
public class EventMeetingCreateRequest {

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "举办方")
    private String organizer;

    @Schema(description = "活动时间")
    private String eventTime;

    @Schema(description = "活动地点")
    private String location;

    @Schema(description = "活动内容")
    private String content;

    @Schema(description = "报名开始时间")
    private String signUpStart;

    @Schema(description = "报名截止时间")
    private String signUpEnd;

    @Schema(description = "附件路径（多个用;分隔）")
    private List<String> fileUrl;
}
