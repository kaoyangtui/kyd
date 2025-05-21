package com.pig4cloud.pigx.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 通用分页查询请求（支持分页、按 ID 查询、范围导出）
 * 建议所有分页请求统一继承此类
 *
 * @author zhaoliang
 */
@Data
@Schema(description = "通用分页查询请求（支持分页、按 ID、范围导出）")
public class BasePageQuery {

    @Schema(description = "指定查询 ID 列表（优先级最高）")
    private List<Long> ids;

    @Schema(description = "起始条数（仅在 range 模式下有效）")
    private Long startNo;

    @Schema(description = "结束条数（仅在 range 模式下有效）")
    private Long endNo;
}
