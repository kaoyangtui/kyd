package com.pig4cloud.pigx.admin.dto.eventMeeting;

import com.pig4cloud.pigx.admin.dto.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "活动会议报名信息响应")
public class EventMeetingApplyResponse extends BaseResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "关联活动ID")
    private Long meetingId;

    @Schema(description = "报名时间")
    private String applyTime;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "所属单位")
    private String organization;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "备注")
    private String remark;

}
