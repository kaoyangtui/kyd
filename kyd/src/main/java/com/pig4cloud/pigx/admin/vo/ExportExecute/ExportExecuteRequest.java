package com.pig4cloud.pigx.admin.vo.ExportExecute;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "通用导出执行请求")
public class ExportExecuteRequest {

    @Schema(description = "导出字段 keys")
    private List<String> fieldKeys;
}
