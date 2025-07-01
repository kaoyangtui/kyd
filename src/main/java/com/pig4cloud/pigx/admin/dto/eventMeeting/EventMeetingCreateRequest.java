package com.pig4cloud.pigx.admin.dto.eventMeeting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "活动会议创建请求")
public class EventMeetingCreateRequest {

    @NotBlank(message = "图片地址不能为空")
    @Schema(description = "图片地址")
    private String imgUrl;

    @NotBlank(message = "活动名称不能为空")
    @Schema(description = "活动名称")
    private String name;

    @NotBlank(message = "举办方不能为空")
    @Schema(description = "举办方")
    private String organizer;

    @NotNull(message = "活动时间不能为空")
    @Schema(description = "活动时间")
    private LocalDateTime eventTime;

    @NotBlank(message = "活动地点不能为空")
    @Schema(description = "活动地点")
    private String location;

    @NotBlank(message = "活动内容不能为空")
    @Schema(description = "活动内容")
    private String content;

    @NotNull(message = "报名开始时间不能为空")
    @Schema(description = "报名开始时间")
    private LocalDateTime signUpStart;

    @NotNull(message = "报名截止时间不能为空")
    @Schema(description = "报名截止时间")
    private LocalDateTime signUpEnd;

    @Schema(description = "附件路径（多个用;分隔）")
    private List<String> fileUrl;
}
