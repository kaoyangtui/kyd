package com.pig4cloud.pigx.admin.dto.consult;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "咨询分页请求")
public class ConsultPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持内容模糊查询）")
    private String keyword;

    @Schema(description = "咨询类型")
    private String type;

    @Schema(description = "状态（0未读 1已读）")
    private Integer status;

    @Schema(description = "创建时间起")
    private String beginTime;

    @Schema(description = "创建时间止")
    private String endTime;

    @Schema(description = "门户用户 ID")
    private Long userId;
}