package com.pig4cloud.pigx.admin.dto.softCopyReg;

import com.pig4cloud.pigx.admin.dto.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "软著登记分页查询请求")
public class SoftCopyRegPageRequest extends BasePageQuery {

    @Schema(description = "关键字，匹配登记号、软著名称")
    private String keyword;

    @Schema(description = "流程状态：-2暂存 -1运行中 0完成 1作废 2撤回")
    private Integer flowStatus;

    @Schema(description = "流程节点名称")
    private String currentNodeName;

    @Schema(description = "创建开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "创建结束时间 yyyy-MM-dd")
    private String endTime;

    @Schema(description = "所属组织 ID")
    private Long deptId;
}
