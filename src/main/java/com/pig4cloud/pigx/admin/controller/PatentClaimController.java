package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.IdRequest;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.patent.*;
import com.pig4cloud.pigx.admin.service.PatentClaimService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * 专利认领表
 *
 * @author pigx
 * @date 2025-07-14 08:58:50
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patentClaim")
@Tag(description = "patentClaim", name = "专利认领表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatentClaimController {

    private final PatentClaimService patentClaimService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public R<IPage<PatentClaimResponse>> page(@ParameterObject Page<?> page, @ParameterObject PatentClaimPageRequest request) {
        return R.ok(patentClaimService.pageResult(page, request));
    }

    @PostMapping("/detail")
    @Operation(summary = "详情")
    public R<PatentClaimResponse> detail(@RequestBody IdRequest request) {
        return R.ok(patentClaimService.getDetail(request.getId()));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    @SysLog("删除专利认领")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(patentClaimService.removeClaims(request.getIds()));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                PatentClaimResponse.BIZ_CODE,
                PatentClaimResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    public void export(@RequestBody PatentClaimExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }
        IPage<PatentClaimResponse> pageData = patentClaimService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "专利认领导出",
                "专利认领列表",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                PatentClaimResponse.class
        );
    }

    @Operation(summary = "专利认领")
    @PostMapping("/claim")
    public R<Boolean> claim(@RequestBody PatentClaimCreateRequest req) {
        return R.ok(patentClaimService.claim(req));
    }

    @Operation(summary = "专利否认领")
    @PostMapping("/unclaim")
    public R<Boolean> unclaim(@RequestBody PatentUnClaimRequest req) {
        return R.ok(patentClaimService.unClaim(req));
    }
}