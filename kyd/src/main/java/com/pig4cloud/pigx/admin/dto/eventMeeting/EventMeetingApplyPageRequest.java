package com.pig4cloud.pigx.admin.dto.eventMeeting;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动会议报名信息分页查询请求")
public class EventMeetingApplyPageRequest extends BasePageQuery {

    @Schema(description = "关联活动ID")
    private Long meetingId;

    @Schema(description = "门户用户 ID")
    private Long userId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "联系电话")
    private String phone;
}
