package com.pig4cloud.pigx.admin.dto.transformCase;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "转化案例分页查询请求")
public class TransformCasePageRequest extends BasePageQuery {

    @Schema(description = "标题关键词")
    private String keyword;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "开始时间（yyyy-MM-dd）")
    private String beginTime;

    @Schema(description = "结束时间（yyyy-MM-dd）")
    private String endTime;
}
