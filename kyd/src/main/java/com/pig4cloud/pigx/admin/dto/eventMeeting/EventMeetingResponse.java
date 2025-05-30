package com.pig4cloud.pigx.admin.dto.eventMeeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "活动会议响应")
public class EventMeetingResponse {

    public static final String BIZ_CODE = "EVENT_MEETING";

    @Schema(description = "主键ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "所属院系")
    private String deptId;

    @Schema(description = "组织名称")
    private String deptName;

    @Schema(description = "更新人")
    private String updateBy;
}
