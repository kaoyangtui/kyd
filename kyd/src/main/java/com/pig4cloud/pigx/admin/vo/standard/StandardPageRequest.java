package com.pig4cloud.pigx.admin.vo.standard;

import com.pig4cloud.pigx.admin.vo.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标准信息分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "标准信息分页查询请求")
public class StandardPageRequest extends BasePageQuery {

    @Schema(description = "关键字（支持标准号、名称模糊查询）")
    private String keyword;

    @Schema(description = "所属院系 ID")
    private String deptId;

    @Schema(description = "流程状态")
    private Integer flowStatus;

    @Schema(description = "当前流程节点名称")
    private String currentNodeName;

    @Schema(description = "发布时间起（yyyy-MM-dd）")
    private String pubBeginTime;

    @Schema(description = "发布时间止（yyyy-MM-dd）")
    private String pubEndTime;
}
