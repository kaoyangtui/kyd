package com.pig4cloud.pigx.admin.vo.consult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "咨询信息响应")
public class ConsultResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "咨询类型")
    private String type;

    @Schema(description = "咨询目标编码")
    private String targetCode;

    @Schema(description = "咨询目标名称")
    private String targetName;

    @Schema(description = "咨询内容")
    private String content;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "前台用户 ID")
    private Long appUserId;

    @Schema(description = "咨询状态（0未读 1已读）")
    private Integer status;

    @Schema(description = "回复人")
    private String replyBy;

    @Schema(description = "回复时间")
    private String replyTime;

    @Schema(description = "回复内容")
    private String replyContent;

    public static final String BIZ_CODE = "consult_list";
}
