package com.pig4cloud.pigx.admin.dto.result;

import com.pig4cloud.pigx.admin.dto.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// 用于 POST Body 的包装类
@Data
@Schema(description = "科研成果查询请求")
public class ResultQueryRequest {
    @Schema(description = "分页请求")
    private PageRequest pageRequest;

    @Schema(description = "筛选条件")
    private ResultPageRequest request;
}
