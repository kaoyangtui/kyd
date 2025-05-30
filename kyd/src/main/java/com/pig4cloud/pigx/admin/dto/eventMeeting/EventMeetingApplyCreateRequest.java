package com.pig4cloud.pigx.admin.dto.eventMeeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "活动会议报名信息创建请求")
public class EventMeetingApplyCreateRequest {

    @Schema(description = "关联活动ID")
    private Long meetingId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "所属单位")
    private String organization;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "备注")
    private String remark;
}
