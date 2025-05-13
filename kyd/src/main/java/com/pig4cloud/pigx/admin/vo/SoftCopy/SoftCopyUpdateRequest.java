package com.pig4cloud.pigx.admin.vo.SoftCopy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 软著提案修改请求
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "软著提案修改请求")
public class SoftCopyUpdateRequest extends SoftCopyCreateRequest {

    @Schema(description = "主键")
    private Long id;
}
