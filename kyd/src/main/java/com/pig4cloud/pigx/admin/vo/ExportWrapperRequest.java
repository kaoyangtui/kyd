package com.pig4cloud.pigx.admin.vo;

import com.pig4cloud.pigx.admin.vo.exportExecute.ExportExecuteRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用导出包装请求
 * 泛型 Q 为分页查询请求，要求继承 BasePageQuery
 *
 * @author zhaoliang
 */
@Data
@Schema(description = "导出包装请求")
public class ExportWrapperRequest<Q extends BasePageQuery> {

    @Schema(description = "分页查询参数")
    private Q query;

    @Schema(description = "导出执行参数（字段配置等）")
    private ExportExecuteRequest export;
}
