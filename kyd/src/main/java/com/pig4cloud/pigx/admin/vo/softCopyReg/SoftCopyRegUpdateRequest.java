package com.pig4cloud.pigx.admin.vo.softCopyReg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 软著登记更新请求
 *
 * @author zhaoliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "软著登记更新请求")
public class SoftCopyRegUpdateRequest extends SoftCopyRegCreateRequest {

    @Schema(description = "主表ID")
    private Long id;
}
