package com.pig4cloud.pigx.admin.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    @Schema(description = """
            排序:
            按发布时间排序 column=shelf_time
            按热度排序 column=view_count
            """)
    private List<OrderItem> orders;
}
