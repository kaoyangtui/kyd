package com.pig4cloud.pigx.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "批量ID请求参数")
public class IdListRequest {

    @Schema(description = "ID列表")
    private List<Long> ids;
}
