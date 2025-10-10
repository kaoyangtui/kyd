package com.pig4cloud.pigx.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhaoliang
 */
@Data
@Schema(description = "ID请求")
public class IdRequest {
    private Long id;
    private String code;
}
