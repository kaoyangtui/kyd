package com.pig4cloud.pigx.admin.vo.SoftCopy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 软著提案新增请求 DTO
 *
 * @author zhaoliang
 */
@Data
@Schema(description = "软著提案新增请求")
public class SoftCopyCreateRequest {

    @Schema(description = "软著主表信息")
    private SoftCopyMainRequest main;

    @Schema(description = "完成人信息列表")
    private List<SoftCopyCompleterRequest> completers;

    @Schema(description = "著作权人信息列表")
    private List<SoftCopyOwnerRequest> owners;
}