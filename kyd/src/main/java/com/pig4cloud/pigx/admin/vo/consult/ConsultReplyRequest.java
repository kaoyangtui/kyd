package com.pig4cloud.pigx.admin.vo.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "咨询回复请求")
public class ConsultReplyRequest {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "回复内容")
    private String replyContent;
}