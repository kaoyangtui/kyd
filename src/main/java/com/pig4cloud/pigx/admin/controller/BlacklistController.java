package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.PageRequest;
import com.pig4cloud.pigx.admin.dto.blacklist.*;
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.service.BlacklistService;
import com.pig4cloud.pigx.admin.utils.ExcelExportUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.admin.utils.PageUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.annotation.Sheet;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blacklist")
@Tag(description = "黑名单管理", name = "黑名单管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BlacklistController {

    private final BlacklistService blacklistService;

    @PostMapping("/create")
    @Operation(summary = "新增黑名单")
    public R<BlacklistResponse> create(@RequestBody @Valid BlacklistCreateRequest request) {
        return R.ok(blacklistService.createBlacklist(request));
    }

    @PostMapping("/update")
    @Operation(summary = "更新黑名单")
    public R<Boolean> update(@RequestBody BlacklistUpdateRequest request) {
        return R.ok(blacklistService.updateBlacklist(request));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询黑名单")
    public R<IPage<BlacklistResponse>> page(@ParameterObject PageRequest pageRequest,
                                            @ParameterObject BlacklistPageRequest request) {
        return R.ok(blacklistService.pageResult(PageUtil.toPage(pageRequest), request));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除黑名单")
    @SysLog("删除黑名单")
    public R<Boolean> remove(@RequestBody IdListRequest request) {
        return R.ok(blacklistService.removeBlacklist(request));
    }

    @PostMapping("/export/fields")
    @Operation(summary = "获取导出字段列表")
    public R<ExportFieldListResponse> exportFields() {
        ExportFieldListResponse fields = ExportFieldHelper.buildExportFieldList(
                BlacklistResponse.BIZ_CODE, BlacklistResponse.class
        );
        return R.ok(fields);
    }

    @PostMapping("/export")
    @Operation(summary = "导出黑名单")
    public void export(@RequestBody BlacklistExportWrapperRequest request) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前不是 HTTP 请求上下文，无法导出 Excel");
        }
        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("无法获取 HttpServletResponse");
        }
        IPage<BlacklistResponse> pageData = blacklistService.pageResult(new Page<>(), request.getQuery());
        ExcelExportUtil.exportByBean(
                response,
                "黑名单导出",
                "黑名单",
                pageData.getRecords(),
                request.getExport().getFieldKeys(),
                BlacklistResponse.class
        );
    }
}
