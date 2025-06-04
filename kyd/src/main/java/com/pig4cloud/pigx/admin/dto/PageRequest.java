package com.pig4cloud.pigx.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import java.util.List;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "分页请求参数")
public class PageRequest {
    @Schema(description = "当前页码", example = "1")
    private long current = 1;
    @Schema(description = "每页条数", example = "10")
    private long size = 10;
    @Schema(description = "排序字段和顺序")
    private List<OrderItem> orders;
}
