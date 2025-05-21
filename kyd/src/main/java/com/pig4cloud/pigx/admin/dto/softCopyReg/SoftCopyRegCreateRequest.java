package com.pig4cloud.pigx.admin.dto.softCopyReg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 软著登记创建请求
 */
@Data
@Schema(description = "软著登记创建请求")
public class SoftCopyRegCreateRequest {

    @Schema(description = "主表数据")
    private SoftCopyRegMainRequest main;

    @Schema(description = "著作权人列表")
    private List<SoftCopyRegOwnerRequest> owners;

    @Schema(description = "完成人列表")
    private List<SoftCopyRegCompleterRequest> completers;
}
