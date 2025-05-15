package com.pig4cloud.pigx.admin.vo.icLayout;

import com.pig4cloud.pigx.admin.vo.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 集成电路布图分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "集成电路布图分页查询请求")
public class IcLayoutPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持登记号、布图名称模糊查询）")
    private String keyword;

    @Schema(description = "所属院系 ID")
    private String deptId;

    @Schema(description = "公告时间起（yyyy-MM-dd）")
    private String publishBeginTime;

    @Schema(description = "公告时间止（yyyy-MM-dd）")
    private String publishEndTime;
}
